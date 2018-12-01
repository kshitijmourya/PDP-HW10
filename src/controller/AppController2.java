package controller;

import java.text.ParseException;


import model.Account;
import view.UserView;

public class AppController2 implements IAppController {

  private Account model;
  private UserView view;

  /**
   * constructor for controller.
   *
   * @param model model object.
   * @param view  view object.
   */
  public AppController2(Account model, UserView view) {

    this.model = model;
    this.view = view;
  }

  /**
   * empty constructor.
   */
  public AppController2() {
    //empty constructor
  }


  /**
   * go method for the controller which interacts with the model.
   */
  @Override
  public void runApp() {
    view.run();

  }

  /**
   * calls model to create portfolio.
   *
   * @param portfolioName gets name from view.
   */
  public void createPortfolio(String portfolioName) {
    //System.out.println(this.model);
    try {
      model.addPortfolio(portfolioName);
      view.display("Created a portfolio successfully.\n");
    } catch (IllegalArgumentException e) {
      view.display(e.getMessage());
    } catch (NullPointerException e) {
      view.display("Please Enter Value");
    }
  }

  /**
   * Calls model to buy single stock.
   *
   * @param commission commission value.
   * @param stockName  name of the stock.
   * @param shares     number of shares.
   * @param portfolio  name of the portfolio.
   **/

  public void buy(double commission, String stockName, int shares, String portfolio, String date) {
    try {
      //model.addPortfolio(portfolio);
      model.buyStock(commission, stockName, date, "open", shares, portfolio);
      view.display("Bought Stock successfully\n");
    } catch (IllegalArgumentException e) {
      view.display(e.getMessage());
    }
  }

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
                          String date, int[] weightArray) {

    Double investment = Double.parseDouble(amount);

    //int[] weightArray = Arrays.stream(weights.split(",")).mapToInt(Integer::parseInt).toArray();

    try {
      // System.out.println("HEre in the controller");
      model.buyMultipleStockInPortfolio(commission, investment, portfolioName, date, weightArray);
      view.display("Bought Stock Succesfully\n");
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

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
                                 String sDate, String edate, int intervals, int[] weightArray) {
    //int[] weightArray = Arrays.stream(weights.split(",")).mapToInt(Integer::parseInt).toArray();

    try {
      model.periodicInvestment(commission, investment, portfolioName, sDate, edate,
              intervals, weightArray);
      view.display("Investment Strategy succesfully created");
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }

  }

  /**
   * calls models to display portfolio information.
   *
   * @param portfolioName name of the portfolio.
   */
  public void examinePortfolio(String portfolioName) {
    view.display(model.viewAccount());
  }

  /**
   * calls model to print portfolio.
   *
   * @return portfolio string.
   */
  public String printPF() {
    return model.checkPortfolioNames();
  }


  /**
   * calls models to number of stocks in a portfolio.
   *
   * @param portfolioName portfolio name.
   * @return number os stocks.
   */

  public int getStockNumber(String portfolioName) {
    return model.getStockNumberInPortfolio(portfolioName);
  }

}
