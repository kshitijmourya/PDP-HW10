package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class represents an entire user account for the buying and selling of stocks. The Class
 * stores the information of these transactions in a hashmap to be called upon when needed.
 */
public class Account implements UserAccount {
  Map<String, LinkedList<Stock>> portfolios;

  /**
   * Constructor for user account. No parameters should be given.
   */
  public Account() {
    this.portfolios = new HashMap<String, LinkedList<Stock>>();
  }

  /**
   * Adds a portfolio to the users account. The initial portfolio is clear, with no stocks held
   * within it. To add stocks to the portfolio, the user must add them in one by one with the
   * buyStock() method.
   *
   * @param portfolioName string that will be used as the label for the portfolio. The user will use
   *                      this name to call the portfolio in other methods.
   */
  @Override
  public void addPortfolio(String portfolioName) throws IllegalArgumentException {
    if (this.portfolios.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Portfolio already exists.");
    }
    if (portfolioName.equals("")) {
      throw new IllegalArgumentException("Please name the portfolio");
    }
    this.portfolios.put(portfolioName, new LinkedList<Stock>());
  }

  /**
   * Removes a portfolio from a users account. All data in the portfolio will be lost. The user will
   * not have access to the stocks stored in this portfolio after removal. In the next version
   * update, we plan to include a feature to move stock from one portfolio to another and another
   * feature to automatically sell the stocks in the portfolio before removal. This will ensure the
   * user is able to keep track of running costs and profits even after removal of portfolio.
   *
   * @param portfolioName that will be removed from the useres account.
   */
  @Override
  public void removePortfolio(String portfolioName) throws IllegalArgumentException {
    if (!this.portfolios.containsKey(portfolioName)) {
      throw new IllegalArgumentException("Portfolio does not exist.");
    }
    if (portfolioName.equals("")) {
      throw new IllegalArgumentException("Please name the portfolio.");
    }
    this.portfolios.remove(portfolioName);
  }

  /**
   * Buys a particular stock and adds it to the specified portfolio at the users command. If the
   * stock does not already exist in the portfolio, it will add it. If the stock does exist in the
   * portfolio, then it will add the shares to the stock within the portfolio.
   *
   * @param commision amount charges in Dollard and Cents to make the transaction.
   * @param ticker    identifier for company to buy stock from. Can be company name or ticker
   *                  symbol.
   * @param date      the user wants to buy the stock in YYYY-MM-dd format.
   * @param type      of buy price the user wants to obtain shares at (i.e. open, close, low,
   *                  high).
   * @param shares    number of shares the user wants to buy.
   * @param portfolio to add the acquired stock to.
   */
  @Override
  public void buyStock(double commision, String ticker, String date, String type,
                       int shares, String portfolio) {
    boolean exists = false;

    Stock stock_bought = new Stock(commision, ticker, date, type, shares);
    Stock stock_owned;

    for (Stock s : this.portfolios.get(portfolio)) {
      if (s.getTicker().equals(ticker)) {
        stock_owned = s;

        int new_shares = stock_bought.getShares() + stock_owned.getShares();
        double running_cost = s.getCost() + stock_bought.getCost();
        s.setShares(new_shares);
        s.setCost(running_cost);
        exists = true;
      }
    }

    if (!exists) {
      this.portfolios.get(portfolio).add(stock_bought);
    }
  }

  /**
   * Buys a particular stock and adds it to the specified portfolio at the users command. If the
   * stock does not already exist in the portfolio, it will add it. If the stock does exist in the
   * portfolio, then it will add the shares to the stock within the portfolio.
   *
   * @param commision  amount charges in Dollard and Cents to make the transaction.
   * @param ticker     identifier for company to buy stock from. Can be company name or ticker
   *                   symbol.
   * @param date       the user wants to buy the stock in YYYY-MM-dd format.
   * @param type       of buy price the user wants to obtain shares at (i.e. open, close, low,
   *                   high).
   * @param investment number of shares the user wants to buy.
   * @param portfolio  to add the acquired stock to.
   */
  void buyMonetaryStock(double commision, String ticker, String date, String type,
                        double investment, String portfolio) {
    Stock stock_bought = new Stock(commision, ticker, date, type, investment);
    Stock stock_owned;

    for (Stock s : this.portfolios.get(portfolio)) {
      if (s.getTicker().equals(ticker)) {
        stock_owned = s;

        int new_shares = stock_bought.getShares() + stock_owned.getShares();
        double running_cost = s.getCost() + stock_bought.getCost();
        s.setShares(new_shares);
        s.setCost(running_cost);
      }
    }
  }

