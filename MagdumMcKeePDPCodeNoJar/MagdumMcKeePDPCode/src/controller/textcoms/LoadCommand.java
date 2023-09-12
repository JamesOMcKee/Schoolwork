package controller.textcoms;

import controller.API;
import controller.TextCommand;
import java.util.Scanner;
import model.PortfolioManager;
import view.ViewInterface;

/**
 * A TextCommand to load in a file from somewhere.
 */
public class LoadCommand implements TextCommand {

  @Override
  public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
    HelpingCommittee helper = new HelpingCommittee();
    v.printLine("Please enter the filename.");
    String name = sc.nextLine();

    try {
      boolean problem = false;
      String[] existing = p.getPortfolioNames();
      for (int i = 0; i < existing.length; i++) {
        if (existing[i].equals(name.substring(0, name.length() - 4))) {
          v.printLine("A portfolio with that name already exists. Please try again.");
          problem = true;
        }
      }
      if (problem) {
        v.showLoadScreen();
        return;
      }
    } catch (Exception e) {
      //there are no portfolios
    }

    try {
      p.readPortfolioFile(name);
      name = name.substring(0, name.length() - 4);
      v.printLines(helper.contentsHelper(name, p));
      v.printLine("Enter any key to return to the previous menu.");
      sc.nextLine();
    } catch (Exception e) {
      v.printLine("The file was either not found, or not in the right format.");
    }
    v.showLoadScreen();
  }
}
