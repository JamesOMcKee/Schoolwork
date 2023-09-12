package controller.guicoms;

import controller.API;
import controller.GuiCommand;
import model.PortfolioManager;
import view.GuiInterface;
import java.io.IOException;
import java.text.ParseException;

/**
 * A GuiCommand to build a flexible portfolio.
 */
public class BuildFlexibleCommandGui implements GuiCommand {

  @Override
  public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {

    try {
      String name = buildFlexPortfolio(f, p);
    } catch (IOException | ParseException e) {
      f.printLine("There was an error building the flexible portfolio. Please try again.");
      f.setCurrScreen("Error");
    }
  }

  private String buildFlexPortfolio(GuiInterface f, PortfolioManager p)
      throws IOException, ParseException {

    //get data from view
    String name = f.getPortfolioName();

    if (name.equals("")) {
      f.printLine("The entered name is not valid. Please try again.");
      f.setCurrScreen("Error");
      return name;
    }

    try {
      String[] existing = p.getPortfolioNames();
      for (int i = 0; i < existing.length; i++) {
        if (existing[i].equals(name)) {
          f.printLine("A portfolio with that name already exists. Please try again.");
          f.setCurrScreen("Error");
          return name;
        }
      }
    } catch (Exception e) {
      //there are no portfolios
    }

    boolean flag = true;
    for (int i = 0; i < name.length(); i++) {
      if (!Character.isLetterOrDigit(name.charAt(i)) && !Character.isWhitespace(name.charAt(i))) {
        flag = false;
      }
    }

    if (!flag) {
      f.printLine("The entered name is not valid. Please try again.");
      f.setCurrScreen("Error");
      return name;
    }

    p.portFlexBuilder(name);
    f.printLine("Portfolio name set successfully!");
    String getCurrScreen = f.getCurrScreen();
    switch (getCurrScreen) {
      case "Build Portfolio":
        f.setCurrScreen("Add Stock");
        break;
      case "Build Strategy":
        f.setCurrScreen("Add Strategy");
        break;
      default:
        break;
    }

    //System.out.println(name + " in build");
    return name;
  }

}
