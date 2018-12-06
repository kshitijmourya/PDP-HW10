package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class represents a single stock interest, to be stores in the users portfolio.
 */
class Stock {
  private String ticker;
  private int shares;
  private double cost;
  private LinkedHashMap<String, List<String>> log = new LinkedHashMap<>();

  /**
   * Constructor for a single stock object. It will hold the total dealings with a particular
   * company in shares, cost of stocks purchased, and the ticker symbol for the company.
   *
   * @param commision   amount in dollars to pay for broker services.
   * @param companyName or ticker symbol that can be used to look the company up.
   * @param date        that the stock is going to be bought.
   * @param type        of price the shares will be bought at (i.e. open, close, high, low)
   * @param shares      to be boughtfor the company.
   */
  Stock(double commision, String companyName, String date, String type, int shares) {
    APIData stock_data = new APIData();
    String code = stock_data.searchCode(companyName);

    this.ticker = code;
    sharesTransaction(commision, date, type, shares);
  }

  /**
   * Constructor for a single stock object. It will hold the total dealings with a particular
   * company in dollar amount, cost of stocks purchased, and the ticker symbol for the company.
   *
   * @param commision   amount in dollars to pay for broker services.
   * @param companyName or ticker symbol that can be used to look the company up.
   * @param date        that the stock is going to be bought.
   * @param type        of price the shares will be bought at (i.e. open, close, high, low)
   * @param investment  amount in dollars to use for buying stocks.
   */
  Stock(double commision, String companyName, String date, String type, double investment) {
    APIData stock_data = new APIData();
    String code = stock_data.searchCode(companyName);
    this.ticker = code;

    monetaryTransaction(commision, date, type, investment);
  }

  /**
   * Bare bones constructor for stock, that does not make any initial investments but
   * instead just instantiates with the ticker identifier.
   *
   * @param companyName
   */
  Stock(String companyName) {
    this.ticker = companyName;
  }

  /**
   * Get ticker symbol from this stock object.
   *
   * @return ticker symbol as a String.
   */
  String getTicker() {
    return this.ticker;
  }

  /**
   * Get the number of shares owned in this stock.
   *
   * @return shares owned of this stock as an int.
   */
  int getShares() {
    Iterator log_iterator = this.log.keySet().iterator();
    int shares = 0;
    while (log_iterator.hasNext()){
      shares += Integer.parseInt(this.log.get(log_iterator.next()).get(1));
    }
    return shares;
  }

  /**
   * Get the total running cost of all the shares in this stock.
   *
   * @return costs of buying shares of this stock as a double.
   */
  double getCost() {
    Iterator log_iterator = this.log.keySet().iterator();
    double cost = 0;
    while (log_iterator.hasNext()){
      cost += Double.parseDouble(this.log.get(log_iterator.next()).get(0));
    }
    return cost;
  }

  /**
   * Converts all the contents of this stock into an easy to read String of information.
   *
   * @return String of stock object summary.
   */
  @Override
  public String toString() {
    String stock_information =
            "\t" + "Ticker Symbol: " + this.ticker
                    + "\n" + "\t" + "Total Shares Owned: " + this.getShares()
                    + "\n" + "\t" + "Total Running Cost of Stock: " + this.getCost() + "\n\n";

    return stock_information;
  }

  /**
   * Initiates transaction for the stock with specified shares.
   *
   * @param commision   amount in dollars to pay for broker services.
   * @param date        that the stock is going to be bought.
   * @param type        of price the shares will be bought at (i.e. open, close, high, low)
   * @param shares      to be boughtfor the company.
   */
  void sharesTransaction(double commision, String date, String type, int shares) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    APIData stock_data = new APIData();
    LocalDate next_date = LocalDate.parse(date).plusDays(1);
    String code = this.getTicker();

    double price = 0;

    try {
      try {
        price = stock_data.getPrices(code, date, type);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } catch (IllegalArgumentException e) {
      sharesTransaction(commision, next_date.format(formatter), type, shares);
      return;
    }

    this.updateLog(date, commision, price, shares);
  }

  /**
   * Initiates transaction for the stock with specified investment amount in dollars.
   *
   * @param commision   amount in dollars to pay for broker services.
   * @param date        that the stock is going to be bought.
   * @param type        of price the shares will be bought at (i.e. open, close, high, low)
   * @param investment  amount in dollars to use for buying stocks.
   */
  void monetaryTransaction(double commision, String date, String type, double investment) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    APIData stock_data = new APIData();
    LocalDate next_date = LocalDate.parse(date).plusDays(1);
    String code = this.getTicker();

    double price = 0;

    try {
      try {
        price = stock_data.getPrices(code, date, type);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    } catch (IllegalArgumentException e) {
      monetaryTransaction(commision, next_date.format(formatter), type, investment);
      return;
    }

    int shares = Math.toIntExact(Math.round(investment / price));

    this.updateLog(date, commision, price, shares);
  }

  /**
   *
   * @param date        that the stock is going to be bought.
   * @param commision   amount in dollars to pay for broker services.
   * @param price       amount in dollar for each of the shares bought in the transaction.
   * @param shares      to be boughtfor the company.
   */
  void updateLog(String date, double commision, double price, int shares) {
    List my_stock = new ArrayList();

    if (!this.log.containsKey(date)) {
      my_stock.add(0, String.valueOf(commision + price * shares)); // cost
      my_stock.add(1, String.valueOf(shares)); // shares
      my_stock.add(2, String.valueOf(commision)); // commission
      my_stock.add(3, String.valueOf(price)); // price
      this.log.put(date, my_stock);

    } else {
      double cost = Double.parseDouble(this.log.get(date).get(0)) + price * shares; // adjusted cost
      my_stock.add(0, String.valueOf(commision + cost)); // adjusted cost
      my_stock.add(1, String.valueOf(Integer.parseInt(this.log.get(date).get(1)) +
              shares)); // adjusted shares
      my_stock.add(2, String.valueOf(Double.parseDouble(this.log.get(date).get(2)) +
              commision)); // adjusted commission
      my_stock.add(3, String.valueOf(price)); // price for same day is the same
      this.log.put(date, my_stock);
    }
  }

  /**
   * saves stock logs transaction information to file.
   *
   * @param portfolio name that the stock is saved in to look for file location.
   */
  void saveStocks(String portfolio) {
    Iterator log_iterator = this.log.keySet().iterator();
    String stock_information = "transaction date, cost, shares, commission, price\n";
    while (log_iterator.hasNext()) {
      String date = String.valueOf(log_iterator.next());
      List stock_details = this.log.get(date);

      stock_information += date + "," + stock_details.get(0) + "," + stock_details.get(1) + ","
              + stock_details.get(2) + "," + stock_details.get(3) + "\n";
    }
    String save_path = "portfolios/" + portfolio + "/" + this.getTicker() + ".csv";
    BufferedWriter write_stock = null;
    try {
        write_stock = new BufferedWriter(new FileWriter(save_path));
        write_stock.write(stock_information);
        write_stock.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Converts the transaction log information to string format.
   *
   * @return a string summary of the transaction log for the log.
   */
  String logString() {
    String log_info = this.ticker + "\n";
    Iterator log_iterator = this.log.keySet().iterator();

    while (log_iterator.hasNext()) {
      log_info += log_iterator.next().toString() + "\n";
    }
    return log_info;
  }

  /**
   * Retrieve the log of transactions of the stock.
   *
   * @return transaction log of stock.
   */
  Map<String, List<String>> getLogs() {
    return this.log;
  }
}
