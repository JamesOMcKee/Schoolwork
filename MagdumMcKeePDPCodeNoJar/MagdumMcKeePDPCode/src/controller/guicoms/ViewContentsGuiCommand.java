package controller.guicoms;

import controller.API;
import controller.GuiCommand;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.PortfolioManager;
import view.GuiInterface;

/**
 * A GuiCommand to view the contents of a portfolio. Used by several other commands as well.
 */
public class ViewContentsGuiCommand implements GuiCommand {

  @Override
  public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {
    String name = f.getPortfolioName();
    String[] tickers = p.getTickers(name);
    Float[] counts = p.getCounts(name);
    Date[] dates = p.getDates(name);
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Object[] sendToView = new String[tickers.length * 3];

    int j = 0;
    for (int i = 0; i < sendToView.length; i += 3) {
      sendToView[i] = tickers[j];
      sendToView[i + 1] = String.format("%.02f", counts[j]);
      sendToView[i + 2] = formatter.format(dates[j]);
      j++;
    }

    f.setConStuff(sendToView);
    f.setCurrScreen("Show Contents");
  }
}
