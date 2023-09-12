package controller.textcoms;

import controller.API;
import controller.TextCommand;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import model.PortfolioManager;
import view.ViewInterface;

/**
 * A TextCommand to see the value of a portfolio on a given date.
 */
public class PortfolioValueCommand implements TextCommand {

  @Override
  public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
    HelpingCommittee helper = new HelpingCommittee();
    String name;
    try {
      name = helper.selectPortfolio(v, sc, p);
    } catch (Exception e) {
      v.printLine("There are either no portfolios yet or the input was out of bounds.");
      sc.nextLine();
      v.showPortfolioScreen();
      return;
    }
    String year;
    String mon;
    String day;
    v.printLine("Please enter the year (4 digits):");
    year = sc.nextLine();
    v.printLine("Please enter the month (2 digits):");
    mon = sc.nextLine();
    v.printLine("Please enter the day (2 digits):");
    day = sc.nextLine();
    try {
      DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
      date.setLenient(false);
      Date target = date.parse(mon + "/" + day + "/" + year);
      Date upperLimit = new Date();
      if (target.compareTo(upperLimit) > 0) {
        v.printLine("The date entered is out of bounds.");
        v.showPortfolioScreen();
        return;
      }
    } catch (Exception e) {
      v.printLine("The date provided was not valid.");
      v.showPortfolioScreen();
      return;
    }
    try {
      v.printLine("Please wait while the API retrieves that information.");
      v.printLines(simpleValueHelper(name, year + "-" + mon + "-" + day, p, api));
    } catch (Exception e) {
      v.printLine("There was an error attempting to value the portfolio.");
    }
    v.printLine("Enter any key to return to the previous menu.");
    sc.nextLine();
    v.showPortfolioScreen();
  }

  private String[] simpleValueHelper(String name, String date, PortfolioManager p, API api)
      throws IOException, ParseException {
    try {
      String[] startTickers = p.getTickers(name);
      Float[] startCounts = p.getCounts(name);
      Date[] startDates = p.getDates(name);
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      int j = 0;
      for (int i = 0; i < startDates.length; i++) {
        if (startDates[i].compareTo(formatter.parse(date)) < 1) {
          j++;
        }
      }
      String[] tickers = new String[j];
      Float[] counts = new Float[j];
      Date[] dates = new Date[j];

      int k = 0;
      int l = 0;
      while (k < j) {
        if (startDates[l].compareTo(formatter.parse(date)) < 1) {
          tickers[k] = startTickers[l];
          counts[k] = startCounts[l];
          dates[k] = startDates[l];
          k++;
        }
        l++;
      }

      float[] values = p.getPortfolioValue(name, formatter.parse(date), api);
      String[] out = new String[tickers.length + 2];
      out[0] = "Value of Portfolio: " + name + " on " + date;
      float sum = 0;
      for (int i = 0; i < values.length; i++) {
        sum += values[i];
        out[i + 1] = "Ticker: " + tickers[i] + "; Count: " + String.format("%.02f", counts[i])
            + "; Value per: $" + String.format("%.02f", values[i])
            + "; Purchased: " + formatter.format(dates[i])
            + "; Total Value: $" + String.format("%.02f", values[i]);

      }
      out[tickers.length + 1] = "Total value of portfolio: $" + String.format("%.02f", sum);
      return out;
    } catch (Exception e) {
      DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      String[] tickers = p.getTickers(name);
      Float[] counts = p.getCounts(name);
      float[] values = p.getPortfolioValue(name, formatter.parse(date), api);
      String[] out = new String[tickers.length + 2];
      out[0] = "Value of Portfolio: " + name + " on " + date;
      float sum = 0;
      for (int i = 0; i < values.length; i++) {
        if (values[i] <= 0) {
          out[i + 1] = "No information found for symbol: " + tickers[i];
        } else {
          sum += values[i] * counts[i];
          out[i + 1] = "Ticker: " + tickers[i] + "; Count: " + String.format("%.02f", counts[i])
              + "; Value per: $" + String.format("%.02f", values[i])
              + "; Total Value: $" + String.format("%.02f", values[i] * counts[i]);
        }
      }
      out[tickers.length + 1] = "Total value of portfolio: $" + String.format("%.02f", sum);
      return out;
    }
  }
}
