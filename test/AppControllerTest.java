

import org.junit.Before;
import org.junit.Test;

import controller.AppController2;
import controller.IAppController;
import model.Account;
import model.UserAccount;
import view.IUserView;
import view.UserView;


public class AppControllerTest {

  /**
   * Initialization.
   */
  @Before
  public void setUp() {

    UserAccount testModel = new Account();
    IUserView testView = new UserView();
    IAppController testController = new AppController2();
  }


  @Test(expected = NullPointerException.class)
  public void test1() {
    IAppController testController = new AppController2();
    testController.createPortfolio("");
  }


}
