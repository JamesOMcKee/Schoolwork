package controller.textcoms;

import controller.API;
import controller.TextCommand;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import model.PortfolioManager;
import view.ViewInterface;

/**
 * A TextCommand to execute a Dollar Cost Average buy on a certain date.
 */
public class DollarCostBuyCommand implements TextCommand {

  @Override
  public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
    HelpingCommittee helper = new HelpingCommittee();
    String name;
    try {
      name = helper.selectFlexPortfolio(v, sc, p);
    } catch (Exception e) {
      v.printLine("There are either no portfolios yet or the input was out of bounds.");
      sc.nextLine();
      v.showBuildScreen();
      return;
    }

    float value;
    v.printLine("Please enter the positive dollar amount you wish to invest ('xx.yy')");
    String totalValue = sc.nextLine();
    try {
      value = Float.parseFloat(totalValue);
      if (value <= 0) {
        throw new IllegalArgumentException();
      }
    } catch (Exception e) {
      v.printLine("There was an error trying to read the dollar amount. Please try again.");
      v.showBuildScreen();
      return;
    }

    String year;
    String mon;
    String day;
    v.printLine("Please enter the year (4 digits):");
    year = sc.next();
    sc.nextLine();
    v.printLine("Please enter the month (2 digits):");
    mon = sc.next();
    sc.nextLine();
    v.printLine("Please enter the day (2 digits):");
    day = sc.next();
    sc.nextLine();
    Date target;
    try {
      DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
      date.setLenient(false);
      target = date.parse(year + "-" + mon + "-" + day);
      Date upperLimit = new Date();
      upperLimit = new Date(upperLimit.getTime() - (1000L * 60 * 60 * 24));
      Date lowerLimit = date.parse("2000-01-01");
      if (target.compareTo(upperLimit) > -1 || target.before(lowerLimit)) {
        v.printLine("The date entered is out of bounds (2000-01-01 to yesterday). "
            + "Please try again.");
        v.showBuildScreen();
        return;
      }
    } catch (Exception e) {
      v.printLine("The date provided was not valid. Please try again.");
      v.showBuildScreen();
      return;
    }

    String[] allTickers = p.getTickers(name); //get unique tickers
    ArrayList<String> tempTickers = new ArrayList<>();
    for (int i = 0; i < allTickers.length; i++) {
      if (!tempTickers.contains(allTickers[i])) {
        tempTickers.add(allTickers[i]);
      }
    }

    String[] tickers = new String[tempTickers.size()];
    for (int i = 0; i < tempTickers.size(); i++) {
      tickers[i] = tempTickers.get(i);
    }

    try {
      for (int i = 0; i < tickers.length; i++) {
        boolean valid = p.validateTicker(tickers[i], target);
        if (!valid) {
          v.printLine("There is a stock in this portfolio which is not available to buy on the"
              + " given date.\nPlease choose a date on which all stocks in the portfolio are "
              + "available.");
          v.showBuildScreen();
          return;
        }
      }
    } catch (Exception e) {
      v.printLine("There was an issue reading the ticker validation file. Please check it is "
          + "in the correct location and try again.");
      v.showBuildScreen();
      return;
    }

    float[] percentages = new float[tickers.length];
    float sum = 0;
    int i = 0;

    v.printLine("Here are the tickers present in the portfolio: ");
    v.printLines(tickers);
    v.printLine("Next, please enter a set of values that add to 100.");
    while (i < tickers.length) {
      v.printLine("There is currently room for " + String.format("%.02f", 100 - sum) + "%");
      v.printLine("Please select an apportioning (40.5% as '40.5') for the following ticker: "
          + tickers[i]);

      String percent = sc.nextLine();
      try {
        float per = Float.parseFloat(percent);
        percentages[i] = per;
        if (per < 0) {
          throw new IllegalArgumentException();
        }
        sum += per;

        if (sum - 100 > 0.1) {
          sum -= per;
          throw new IllegalArgumentException();
        }

      } catch (Exception e) {
        v.printLine("Please be sure to enter a number less than or equal to what remains.");
        continue;
      }
      i++;
    }

    if (Math.abs(100 - sum) > 0.1) {
      v.printLine("The given apportioning does not add up to 100%. Please try again.");
      v.showBuildScreen();
      return;
    }

    float[] prices;
    try {
      prices = api.getPricesAfter(tickers, target);
    } catch (Exception e) {
      v.printLine("There was an error during the API call. Please try again.");
      v.showBuildScreen();
      return;
    }
    for (int k = 0; k < prices.length; k++) {
      if (prices[k] == 0) {
        v.printLine("There was an error retrieving prices. Please try again.");
        v.showBuildScreen();
        return;
      }
    }

    for (int j = 0; j < tickers.length; j++) {
      float countBuy = (value * percentages[j] * 0.01f) / prices[j];
      p.editFlexPortfolio(name, tickers[j], countBuy, target);
    }

    v.printLines(helper.contentsHelper(name, p));
    v.printLine("Enter any key to return to the previous menu.");
    sc.nextLine();
    v.showBuildScreen();
  }
}
