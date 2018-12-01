package model;

import java.text.ParseException;

/**
 * This interface represents an account for transing and selling stock.
 */
public interface UserAccount {

  /**
   * Adds a portfolio to the users account. The initial portfolio is clear, with no stocks held
   * within it. To add stocks to the portfolio, the user must add them in one by one with the
   * buyStock() method.
   *
   * @param portfolioName string that will be used as the label for the portfolio. The user will use
   *                      this name to call the portfolio in other methods.
   */
  void addPortfolio(String portfolioName);

  /**
   * Removes a portfolio from a users account. All data in the portfolio will be lost. The user will
   * not have access to the stocks stored in this portfolio after removal. In the next version
   * update, we plan to include a feature to move stock from one portfolio to another and another
   * feature to automatically sell the stocks in the portfolio before removal. This will ensure the
   * user is able to keep track of running costs and profits even after removal of portfolio.
   *
   * @param portfolioName that will be removed from the useres account.
   */
  void removePortfolio(String portfolioName);

  /**
   * Buys a particular stock and adds it to the specified portfolio at the users command. If the
   * stock does not already exist in the portfolio, it will add it. If the stock does exist in the
   * portfolio, then it will add the shares to the stock within the portfolio.
   *
   * @param ticker    identifier for company to buy stock from. Can be company name or ticker
   *                  symbol.
   * @param date      the user wants to buy the stock in YYYY-MM-dd format.
   * @param type      of buy price the user wants to obtain shares at (i.e. open, close, low,
   *                  high).
   * @param shares    number of shares the user wants to buy.
   * @param portfolio to add the acquired stock to.
   */
  void buyStock(double commision, String ticker, String date, String type,
                int shares, String portfolio);

  /**
   * A future feature for the next version update. This method is incomplete and not ready for use
   * by the user.
   * <p></p>
   * Sells a particular stock from a specified portfolio at the users command. The stock MUST exist
   * in the portfolio to be able to sell it. The user can only sell, at maximum, the total number of
   * shares owned.
   *
   * @param ticker    code for the company to sell the stock.
   * @param shares    number of shares to sell.
   * @param portfolio portfolio which contains the stock the user wants to sell.
   */
  void sellStock(String ticker, int shares, String portfolio);

  /**
   * Buys stocks of the specified portfolio in the weights that the user defines. Weights do not
   * have to add up to 100, but they will be proportioned accordingly. If the stock does not already
   * exist in the portfolio, it will add it. If the stock does exist in the portfolio, then it will
   * add the shares to the stock within the portfolio.
   *
   * @param investment amount in Dollars and Cents towards the portfolio.
   * @param portfolio  portfolio which contains the stock the user wants to sell.
   * @param date       the user wants to buy the stock in YYYY-MM-dd format.
   * @param weights    of investment into each stock in the portfolio.
   */
  void buyMultipleStockInPortfolio(double commision, double investment, String portfolio,
                                   String date, int... weights) throws InterruptedException;

  /**
   * Buys stocks of the specified portfolio in the weights that the user defines. Weights do not
   * have to add up to 100, but they will be proportioned accordingly. If the stock does not already
   * exist in the portfolio, it will add it. If the stock does exist in the portfolio, then it will
   * add the shares to the stock within the portfolio.
   *
   * @param commision  commision value.
   * @param investment amount in Dollars and Cents towards the portfolio.
   * @param portfolio  portfolio which contains the stock the user wants to sell.
   * @param start      date of periodic investment.
   * @param end        date of periodic investment.
   * @param interval   interval of periodic investment.
   * @param weights    of investment into each stock in the portfolio.
   */
  void periodicInvestment(double commision, double investment, String portfolio,
                          String start, String end, int interval, int... weights)
          throws InterruptedException, ParseException;

  /**
   * Displays the total current information within the users account. Portfolio names, the stocks
   * within each portfolio, shares owned of each stock, total running cost of each stock.
   *
   * @return String paragraph of user account information.
   */
  String viewAccount();

  /**
   * Displays the total current Portfolio names.
   *
   * @return String paragraph of user account information.
   */
  String checkPortfolioNames();

  /**
   * Displays the total current information within a specified portfolio. Portfolio name, the stocks
   * within the portfolio, shares owned of each stock, and total running cost of each stock.
   *
   * @param portfolio portfolio which contains the stock the user wants to view.
   * @return String paragraph of specified portfolio information.
   */
  String viewPortfolio(String portfolio);

  /**
   * Displays the logs of a specified stock in a portfolio.
   *
   * @param portfolio portfolio which contains the stock the user wants to view.
   * @return String paragraph of stock logs in specified portfolio.
   */
  String viewStockLogsInPortfolio(String portfolio);

  /**
   * Displays the total current profit of the account. Portfolio name, the stocks within the
   * portfolio, and profit for each stock.
   *
   * @param start date of profit calculations.
   * @param end   date of profit calculations.
   */
  String getAccountProfit(String start, String end) throws InterruptedException;

  /**
   * Displays the total current profit of a specified portfolio.
   *
   * @param portfolio portfolio which contains the stock the user wants to view.
   * @param start     date of profit calculations.
   * @param end       date of profit calculations.
   * @return String paragraph of profit from specified portfolio.
   */
  String getPortfolioProfit(String portfolio, String start, String end) throws InterruptedException;

  /**
   * Obtains the number of stocks within a given portfolio.
   *
   * @param portfolio portfolio which contains the stock the user wants to view.
   * @return number of stocks in a portfolio.
   */
  int getStockNumberInPortfolio(String portfolio);
}
