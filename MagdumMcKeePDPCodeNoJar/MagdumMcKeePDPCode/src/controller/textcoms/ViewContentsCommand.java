package controller.textcoms;

import controller.API;
import controller.TextCommand;
import java.util.Scanner;
import model.PortfolioManager;
import view.ViewInterface;

/**
 * A TextCommand to view the contents of a portfolio.
 */
public class ViewContentsCommand implements TextCommand {

  @Override
  public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
    HelpingCommittee helper = new HelpingCommittee();
    try {
      String name = helper.selectPortfolio(v, sc, p);
      v.printLines(helper.contentsHelper(name, p));
      v.printLine("Enter any key to return to the previous menu.");
      sc.nextLine();
      v.showPortfolioScreen();
    } catch (Exception e) {
      v.printLine("There are either no portfolios yet or the input was out of bounds.");
      sc.nextLine();
      v.showPortfolioScreen();
    }
  }
}
