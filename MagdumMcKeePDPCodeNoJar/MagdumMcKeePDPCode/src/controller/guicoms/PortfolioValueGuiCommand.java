package controller.guicoms;

import controller.API;
import controller.GuiCommand;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import model.PortfolioManager;
import view.GuiInterface;

/**
 * A GuiCommand to get the value of a portfolio on a certain date.
 */
public class PortfolioValueGuiCommand implements GuiCommand {

  @Override
  public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {
    String name = f.getPortfolioName();
    Object[] o = f.getOperationalStuff();
    String year = o[0].toString();
    String month = o[1].toString();
    String day = o[2].toString();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date upperLimit = new Date();
    upperLimit = new Date(upperLimit.getTime() - 1000L * 60 * 60 * 24);
    Date target = null;
    try {
      target = formatter.parse(year + "-" + month + "-" + day);
      if (!target.before(upperLimit)) {
        f.printLine("The date entered does not have information available. Please try again.");
        f.setCurrScreen("Error");
        return;
      }
    } catch (ParseException e) {
      f.printLine("The date entered is invalid. Please try again.");
      f.setCurrScreen("Error");
      return;
    }

    f.printLine("Please wait while API is loading the information...");
    String[] startTickers = p.getTickers(name);
    Float[] startCounts = p.getCounts(name);
    Date[] startDates = p.getDates(name);
    int j = 0;
    for (int i = 0; i < startDates.length; i++) {
      if (startDates[i].compareTo(target) < 1) {
        j++;
      }
    }
    String[] tickers = new String[j];
    Float[] counts = new Float[j];
    Date[] dates = new Date[j];

    int k = 0;
    int l = 0;
    while (k < j) {
      if (startDates[l].compareTo(target) < 1) {
        tickers[k] = startTickers[l];
        counts[k] = startCounts[l];
        dates[k] = startDates[l];
        k++;
      }
      l++;
    }

    float[] values;
    try {
      values = p.getPortfolioValue(name, target, api);
    } catch (IOException | ParseException e) {
      throw new RuntimeException(e);
    }
    Object[] out = new Object[tickers.length * 4 + 2];
    out[0] = formatter.format(target);
    float sum = 0;
    j = 0;
    for (int i = 1; i < out.length - 2; i += 4) {
      sum += values[j];
      out[i] = tickers[j];
      out[i + 1] = counts[j];
      out[i + 2] = formatter.format(dates[j]);
      out[i + 3] = values[j];
      j++;
    }
    out[out.length - 1] = String.format("%.02f", sum);
    f.setConStuff(out);
    f.setCurrScreen("Value Extracted");

  }
}
