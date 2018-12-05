import model.Account;
import view.UserView;
import view.GUI;

import java.util.Scanner;
import java.io.InputStreamReader;

public class Main {
  /**
   * Main method which initiates model and view.
   *
   * @param args args.
   */
  public static void main(String[] args) {

    Account model = new Account();

    System.out.println("Console OR GUI??");
    InputStreamReader input = new InputStreamReader(System.in);
    Scanner scanner = new Scanner(input);
    String command = scanner.next();
    command = command.toLowerCase();

    switch (command) {
      case "gui": {
        GUI viewGUI = new GUI();
        viewGUI.run();
        break;
      }
      case "console": {
        UserView view = new UserView();
        view.run();
        break;
      }
      default:
        System.out.println("Command not recognized, please try again");
        break;
    }
    //AppController2 controller = new AppController2(model);

    //controller.runApp();

  }
}
