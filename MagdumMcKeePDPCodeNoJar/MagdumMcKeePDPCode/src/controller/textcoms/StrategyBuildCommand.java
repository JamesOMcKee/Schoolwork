package controller.textcoms;

import controller.API;
import controller.TextCommand;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import model.PortfolioManager;
import model.Stock;
import view.ViewInterface;

/**
 * A TextCommand to build a portfolio with a strategy.
 */
public class StrategyBuildCommand implements TextCommand {

  @Override
  public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
    try {
      buildFlexPortfolio(v, sc, p, api);
    } catch (IOException | ParseException e) {
      v.printLine("There was an error building the flexible portfolio. Please try again.");
    }
    v.showBuildScreen();
  }

  private String buildFlexPortfolio(ViewInterface v, Scanner sc, PortfolioManager p, API api)
      throws IOException, ParseException {
    String name;

    v.printLine("Please enter the portfolio's name (alphanumeric).");
    name = sc.nextLine();

    try {
      String[] existing = p.getPortfolioNames();
      for (int i = 0; i < existing.length; i++) {
        if (existing[i].equals(name)) {
          v.printLine("A portfolio with that name already exists. Please try again.");
          return name;
        }
      }
    } catch (Exception e) {
      //there are no portfolios
    }

    boolean flag = true;
    if (name.length() < 1) {
      flag = false;
    }
    for (int i = 0; i < name.length(); i++) {
      if (!Character.isLetterOrDigit(name.charAt(i)) && !Character.isWhitespace(name.charAt(i))) {
        flag = false;
      }
    }

    if (!flag) {
      v.printLine("The entered name is not valid. Please try again.");
      return name;
    }

    p.portFlexBuilder(name);
    editStrategy(name, v, sc, p, api);
    return name;
  }

  private void editStrategy(String name, ViewInterface v, Scanner sc, PortfolioManager p, API api)
      throws IllegalArgumentException, IOException, ParseException {
    HelpingCommittee helper = new HelpingCommittee();
    boolean amountFlag = true;
    boolean frequencyFlag = true;
    boolean startDateFlag = true;
    boolean endDateFlag = true;
    float amount = 1;
    int frequency = 1;
    ArrayList<String> tickers = new ArrayList<>();
    ArrayList<Stock<String, Float>> list = new ArrayList<>();

    while (amountFlag) {
      v.printLine("Please enter a positive, non-zero dollar amount to buy each period (xx.yy).");
      try {
        amount = Float.parseFloat(sc.nextLine());
        if (amount < 1) {
          throw new IllegalArgumentException();
        }
        amountFlag = false;
      } catch (Exception e) {
        v.printLine("Please enter a valid dollar amount.");
      }
    }

    while (frequencyFlag) {
      v.printLine("Please enter a positive, integer number of days between buys.");
      try {
        frequency = Integer.parseInt(sc.nextLine());
        if (frequency < 1 || frequency > 1825) {
          throw new IllegalArgumentException();
        }
        frequencyFlag = false;
      } catch (Exception e) {
        v.printLine("Please enter a valid interval between 1 day and 5 years.");
      }
    }

    Date upperLimit = new Date();
    upperLimit = new Date(upperLimit.getTime() - (1000L * 60 * 60 * 24)); //yesterday
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    formatter.setLenient(false);
    Date target1 = null;
    Date target2 = null;

    while (startDateFlag) {
      v.printLine("Please enter the starting year (4 digits):");
      String year = sc.nextLine();
      v.printLine("Please enter the starting month (2 digits):");
      String mon = sc.nextLine();
      v.printLine("Please enter the starting day (2 digits):");
      String day = sc.nextLine();
      try {
        target1 = formatter.parse(year + "-" + mon + "-" + day);
        if (target1.compareTo(upperLimit) > 0) {
          v.printLine("The date entered must be before today.");
          continue;
        }
        startDateFlag = false;
      } catch (Exception e) {
        v.printLine("The date provided was not valid.");
      }
    }

    while (endDateFlag) {
      v.printLine("Please enter the ending year(4 digits) "
          + "or enter 'done' if the strategy will be ongoing:");
      String year = sc.nextLine();
      if (year.equalsIgnoreCase("done")) {
        target2 = formatter.parse("2100-01-01");
        break;
      }
      v.printLine("Please enter the ending month (2 digits):");
      String mon = sc.nextLine();
      v.printLine("Please enter the ending day (2 digits):");
      String day = sc.nextLine();
      try {
        target2 = formatter.parse(year + "-" + mon + "-" + day);
        if (target2.compareTo(target1) < 1) {
          v.printLine("The date entered is out of bounds.");
          continue;
        }
        endDateFlag = false;
      } catch (Exception e) {
        v.printLine("The date provided was not valid.");
      }
    }

    long interval = target2.getTime() - target1.getTime();
    long minimum = 1000L * 60 * 60 * 24;
    if (interval < minimum) {
      v.printLine("Please enter two dates, chronologically and at least 1 day apart.");
      v.showBuildScreen();
      return;
    }

    while (true) {
      String ticker;

      v.printLine("Please enter a ticker symbol or enter 'done' to finish.");
      ticker = sc.next();
      sc.nextLine();
      if (ticker.equalsIgnoreCase("done")) {
        break;
      }
      if (!p.validateTicker(ticker)) {
        v.printLine("The symbol you entered is not recognized and "
            + "is incompatible with constructing a portfolio in this way.");
        continue;
      }
      if (!p.validateTicker(ticker, target1)) {
        v.printLine("This stock was not available by the given starting date.");
        continue;
      }
      if (tickers.contains(ticker)) {
        v.printLine("You have already added that stock to the list.");
        continue;
      }
      tickers.add(ticker);
      v.printLine("The following stocks are in the strategy:");
      for (String tick : tickers) {
        v.printLine(tick);
      }
    }

    if (tickers.size() < 1) {
      v.printLine("This is an empty portfolio and cannot have a strategy added.");
      v.showBuildScreen();
      return;
    }

    float[] percentages = new float[tickers.size()];
    float sum = 0;
    int i = 0;

    v.printLine("Here are the tickers present in the portfolio: ");
    for (String tick : tickers) {
      v.printLine(tick);
    }
    v.printLine("Next, please enter a set of values that add to 100.");
    while (i < tickers.size()) {
      v.printLine("There is currently room for " + String.format("%.02f", 100 - sum) + "% "
          + "among " + (tickers.size() - i) + " tickers left.");
      v.printLine("Please select an apportioning (40.5% as '40.5') for the following ticker: "
          + tickers.get(i));

      String percent = sc.nextLine();
      try {
        float per = Float.parseFloat(percent);
        percentages[i] = per;
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

    v.printLines(helper.contentsStrategyHelper(tickers, percentages, amount));
    v.printLine("Enter any key to return to continue.");
    sc.nextLine();

    for (int j = 0; j < tickers.size(); j++) {
      list.add(new Stock<String, Float>(tickers.get(j), amount * percentages[j] * 0.01f));
    }
    p.addStrategy(name, list, target1, target2, frequency);
    v.printLine("Please wait while the system interacts with the API.");
    p.updateFromStrategy(name, api);

    v.printLines(helper.contentsHelper(name, p));
    v.printLine("Enter any key to return to the previous menu.");
    sc.nextLine();
  }
}
