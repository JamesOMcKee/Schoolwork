package controller.guicoms;

import controller.API;
import controller.GuiCommand;
import model.PortfolioManager;
import view.GuiInterface;

import java.util.ArrayList;

/**
 * A GuiCommand to determine the right action on the build edit screen.
 */
public class BuildEditCommandGui implements GuiCommand {

  @Override
  public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {
    Object[] o = f.getOperationalStuff();
    switch (o[0].toString()) {
      case "Begin building a flexible portfolio":
        f.setCurrScreen("Build Portfolio");
        break;
      case "Begin a flexible portfolio with a strategy":
        f.setCurrScreen("Build Strategy");
        break;
      case "Edit a flexible portfolio":
        try {
          selectFlexPortfolio(f, p);
          f.setCurrScreen("Edit Portfolio");
        } catch (Exception e) {
          f.printLine("There are either no flexible portfolios "
              + "yet or the input was out of bounds.");
          f.setCurrScreen("Error");
        }
        break;
      case "Add a strategy to a flexible portfolio":
        try {
          selectNonEmptyFlexPortfolio(f, p);
          f.setCurrScreen("Edit Strategy");
        } catch (Exception e) {
          f.printLine("There are either no flexible portfolios "
              + "yet or the input was out of bounds.");
          f.setCurrScreen("Error");
        }
        break;
      case "Add a fixed cost buy across a flexible portfolio":
        try {
          selectNonEmptyFlexPortfolio(f, p);
          f.setCurrScreen("Dollar Cost");
        } catch (Exception e) {
          f.printLine("There are either no flexible portfolios "
              + "yet or the input was out of bounds.");
          f.setCurrScreen("Error");
        }
        break;
      default:
        f.printLine("Please select one option");
        f.setCurrScreen("Error");
        break;
    }
  }

  private void selectFlexPortfolio(GuiInterface f, PortfolioManager p) {
    String[] portNames = p.getFlexPortfolioNames();
    f.setConStuff(portNames);
  }

  private void selectNonEmptyFlexPortfolio(GuiInterface f, PortfolioManager p) {
    String[] portNames = p.getFlexPortfolioNames();
    ArrayList<String> finalList = new ArrayList<>();
    for (String name : portNames) {
      if (p.getTickers(name).length > 0) {
        finalList.add(name);
      }
    }
    String[] finalNames = new String[finalList.size()];
    for (int i = 0; i < finalNames.length; i++) {
      finalNames[i] = finalList.get(i);
    }
    f.setConStuff(finalNames);
  }
}
