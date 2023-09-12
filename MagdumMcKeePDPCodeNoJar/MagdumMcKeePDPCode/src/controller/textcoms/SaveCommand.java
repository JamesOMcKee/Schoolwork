package controller.textcoms;

import controller.API;
import controller.TextCommand;
import java.io.IOException;
import java.util.Scanner;
import model.PortfolioManager;
import view.ViewInterface;

/**
 * A TextCommand to save a chosen portfolio.
 */
public class SaveCommand implements TextCommand {

  @Override
  public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
    HelpingCommittee helper = new HelpingCommittee();
    String name;
    try {
      name = helper.selectPortfolio(v, sc, p);
    } catch (Exception e) {
      v.printLine(
          "There are either no flexible portfolios yet or the input was out of bounds.");
      sc.nextLine();
      v.showSaveScreen();
      return;
    }
    try {
      p.savePortfolio(name);
      v.printLine("Portfolio saved.");
    } catch (IOException e) {
      v.printLine("Saving failed.");
    }
    v.showSaveScreen();
  }
}
