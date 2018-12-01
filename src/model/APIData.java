package model;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a caller to the alphavantage API.
 */
class APIData {
  private Map<String, String> nameToCode;
  private Map<String, Map<String, Map<String, Double>>> prices;
  private String apiKey2 = "SSO4MPHRUSM6YMEB";
  private String apiKey = "UISBJFEXNUUOZ3II";
  private String apiKey3 = "3KC45X2HPAQ6X05J";
  private URL url = null;


  /**
   * This constructor will hold the data obtained from the API in a map object.
   */
  APIData() {
    this.nameToCode = new HashMap<>();
    this.prices = new HashMap<>();
  }

  /**
   * This method builds the query URL for the API and obtains the ticker symbol from it. It returns
   * this ticker symbol as a String.
   *
   * @param companyName or ticker symbol of the company to be looked up with the API.
   * @return ticker symbol of desired company as a String.
   */
  String searchCode(String companyName) {

    try {
      url = new URL("https://www.alphavantage.co/query?function=SYMBOL_SEARCH&"
              + "keywords=" + companyName
              + "&apikey=" + apiKey2
              + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    try {
      in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No Tickr found for " + companyName);
    }

    String[] value = output.toString().split("\n");
    String[] row = value[1].split(",");
    this.nameToCode.put(companyName, row[0]);

    return this.nameToCode.get(companyName);
  }

  /**
   * Gets the prices of a specified stock. It returns 4 different prices for the day and the amount
   * of shares moved by the company. The 4 different prices are: open, close, high, and low. The
   * user will be able select which price type to buy the stock at in the future for training
   * purposes. However, the current version limits the user to buying stock at the opening price.
   *
   * @param tickrCode for the company to buy stocks of.
   * @param date      that the stocks will be bought.
   * @param type      of price the user will give to select the price point to buy at.
   * @return price of a single share of this stock.
   * @throws IllegalArgumentException if the url query does not work or if the ticker symbol did not
   *                                  return any data
   */
  Double getPrices(String tickrCode, String date, String type)
          throws IllegalArgumentException, InterruptedException {

    try {
      url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + tickrCode + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }

    InputStream in = null;
    StringBuilder output = new StringBuilder();

    Thread.sleep(40000);
    try {
      in = url.openStream();
      int b;
      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + tickrCode);
    }


    Map<String, Map<String, Double>> res = new HashMap<String, Map<String, Double>>();
    String[] value = output.toString().split("\n");
    String[] category = {"open", "high", "low", "close", "volume"};

    for (int i = 1; i < value.length; i++) {
      String[] row = value[i].split(",");
      res.put(row[0], new HashMap<String, Double>());
      for (int j = 1; j < row.length; j++) {
        res.get(row[0]).put(category[j - 1], Double.parseDouble(row[j]));
      }
    }
    this.prices.put(tickrCode, res);


    Map<String, Map<String, Double>> resu = this.prices.get(tickrCode);
    Double result = 0.00;
    try {
      result = helperPrice(resu, date, type);

    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalArgumentException("No Info");
    }
    return result;
  }

  Double helperPrice(Map<String, Map<String, Double>> resu, String date, String type)
          throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date today = new Date();
    Double out = 0.00;

    if (formatter.parse(date).compareTo(today) == 0) {

      return out;

    } else if (resu.containsKey(date)) {

      return resu.get(date).get(type);
    } else {
      Calendar next_date = Calendar.getInstance();
      next_date.setTime(formatter.parse(date));
      next_date.add(Calendar.DATE, 1);
      Date new_date = next_date.getTime();

      out = helperPrice(resu, formatter.format(new_date), type);
    }
    return out;
  }
}
