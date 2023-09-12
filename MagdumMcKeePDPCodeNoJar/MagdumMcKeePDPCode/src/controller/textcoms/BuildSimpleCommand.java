package controller.textcoms;

import controller.API;
import controller.TextCommand;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import model.PortfolioManager;
import view.ViewInterface;

/**
 * A TextCommand to build a simple portfolio.
 */
public class BuildSimpleCommand implements TextCommand {

  @Override
  public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
    HelpingCommittee helper = new HelpingCommittee();
    try {
      String name = buildPortfolio(v, sc, p);
      try {
        v.printLines(helper.contentsHelper(name, p));
        v.printLine("Enter any key to return to the previous menu.");
        sc.nextLine();
      } catch (Exception e) {
        //do nothing
      }
    } catch (IOException e) {
      v.printLine("There was an error building the simple portfolio. Please try again.");
    }
    v.showBuildScreen();
  }

  private String buildPortfolio(ViewInterface v, Scanner sc, PortfolioManager p)
      throws IOException {
    String name;
    ArrayList<String> tickerList = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();

    v.printLine("Please enter the portfolio's name (alphanumeric).");
    name = sc.nextLine();

    try {
      String[] existing = p.getPortfolioNames();
      for (int i = 0; i < existing.length; i++) {
        if (existing[i].equals(name)) {
          v.printLine("A portfolio with that name already exists. Please try again.");
          return name;
        }
      }
    } catch (Exception e) {
      //there are no portfolios
    }

    boolean flag = true;
    for (int i = 0; i < name.length(); i++) {
      if (!Character.isLetterOrDigit(name.charAt(i)) && !Character.isWhitespace(name.charAt(i))) {
        System.out.println(name.charAt(i));
        flag = false;
      }
    }

    if (!flag) {
      v.printLine("The entered name is not valid. Please try again.");
      return name;
    }

    while (true) {
      String ticker;
      String count;
      v.printLine("Please enter a ticker symbol or enter 'Done'.");
      ticker = sc.next();
      sc.nextLine();

      if (ticker.equalsIgnoreCase("done")) {
        break;
      }

      if (!p.validateTicker(ticker)) {
        v.printLine("Warning: the symbol you entered is not recognized.");
        v.printLine("Enter 'y' to continue with this symbol. Enter anything else to try again.");
        String response = sc.next();
        sc.nextLine();
        if (!response.equals("y")) {
          continue;
        }
      }

      v.printLine("Please enter the stock count.");
      count = sc.next();
      sc.nextLine();
      try {
        int temp = Integer.parseInt(count);
        if (temp <= 0) {
          v.printLine("The count entered is not a positive integer above 0. Please try again.");
          continue;
        }
      } catch (Exception e) {
        v.printLine("The count entered was not an integer. Please try again.");
        continue;
      }

      tickerList.add(ticker);
      floatList.add(Float.parseFloat(count));
    }

    p.portBuilder(tickerList, floatList, name);
    return name;
  }
}
