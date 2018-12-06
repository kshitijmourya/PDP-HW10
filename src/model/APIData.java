package model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
  private Map<String, Double> prices;
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
  String searchCode(String companyName) throws IllegalArgumentException {
    if (companyName.equals("")) {
      throw new IllegalArgumentException("Please enter a valid company name.");
    }
    try {
      Thread.sleep(20000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    try {
      url = new URL("https://www.alphavantage.co/query?function=SYMBOL_SEARCH&"
              + "keywords=" + companyName
              + "&apikey=" + apiKey
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
   * Saves csv file obtained from the url stock api.
   *
   * @param tickrCode for the company to buy stocks of.
   * @throws InterruptedException
   */
  private void saveStock(String tickrCode) throws InterruptedException {
    try {
      url = new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + tickrCode + "&apikey=" + apiKey3
              + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new RuntimeException("the alphavantage API has either changed or "
              + "no longer works");
    }

    InputStream in_2 = null;
    try {
      in_2 = url.openStream();
      Files.copy(in_2, Paths.get("stocks", tickrCode + ".csv"), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Thread.sleep(20000);
  }

  /**
   * Reads in and pulls data from the saved file obtained from the url. If the file does not exist,
   * then it will initiate a grab from the stock API.
   *
   * @param tickrCode for the company to buy stocks of.
   * @param date      that the stocks will be bought.
   * @param type      of price the user will give to select the price point to buy at.
   * @return array of file lines for the queried stock.
   * @throws IllegalArgumentException
   * @throws InterruptedException
   */
  private String[] getFile(String tickrCode, String date, String type)
          throws IllegalArgumentException, InterruptedException {

    BufferedReader in = null;
    StringBuilder output = new StringBuilder();

    try {
      in = new BufferedReader(new FileReader("stocks/" + tickrCode + ".csv"));
      String b;
      while ((b = in.readLine()) != null) {
        output.append(b + "\n");
      }
    } catch (FileNotFoundException e) {
      saveStock(tickrCode);
      getPrices(tickrCode, date, type);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return output.toString().split("\n");
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
    Map<String, Double> res = new HashMap<>();
    String[] category = {"open", "high", "low", "close", "volume"};
    String [] value = getFile(tickrCode, date, type);

    for (int i = 1; i < value.length; i++) {
      String[] row = value[i].split(",");

      if (row[0].equals(date)) {

        for (int j = 0; j < row.length - 1; j++) {
          res.put(category[j], Double.parseDouble(row[j+1]));
        }
      }
    }

    if (!res.isEmpty()) {
      return res.get("open");
    } else {
      throw new IllegalArgumentException("No Info");
    }
  }
}