  /**
   * A future feature for the next version update. This method is incomplete and not ready for use
   * by the user. Sells a particular stock from a specified portfolio at the users command. The
   * stock MUST exist in the portfolio to be able to sell it. The user can only sell, at maximum,
   * the total number of shares owned.
   *
   * @param ticker    code for the company to sell the stock.
   * @param shares    number of shares to sell.
   * @param portfolio portfolio ehich contains the stock the user wants to sell.
   */
  @Override
  public void sellStock(String ticker, int shares, String portfolio) {
    boolean exists = false;

    for (Stock s : this.portfolios.get(portfolio)) {
      if (s.getTicker().equals(ticker)) {
        int remaining_shares = s.getShares() - shares;

        if (remaining_shares > 0) {
          s.setShares(remaining_shares);
        } else if (remaining_shares < 0) {
          System.out.println("Not enough shares owned to make this sale.");
        } else {
          this.portfolios.get(portfolio).remove(s);
        }

        exists = true;
      }
    }

    if (!exists) {
      System.out.println("This stock does not exist in this portfolio.");
    }
  }

  /**
   * Buys stocks of the specified portfolio in the weights that the user defines. Weights do not
   * have to add up to 100, but they will be proportioned accordingly. If the stock does not already
   * exist in the portfolio, it will add it. If the stock does exist in the portfolio, then it will
   * add the shares to the stock within the portfolio.
   *
   * @param commision  amount charges in Dollard and Cents to make the transaction.
   * @param investment amount in Dollars and Cents towards the portfolio.
   * @param portfolio  portfolio which contains the stock the user wants to sell.
   * @param date       the user wants to buy the stock in YYYY-MM-dd format.
   * @param weights    of investment into each stock in the portfolio.
   */
  @Override
  public void buyMultipleStockInPortfolio(double commision, double investment,
                                          String portfolio, String date, int... weights)
          throws InterruptedException {
    List weights_list = new ArrayList<Integer>();
    double weights_total = 0;

    for (int i : weights) {
      weights_total += i;
      weights_list.add(i);
    }

    ListIterator<Stock> stock_iterator = this.portfolios.get(portfolio).listIterator(0);
    ListIterator<Integer> weight_iterator = weights_list.listIterator(0);

    while (stock_iterator.hasNext() && weight_iterator.hasNext()) {
      double proportion = weight_iterator.next().doubleValue() / weights_total;
      Thread.sleep(10000);
      buyMonetaryStock(commision, stock_iterator.next().getTicker(), date, "open",
              investment * proportion, portfolio);


      buyMonetaryStock(commision, stock_iterator.next().getTicker(), date, "open",
              investment * proportion, portfolio);
    }
  }

  /**
   * Buys stocks of the specified portfolio in the weights that the user defines. Weights do not
   * have to add up to 100, but they will be proportioned accordingly. If the stock does not already
   * exist in the portfolio, it will add it. If the stock does exist in the portfolio, then it will
   * add the shares to the stock within the portfolio.
   *
   * @param commision  amount charges in Dollard and Cents to make the transaction.
   * @param investment amount in Dollars and Cents towards the portfolio.
   * @param portfolio  portfolio which contains the stock the user wants to sell.
   * @param start      date of periodic investment.
   * @param end        date of periodic investment.
   * @param interval   interval of periodic investment.
   * @param weights    of investment into each stock in the portfolio.
   */
  @Override
  public void periodicInvestment(double commision, double investment, String portfolio,
                                 String start, String end, int interval, int... weights)
          throws InterruptedException, ParseException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    LocalDate start_date = LocalDate.parse(start);
    LocalDate end_date = LocalDate.parse(end);

    long days = ChronoUnit.DAYS.between(start_date, end_date);
    int number_of_investments = Math.toIntExact(days / interval);

