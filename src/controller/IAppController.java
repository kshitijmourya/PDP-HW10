package controller;

public interface IAppController {

  /**
   * go method for the controller which interacts with the model.
   */
  public void runApp();

  /**
   * calls model to create portfolio.
   *
   * @param portfolioName gets name from view.
   */
  public void createPortfolio(String portfolioName);

  /**
   * Calls model to buy single stock.
   *
   * @param commission commission value.
   * @param stockName  name of the stock.
   * @param shares     number of shares.
   * @param portfolio  name of the portfolio.
   */
  public void buy(double commission, String stockName, int shares, String portfolio, String date);

  /**
   * Calls model to buy multiple stocks in a portfolio.
   *
   * @param commission    commision value.
   * @param amount        investment amount.
   * @param portfolioName name of portfolio.
   * @param date          date of transaction.
   * @param weightArray   weights of the stocks.
   */
  public void buyMultiple(Double commission, String amount, String portfolioName,
                          String date, int[] weightArray);

  /**
   * calls model to buy periodic Investment.
   *
   * @param commission    commission value.
   * @param investment    investment amount.
   * @param portfolioName name of the portfolio.
   * @param sDate         start date.
   * @param edate         end date.
   * @param intervals     intervals in days.
   * @param weightArray   weights of the stocks.
   */
  public void periodicInvestment(double commission, double investment, String portfolioName,
                                 String sDate, String edate, int intervals, int[] weightArray);

  /**
   * calls models to display portfolio information.
   *
   * @param portfolioName name of the portfolio.
   */

  public void examinePortfolio(String portfolioName);

  /**
   * calls model to print portfolio.
   *
   * @return portfolio string.
   */
  public String printPF();

  /**
   * calls models to number of stocks in a portfolio.
   *
   * @param portfolioName portfolio name.
   * @return number os stocks.
   */
  public int getStockNumber(String portfolioName);

}
