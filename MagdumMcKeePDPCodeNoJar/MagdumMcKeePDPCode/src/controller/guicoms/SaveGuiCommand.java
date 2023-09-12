package controller.guicoms;

import controller.API;
import controller.GuiCommand;
import java.io.IOException;
import model.PortfolioManager;
import view.GuiInterface;

/**
 * A GuiCommand to save whatever portfolio is relevant to the GUI.
 */
public class SaveGuiCommand implements GuiCommand {

  @Override
  public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {
    String name = f.getPortfolioName();
    try {
      p.savePortfolio(name);
      f.printLine("Portfolio saved.");
    } catch (IOException e) {
      f.printLine("Saving failed.");
    }
  }
}
