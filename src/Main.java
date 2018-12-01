import model.Account;
import view.UserView;

public class Main {
  /**
   * Main method which initiates model and view.
   *
   * @param args args.
   */
  public static void main(String[] args) {

    Account model = new Account();
    UserView view = new UserView();

    //AppController2 controller = new AppController2(model);

    //controller.runApp();
    view.run();
  }
}
