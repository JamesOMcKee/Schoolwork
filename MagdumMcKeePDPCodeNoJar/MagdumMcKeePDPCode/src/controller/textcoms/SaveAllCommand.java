package controller.textcoms;

import controller.API;
import controller.TextCommand;
import java.util.Scanner;
import model.PortfolioManager;
import view.ViewInterface;

/**
 * A TextCommand to save all portfolios currently in the model.
 */
public class SaveAllCommand implements TextCommand {

  @Override
  public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {

    try {
      String[] names = p.getPortfolioNames();
      for (int i = 0; i < names.length; i++) {
        p.savePortfolio(names[i]);
      }
      v.printLine("All portfolios saved.");
    } catch (Exception e) {
      v.printLine("Unable to successfully save all portfolios.");
    }
    v.showSaveScreen();
  }
}
