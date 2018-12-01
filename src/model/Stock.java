package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
  private Map<String, List<String>> log = new HashMap<>();

  /**
   * Constructor for a single stock object. It will hold the total dealings with a particular
   * company in shares, cost of stocks purchased, and the ticker symbol for the company.
   *
   * @param companyName or ticker symbol that can be used to look the company up.
   * @param date        that the stock is going to be bought.
   * @param type        of price the shares will be bought at (i.e. open, close, high, low)
   * @param shares      to be boughtfor the company.
   */
  Stock(double commision, String companyName, String date, String type, int shares) {

    APIData stock_data = new APIData();
    String code = stock_data.searchCode(companyName);
    double price = 0;
    try {
      price = stock_data.getPrices(code, date, type);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    this.ticker = code;
    List cost_shares = new ArrayList();

    if (!this.log.containsKey(date)) {
      cost_shares.add(0, String.valueOf(commision + price * shares));
      cost_shares.add(1, String.valueOf(shares));
      cost_shares.add(2, String.valueOf(commision));
      cost_shares.add(3, String.valueOf(price));
      this.log.put(date, cost_shares);
      this.shares = shares;

    } else {
      double cost = Double.parseDouble(this.log.get(date).get(0)) + price * shares;
      cost_shares.add(0, String.valueOf(commision + cost));
      cost_shares.add(1, String.valueOf(Integer.parseInt(this.log.get(date).get(1)) +
              shares));
      cost_shares.add(2, String.valueOf(Double.parseDouble(this.log.get(date).get(2)) +
              commision));
      cost_shares.add(3, String.valueOf(price));
      this.shares += shares;
      this.log.put(date, cost_shares);
    }

    this.cost = this.log.values().stream().mapToDouble(a -> Double.parseDouble(a.get(0))).sum();
  }

  Stock(double commision, String companyName, String date, String type, double investment) {

    APIData stock_data = new APIData();
    String code = stock_data.searchCode(companyName);
    double price = 0;
    try {
      price = stock_data.getPrices(code, date, type);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    this.shares = Math.toIntExact(Math.round(investment / price));
    this.ticker = code;
    List cost_shares = new ArrayList();

    if (!this.log.containsKey(date)) {
      cost_shares.add(0, String.valueOf(commision + price * shares));
      cost_shares.add(1, String.valueOf(shares));
      cost_shares.add(2, String.valueOf(commision));
      cost_shares.add(3, String.valueOf(price));
      this.log.put(date, cost_shares);
      this.shares = shares;

    } else {
      double cost = Double.parseDouble(this.log.get(date).get(0)) + price * shares;
      cost_shares.add(0, String.valueOf(commision + cost));
      cost_shares.add(1, String.valueOf(Integer.parseInt(this.log.get(date).get(1)) +
              shares));
      cost_shares.add(2, String.valueOf(Double.parseDouble(this.log.get(date).get(2)) +
              commision));
      cost_shares.add(3, String.valueOf(price));
      this.shares += shares;
      this.log.put(date, cost_shares);
    }

    this.cost = this.log.values().stream().mapToDouble(a -> Double.parseDouble(a.get(0))).sum();
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
    return this.shares;
  }

  /**
   * Set the number of shares owned in this stock.
   *
   * @param shares to be added to the current stock object.
   */
  void setShares(int shares) {
    this.shares = shares;
  }

  /**
   * Get the total running cost of all the shares in this stock.
   *
   * @return costs of buying shares of this stock as a double.
   */
  double getCost() {
    return this.cost;
  }

  /**
   * Set the total running cost of buying shares of this stock.
   *
   * @param cost to be added to the total running cost of this stock.
   */
  void setCost(double cost) {
    this.cost = cost;
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
                    + "\n" + "\t" + "Total Shares Owned: " + this.shares
                    + "\n" + "\t" + "Total Running Cost of Stock: " + this.cost + "\n\n";

    return stock_information;
  }

  String logString() {
    String log_info = this.ticker + "\n";
    SortedMap sorted_log = new TreeMap(this.log);
    //this.log.keySet().;
    Iterator log_iterator = sorted_log.entrySet().iterator();

    while (log_iterator.hasNext()) {
      //log_iterator.next()
      log_info += log_iterator.next().toString() + "\n";
    }
    return log_info;
  }

  Map<String, List<String>> getLogs() {
    return this.log;
  }
}
