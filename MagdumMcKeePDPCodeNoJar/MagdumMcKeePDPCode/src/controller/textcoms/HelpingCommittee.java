package controller.textcoms;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import model.PortfolioManager;
import view.ViewInterface;

/**
 * A class storing multiple protected helper methods that get reused by TextCommands.
 */
public class HelpingCommittee {

  protected String selectPortfolio(ViewInterface v, Scanner sc, PortfolioManager p) {
    String[] portNames = p.getPortfolioNames();
    String[] numbered = new String[portNames.length];

    for (int i = 0; i < portNames.length; i++) {
      numbered[i] = (i + 1) + ". " + portNames[i];
    }

    v.printLines(numbered);
    v.printLine("Please choose one of the following options:");
    int index = sc.nextInt();
    sc.nextLine();
    return portNames[index - 1];
  }

  protected String selectFlexPortfolio(ViewInterface v, Scanner sc, PortfolioManager p) {
    String[] portNames = p.getFlexPortfolioNames();
    String[] numbered = new String[portNames.length];

    for (int i = 0; i < portNames.length; i++) {
      numbered[i] = (i + 1) + ". " + portNames[i];
    }

    v.printLines(numbered);
    v.printLine("Please choose one of the following options:");
    int index = sc.nextInt();
    sc.nextLine();

    return portNames[index - 1];
  }

  protected String[] contentsStrategyHelper(ArrayList<String> tickers, float[] counts,
      float amount) {

    String[] out = new String[tickers.size() + 1];

    out[0] = "Contents of Strategy:";
    for (int i = 0; i < tickers.size(); i++) {
      out[i + 1] = "Ticker: " + tickers.get(i)
          + "; Share: " + String.format("%.0f", counts[i]) + "%";
    }
    return out;
  }

  protected String[] contentsHelper(String name, PortfolioManager p) {

    try {
      String[] tickers = p.getTickers(name);
      Float[] counts = p.getCounts(name);
      Date[] dates = p.getDates(name);

      String[] out = new String[tickers.length + 1];

      out[0] = "Contents of Flexible Portfolio: " + name;
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      for (int i = 0; i < tickers.length; i++) {
        if (counts[i] > 0) {
          out[i + 1] = "BUY "
              + "; Ticker: " + tickers[i]
              + "; Count: " + String.format("%.02f", counts[i])
              + "; Date: " + formatter.format(dates[i]);
        }
        if (counts[i] < 0) {
          out[i + 1] = "SELL"
              + "; Ticker: " + tickers[i]
              + "; Count: " + String.format("%.02f", Math.abs(counts[i]))
              + "; Date: " + formatter.format(dates[i]);
        }
      }
      return out;

    } catch (Exception e) {
      String[] tickers = p.getTickers(name);
      Float[] counts = p.getCounts(name);

      String[] out = new String[tickers.length + 1];

      out[0] = "Contents of Simple Portfolio: " + name;

      for (int i = 0; i < tickers.length; i++) {
        out[i + 1] = "Ticker: " + tickers[i]
            + "; Count: " + String.format("%.02f", counts[i]);
      }
      return out;
    }
  }
}
