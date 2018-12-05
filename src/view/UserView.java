package view;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

import controller.AppController2;
import model.Account;

public class UserView implements IUserView {
  private Account model = new Account();
  private AppController2 controllerObj = new AppController2(model, this);

  /**
   * empty constructor.
   */
  public UserView() {
    //empty constructor.
  }

  /**
   * method used to display all texts.
   *
   * @param st string.
   */

  public void display(String st) {
    System.out.println(st);
  }

  /**
   * method used to input the scanner input.
   *
   * @param scan scanner object.
   * @return string.
   * @throws IllegalStateException e
   */
  private String input(Scanner scan) throws IllegalStateException {
    String string = "";
    try {
      string = scan.next();
    } catch (NoSuchElementException e) {
      throw new IllegalStateException();
    }
    return string;
  }


  @Override
  public void run() {
    display("Welcome User to the Application\nHow to Invest for Dummies\n");
    String menu = "Please Enter the Following Options\n" +
            "Enter 1 to create a portfolio\n" +
            "Enter 2 to Buy Stocks\n" +
            "Enter 3 to Examine a portfolio\n" +
            "Enter Q to quit\n";

    while (true) {
      display(menu);
      Scanner sc = new Scanner(System.in);
      String command = input(sc);

      switch (command) {
        case "1":
          System.out.println("Enter Portfolio name");
          String s = input(sc);
          controllerObj.createPortfolio(s);
          break;

        case "2":
          buyStockOptions();
          break;


        case "3":
          System.out.println("Enter Portfolio name");
          String st = input(sc);
          controllerObj.examinePortfolio(st);
          break;


        case "q":
        case "Q":
          display("Exiting the program");
          System.exit(0);
          break;

        default:
          display("Please Enter valid response\n\n");
          break;
      }
    }
  }

  /**
   * provided submenu for buyStock.
   */
  private void buyStockOptions() {

    Scanner sc = new Scanner(System.in);
    display("Please Enter Commission amount");
    String com = input(sc);
    double commission = Double.parseDouble(com);

    System.out.println("There are sub options in this menu");
    String subMenu1 = "Enter 1 to buy single stocks\n" +
            "Enter 2 to buy multiple stocks\n" +
            "Enter 3 to buy stocks in periodic investment\n";
    display(subMenu1);

    String command = input(sc);

    switch (command) {
      case "1":
        System.out.println("Enter Stock's name");
        String stockName = input(sc);
        System.out.println("Enter number of shares");
        String s1 = input(sc);
        display("Enter the Date");
        String date = input(sc);
        int shares = 0;
        try {
          shares = Integer.parseInt(s1);
        } catch (NumberFormatException e) {
          System.out.println("Please Enter Number");

        }
        System.out.println("Enter Portfolio's name");
        String portfolioName = input(sc);
        controllerObj.buy(commission, stockName, shares, portfolioName, date);
        break;

      case "2":
        display("Displaying all portfolios to choose from:");
        display(controllerObj.printPF());
        display("Enter Portfolio name exactly as displayed above");
        String portfolio = input(sc);
        display("Enter Amount to invest");
        String amount = input(sc);
        display("Enter the Date");
        String date1 = input(sc);


        display("Enter the weights [separated by commas] || " +
                "Write DEFAULT to use default weights");

        String weights = input(sc);

        //finding the integer of number of stocks in that portfolio
        int i = controllerObj.getStockNumber(portfolio);
        int defaultWeight = 0;
        if (i > 0) {
          defaultWeight = 100 / i;
          // System.out.println("Assigned default weights");
        }


        int[] defaultArray = new int[i];
        for (int k = 0; k < defaultArray.length; k++) {
          defaultArray[k] = defaultWeight;
          //System.out.print("default array assigned");
        }

        int[] weightArray;
        if (weights.equals("DEFAULT")) {
          //System.out.println("Default if condition");
          weightArray = defaultArray;

        } else {
          weightArray = Arrays.stream(weights.split(","))
                  .mapToInt(Integer::parseInt).toArray();
        }

        if (weightArray.length != i) {
          display("Please Enter Correct Weights");
        }
        //System.out.print("Controller called");
        controllerObj.buyMultiple(commission, amount, portfolio, date1, weightArray);
        break;

      //Need to add method to enter more periodic investments
      case "3":
        periodicInvestment();
        break;

      default:
        display("Enter correct values");
        break;
    }

  }

  /**
   * provide submenu for periodic investment.
   */
  private void periodicInvestment() {
    display("Select your periodic Investment Strategy:\n");
    display("Enter 1 for Dollar Cost Averaging");
    Scanner sc = new Scanner(System.in);
    String command = input(sc);
    if (command.equals("1")) {
      display("Displaying all portfolios to choose from:");
      display(controllerObj.printPF());
      display("Enter Portfolio name");
      String portfolio = input(sc);

      display("Enter Commission");
      String com = input(sc);
      double commission = Double.parseDouble(com);

      display("Enter Investment Amount");
      String amount = input(sc);
      double investment = Double.parseDouble(amount);

      display("Enter Start Date");
      String sDate = input(sc);
      display("Enter End Date (Optional)");
      String eDate = input(sc);
      display("Enter interval in days");
      String interval = input(sc);
      int intervals = Integer.parseInt(interval);

      //finding the integer of number of stocks in that portfolio
      int i = controllerObj.getStockNumber(portfolio);
      int defaultWeight = 0;
      if (i < 0) {
        defaultWeight = 100 / i;
      }


      int[] defaultArray = new int[i];
      for (int k = 0; k < defaultArray.length; i++) {
        defaultArray[k] = defaultWeight;
      }

      display("Enter the weights [separated by commas] ||" +
              " Write DEFAULT to use default weights");

      String weights = input(sc);

      int[] weightArray;
      if (weights.equals("DEFAULT")) {
        weightArray = defaultArray;
      } else {
        weightArray = Arrays.stream(weights.split(","))
                .mapToInt(Integer::parseInt).toArray();
      }

      if (weightArray.length != i) {
        display("Please Enter Correct Weights");
      }

      controllerObj.periodicInvestment(commission, investment, portfolio,
              sDate, eDate, intervals, weightArray);

    }
  }


}