    for (int i = 0; i < number_of_investments; i++) {
      LocalDate date = start_date.plusDays(interval);
      buyMultipleStockInPortfolio(commision, investment, portfolio, date.format(formatter),
              weights);
    }
  }

  /**
   * Displays the total current information within the users account. Portfolio names, the stocks
   * within each portfolio, shares owned of each stock, total running cost of each stock, total
   * profit/loss from each stock.
   *
   * @return String paragraph of user account information.
   */
  @Override
  public String viewAccount() {
    if (this.portfolios.isEmpty()) {
      return "User has no active portfolios.";
    } else {
      String account_information = "";
      Set<String> keys = this.portfolios.keySet();
      int index = 0;
      for (String k : keys) {
        index++;
        account_information += viewPortfolio(k);
      }

      return account_information;
    }
  }

  /**
   * Displays the total current Portfolio names.
   *
   * @return String paragraph of user account information.
   */
  @Override
  public String checkPortfolioNames() {
    String portfolio_names = "Portfolio Names: \n";

    for (String p : this.portfolios.keySet()) {
      portfolio_names += p + "\n";
    }

    return portfolio_names;
  }

  /**
   * Displays the total current information within a specified portfolio. Portfolio name, the stocks
   * within the portfolio, shares owned of each stock, and total running cost of each stock.
   *
   * @param portfolio portfolio which contains the stock the user wants to view.
   * @return String paragraph of specified portfolio information.
   */
  @Override
  public String viewPortfolio(String portfolio) {
    String portfolio_information = "Portfolio: " + portfolio + "\n";
    for (Stock s : this.portfolios.get(portfolio)) {
      portfolio_information += s.toString();
    }

    return portfolio_information;
  }

  /**
   * Displays the logs of a specified stock in a portfolio.
   *
   * @param portfolio portfolio which contains the stock the user wants to view.
   * @return String paragraph of stock logs in specified portfolio.
   */
  @Override
  public String viewStockLogsInPortfolio(String portfolio) {
    LinkedList<Stock> stocks = this.portfolios.get(portfolio);

    return stocks.stream().map(a -> a.logString()).collect(Collectors.joining());
  }

  /**
   * Displays the total current profit of the account. Portfolio name, the stocks within the
   * portfolio, and profit for each stock.
   *
   * @param start date of profit calculations.
   * @param end   date of profit calculations.
   */
  @Override
  public String getAccountProfit(String start, String end) throws InterruptedException {
    if (this.portfolios.isEmpty()) {
      return "User has no active portfolios.";
    } else {

      String account_profit = "";
      Set<String> keys = this.portfolios.keySet();

      for (String k : keys) {
        account_profit += getPortfolioProfit(k, start, end);
      }

      return account_profit;
    }
  }

  /**
   * Displays the total current profit of a specified portfolio.
   *
   * @param portfolio portfolio which contains the stock the user wants to view.
   * @param start     date of profit calculations.
   * @param end       date of profit calculations.
   * @return String paragraph of profit from specified portfolio.
   */
  @Override
  public String getPortfolioProfit(String portfolio, String start, String end)
          throws InterruptedException {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date today = new Date();


    int total_value = 0;

    String portfolio_information = "Portfolio: " + portfolio + "\n";

    for (Stock s : this.portfolios.get(portfolio)) {
      APIData stock_data = new APIData();
      String code = stock_data.searchCode(s.getTicker());
      double end_price = stock_data.getPrices(code, end, "open");

      for (String date : s.getLogs().keySet()) {
        portfolio_information += "\n\t" + s.getTicker() + "\n";
        try {
          if (formatter.parse(date).after(formatter.parse(start))
                  && formatter.parse(date).before(formatter.parse(end))) {

            double value_difference = end_price * Integer.parseInt(s.getLogs().get(date).get(1))
                    - Double.parseDouble(s.getLogs().get(date).get(0));

            total_value += value_difference;
            portfolio_information += "\t\t" + "Current Profit: " + value_difference + "\n";
          }
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
    }
    portfolio_information += "Total Portfolio Earnings: " + total_value + "\n\n";
    return portfolio_information;
  }

  /**
   * Obtains the number of stocks within a given portfolio.
   *
   * @param portfolio portfolio which contains the stock the user wants to view.
   * @return number of stocks in a portfolio.
   */
  @Override
  public int getStockNumberInPortfolio(String portfolio) {
    return this.portfolios.get(portfolio).size();
  }
}
