package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
  private Map<String, List<Stock>> portfolios;
  private Map<String, List<String>> strategies;

  /**
   * Constructor for user account. No parameters should be given.
   */
  public Account() {
    this.portfolios = new HashMap<String, List<Stock>>();
    this.strategies = new HashMap<String, List<String>>();
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
    this.portfolios.put(portfolioName, new ArrayList<Stock>());
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
   * portfolio, then it will add the shares to the stock within the portfolio. Automatically saves
   * the state of the account after buying the stock.
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
    this.saveAccount();

    boolean exists = false;

    for (Stock s : this.portfolios.get(portfolio)) {
      if (s.getTicker().equals(ticker)) {
        s.sharesTransaction(commision, date, type, shares);
        exists = true;
      }
    }

    if (!exists) {
      this.portfolios.get(portfolio).add(new Stock(commision, ticker, date, type, shares));
    }

  }

  /**
   * Buys a particular stock and adds it to the specified portfolio at the users command. If the
   * stock does not already exist in the portfolio, it will add it. If the stock does exist in the
   * portfolio, then it will add the shares to the stock within the portfolio. Automatically saves
   * the state of the account after buying the stock.
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
  private void buyMonetaryStock(double commision, String ticker, String date, String type,
                                double investment, String portfolio) {
    this.saveAccount();

    boolean exists = false;

    for (Stock s : this.portfolios.get(portfolio)) {
      if (s.getTicker().equals(ticker)) {
        s.monetaryTransaction(commision, date, type, investment);

        exists = true;
      }
    }

    if (!exists) {
      this.portfolios.get(portfolio).add(new Stock(commision, ticker, date, type, investment));
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
    List<Stock> clone_portfolio = new ArrayList();
    clone_portfolio.addAll(this.portfolios.get(portfolio));

    ListIterator<Stock> stock_iterator = clone_portfolio.listIterator();
    ListIterator<Integer> weight_iterator = weights_list.listIterator(0);

    while (stock_iterator.hasNext() && weight_iterator.hasNext()) {
      Integer w = weight_iterator.next();
      Stock s = stock_iterator.next();
      double proportion = w.doubleValue() / weights_total;
      buyMonetaryStock(commision, s.getTicker(), date, "open",
              investment * proportion, portfolio);

      proportion = w.doubleValue() / weights_total;
      buyMonetaryStock(commision, s.getTicker(), date,
              "open", investment * proportion, portfolio);
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

    saveStrategy(commision, investment, portfolio, start, end, interval, weights);

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    LocalDate start_date = LocalDate.parse(start);
    LocalDate end_date = LocalDate.parse(end);
    LocalDate buy_date = start_date;
    long days = ChronoUnit.DAYS.between(start_date, end_date);
    int number_of_investments = Math.toIntExact(days / interval);

    for (int i = 0; i < number_of_investments; i++) {
      buyMultipleStockInPortfolio(commision, investment, portfolio, buy_date.format(formatter),
              weights);
      buy_date = buy_date.plusDays(interval);

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
   * Displays the total Portfolio names in the account currently.
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
    List<Stock> stocks = this.portfolios.get(portfolio);

    return stocks.stream().map(a -> a.logString()).collect(Collectors.joining());
  }

  /**
   * Displays the total current profit of the account. Portfolio name, the stocks within the
   * portfolio, and profit for each stock.
   *
   * @param start date of profit calculations.
   * @param end   date of profit calculations.
   * @return cumulative profit of the account from one particular date to another..
   */
  @Override
  public String getAccountProfit(String start, String end)
          throws InterruptedException, ParseException {
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
          throws InterruptedException, ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date today = new Date();


    int total_value = 0;

    String portfolio_information = "Portfolio: " + portfolio + "\n";
    for (Stock s : this.portfolios.get(portfolio)) {
      APIData stock_data = new APIData();
      String code = stock_data.searchCode(s.getTicker());
      double end_price = 0;
      end_price = stock_data.getPrices(code, end, "open");
      double value_difference = 0.00;

      for (String date : s.getLogs().keySet()) {
        portfolio_information += "\n\t" + s.getTicker() + "\n";

        if (formatter.parse(date).after(formatter.parse(start))
                && formatter.parse(date).before(formatter.parse(end))) {

          value_difference += end_price * Integer.parseInt(s.getLogs().get(date).get(1))
                  - Double.parseDouble(s.getLogs().get(date).get(0));

          portfolio_information += "\t\t" + "Current Profit: " + value_difference + "\n";
        }
      }
      total_value += value_difference;
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

  /**
   * Saves the current state of the account portfolios, and all stocks within it.
   */
  private void saveAccount() {
    Iterator portfolio_iterator = this.portfolios.keySet().iterator();

    while (portfolio_iterator.hasNext()) {
      String portfolio_name = String.valueOf(portfolio_iterator.next());

      for (Stock s : this.portfolios.get(portfolio_name)) {
        Path save_path = Paths.get("portfolios/" + portfolio_name);
        if (Files.exists(save_path)) {
          s.saveStocks(portfolio_name);
        } else {
          try {
            Files.createDirectory(save_path);
            s.saveStocks(portfolio_name);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  /**
   * Clears the current account information. Loads info from the most recently saved account.
   */
  public void loadAccount() {
    this.portfolios.clear();
    this.strategies.clear();

    File portfolios = new File("portfolios");

    File[] portfolio_dirs = portfolios.listFiles();
    for (File f : portfolio_dirs) {
      if (f.isDirectory()) {
        getSaveFile(f.toString());
      }
    }
  }

  /**
   * Saves all stocks and information in current specified portfolio.
   *
   * @param portfolio to be saved.
   */
  private void getSaveFile(String portfolio)
          throws IllegalArgumentException {

    String tickr = "";
    BufferedReader in = null;
    StringBuilder output = new StringBuilder();
    List<Stock> stocks = new ArrayList();

    File portfolios = new File(portfolio);
    File[] portfolio_files = portfolios.listFiles();
    for (File f : portfolio_files) {
      if (f.isFile()) {
        tickr = f.toString().split("\\W")[2];

        try {
          in = new BufferedReader(new FileReader(f.toString()));
          Stock s = new Stock(tickr);
          String b;

          while ((b = in.readLine()) != null) {
            String[] line_array = b.split(",");

            if (!line_array[0].equals("transaction date")) {
              s.updateLog(line_array[0], Double.parseDouble(line_array[3]),
                      Double.parseDouble(line_array[4]), Integer.parseInt(line_array[2]));
            }
          }

          stocks.add(s);

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    System.out.println(portfolio.split("\\W")[1]);
    this.portfolios.put(portfolio.split("\\W")[1], stocks);
  }

  /**
   * Save an investment strategy to file for later use.
   *
   * @param commision  amount charges in Dollard and Cents to make the transaction.
   * @param investment amount in Dollars and Cents towards the portfolio.
   * @param portfolio  portfolio which contains the stock the user wants to sell.
   * @param start      date of periodic investment.
   * @param end        date of periodic investment.
   * @param interval   interval of periodic investment.
   * @param weights    of investment into each stock in the portfolio.
   */
  private void saveStrategy(double commision, double investment, String portfolio,
                            String start, String end, int interval, int... weights) {
    String save_path = "strategies/" + portfolio + ".csv";

    String strategy =
            "commission,investment amount,portfolio,start date,end date,interval,weights\n";

    strategy += commision + "," + investment + "," + portfolio + ","
            + start + "," + end + "," + interval
            + ",";

    for (int w : weights) {
      strategy += w + "|";
    }

    BufferedWriter write_strategy = null;

    try {
      write_strategy = new BufferedWriter(new FileWriter(save_path));
      write_strategy.write(strategy);
      write_strategy.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Retrieve saved investment strategy from file.
   *
   * @param portfolio strategy to be loaded.
   */
  void getSaveStrategy(String portfolio) {

    BufferedReader in = null;
    StringBuilder output = new StringBuilder();
    List<String> strategy = new ArrayList();

    File portfolios = new File(portfolio);
    File[] strategy_files = portfolios.listFiles();

    for (File f : strategy_files) {
      if (f.toString().split("\\.")[0].equals(portfolio)) {

        try {
          in = new BufferedReader(new FileReader(f.toString()));
          String b;

          while ((b = in.readLine()) != null) {
            String[] line_array = b.split(",");

            if (!line_array[0].equals("commission")) {
              for (String s : line_array) {
                strategy.add(s);
              }
            }
          }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    this.strategies.put(portfolio, strategy);
  }
}
