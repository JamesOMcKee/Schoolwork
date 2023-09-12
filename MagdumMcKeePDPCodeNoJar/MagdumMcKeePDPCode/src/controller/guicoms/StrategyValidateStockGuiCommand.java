package controller.guicoms;

import controller.API;
import controller.GuiCommand;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.PortfolioManager;
import view.GuiInterface;

/**
 * A GuiCommand to validate stocks as they are entered for a strategy.
 */
public class StrategyValidateStockGuiCommand implements GuiCommand {

  @Override
  public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {
    Object[] o = f.getOperationalStuff();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date start = null;
    String year = o[2].toString();
    String month = o[3].toString();
    String day = o[4].toString();
    String ticker = o[o.length - 2].toString();
    String count = o[o.length - 1].toString();
    try {
      start = formatter.parse(year + "-" + month + "-" + day);
    } catch (Exception e) {
      f.printLine("This is a bug an should never be possible. "
          + "The date should have previously been validated.");
      f.setCurrScreen("Error");
      return;
    }
    try {
      if (!p.validateTicker(ticker, start)) {
        throw new RuntimeException();
      }
    } catch (Exception e) {
      f.printLine("That ticker was either blank or not recognized or was not available by the given"
          + " starting date.");
      f.setCurrScreen("Error");
      return;
    }

    try {
      float percent = Float.parseFloat(count);
      if (percent < 0 || percent > 100) {
        throw new RuntimeException();
      }
    } catch (Exception e) {
      f.printLine("The entered count was blank or less than 0 or greater than 100.");
      f.setCurrScreen("Error");
      return;
    }
    f.setCurrScreen("Validated");
  }
}
