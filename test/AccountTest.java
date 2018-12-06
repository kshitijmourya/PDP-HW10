import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import model.Account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class AccountTest {
  Account testTrade;

  @Test
  public void test1() throws InterruptedException, ParseException {
    testTrade = new Account();
    testTrade.addPortfolio("Technology");

    testTrade.buyStock(20.00, "AMZN", "2018-11-08",
            "open", 10, "Technology");
    assertEquals("Portfolio: Technology\n"
            + "\tTicker Symbol: AMZN\n"
            + "\tTotal Shares Owned: 10\n"
            + "\tTotal Running Cost of Stock: 17570.0\n\n", testTrade.viewPortfolio("Technology"));

    testTrade.buyStock(20.00, "amd", "2018-11-08",
            "open", 10, "Technology");
    testTrade.buyStock(20.00, "apple", "2018-11-13",
            "open", 20, "Technology");
    testTrade.buyStock(20.00, "microsoft", "2018-11-08",
            "open", 10, "Technology");

    String early_ports = testTrade.viewAccount();
    testTrade.addPortfolio("Retail");

    assertEquals("Portfolio: Technology\n" + "\n" + "\tAMZN\n"
                    + "\t\tCurrent Profit: -1810.1000000000004\n" + "\n" + "\tAMD\n"
                    + "\t\tCurrent Profit: -40.0\n" + "\n" + "\tAAPL\n"
                    + "\t\tCurrent Profit: -422.4000000000001\n" + "\n" + "\tMSFT\n"
                    + "\t\tCurrent Profit: -75.29999999999995\n" + "Total Portfolio Earnings: -2347\n\n",
            testTrade.getPortfolioProfit("Technology",
                    "2018-11-2", "2018-11-27"));

    String all_ports = testTrade.viewAccount();
    testTrade.removePortfolio("Retail");

    assertNotEquals(early_ports, all_ports);
    assertEquals(early_ports, testTrade.viewAccount());

    testTrade.buyMultipleStockInPortfolio(20.00, 2000.00,
            "Technology","2018-10-05", 20, 10, 20, 50);

    assertEquals("Portfolio: Technology\n" + "\tTicker Symbol: AMZN\n"
            + "\tTotal Shares Owned: 10\n" + "\tTotal Running Cost of Stock: 17610.0\n" + "\n"
            + "\tTicker Symbol: AMD\n" + "\tTotal Shares Owned: 24\n"
            + "\tTotal Running Cost of Stock: 670.6800000000001\n" + "\n"
            + "\tTicker Symbol: AAPL\n" + "\tTotal Shares Owned: 24\n"
            + "\tTotal Running Cost of Stock: 4804.44\n" + "\n" + "\tTicker Symbol: MSFT\n"
            + "\tTotal Shares Owned: 28\n"
            + "\tTotal Running Cost of Stock: 3205.34\n\n", testTrade.viewPortfolio("Technology"));
  }

  @Test
  public void test2() {
    testTrade = new Account();
    testTrade.loadAccount();

    String portfolio_info = "Portfolio: Technology\n " + "\tTicker Symbol: AAPL\n"
            + "\tTotal Shares Owned: 24\n" + "\tTotal Running Cost of Stock: 4804.44\n"
            + "\n" + "\tTicker Symbol: AMD\n" + "\tTotal Shares Owned: 24\n"
            + "\tTotal Running Cost of Stock: 670.6800000000001\n" + "\n"
            + "\tTicker Symbol: AMZN\n" + "\tTotal Shares Owned: 10\n"
            + "\tTotal Running Cost of Stock: 17610.0\n" + "\n" + "\tTicker Symbol: MSFT\n"
            + "\tTotal Shares Owned: 19\n" + "\tTotal Running Cost of Stock: 2171.67\n\n";
    assertEquals(portfolio_info, testTrade.viewPortfolio("Technology").trim());
  }

  @Test
  public void test3() throws InterruptedException, ParseException {
    testTrade = new Account();
    testTrade.addPortfolio("Technology");
    testTrade.buyStock(20.00, "AMZN", "2018-11-08",
            "open", 10, "Technology");
    testTrade.buyStock(20.00, "amd", "2018-11-08",
            "open", 10, "Technology");
    testTrade.buyStock(20.00, "apple", "2018-11-13",
            "open", 20, "Technology");
    testTrade.buyStock(20.00, "microsoft", "2018-11-08",
            "open", 10, "Technology");
    testTrade.periodicInvestment(20, 2000.00, "Technology",
            "2018-08-05", "2018-11-27", 20, 10, 20, 50);

    assertEquals("Portfolio: Technology\n" + "\tTicker Symbol: AMZN\n"
            + "\tTotal Shares Owned: 10\n" + "\tTotal Running Cost of Stock: 17770.0\n" + "\n"
            + "\tTicker Symbol: AMD\n" + "\tTotal Shares Owned: 210\n"
            + "\tTotal Running Cost of Stock: 5432.58\n" + "\n" + "\tTicker Symbol: AAPL\n"
            + "\tTotal Shares Owned: 78\n" + "\tTotal Running Cost of Stock: 16842.4\n"
            + "\n" + "\tTicker Symbol: MSFT\n" + "\tTotal Shares Owned: 10\n"
            + "\tTotal Running Cost of Stock: 1138.0\n\n", testTrade.viewPortfolio("Technology"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddRemove1() {
    testTrade = new Account();
    testTrade.addPortfolio("Technology");
    testTrade.buyStock(20.00, "", "2018-11-08",
            "open", 10, "Technology");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddRemove2() {
    testTrade = new Account();
    testTrade.addPortfolio("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddRemove3() {
    testTrade = new Account();
    testTrade.removePortfolio("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddRemove4() {
    testTrade = new Account();
    testTrade.addPortfolio("Farming");
    testTrade.addPortfolio("Farming");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddRemove5() {
    testTrade = new Account();
    testTrade.removePortfolio("Retirement");
  }
}