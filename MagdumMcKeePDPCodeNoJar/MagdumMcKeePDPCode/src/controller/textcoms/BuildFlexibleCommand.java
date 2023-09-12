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
import view.ViewInterface;

/**
 * A TextCommand to build a new flexible portfolio.
 */
public class BuildFlexibleCommand implements TextCommand {

  @Override
  public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
    HelpingCommittee helper = new HelpingCommittee();
    try {
      String name = buildFlexPortfolio(v, sc, p);
      try {
        v.printLines(helper.contentsHelper(name, p));
        v.printLine("Enter any key to return to the previous menu.");
        sc.nextLine();
      } catch (Exception e) {
        //do nothing
      }
    } catch (IOException | ParseException e) {
      v.printLine("There was an error building the flexible portfolio. Please try again.");
    }
    v.showBuildScreen();
  }

  private String buildFlexPortfolio(ViewInterface v, Scanner sc, PortfolioManager p)
      throws IOException, ParseException {
    String name;
    ArrayList<String> tickerList = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    ArrayList<Date> dateList = new ArrayList<>();

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
    editFlexPortfolio(name, v, sc, p);
    return name;
  }

  private void editFlexPortfolio(String name, ViewInterface v, Scanner sc, PortfolioManager p)
      throws IllegalArgumentException, IOException, ParseException {
    while (true) {
      String ticker;
      String count;

      v.printLine("Please choose whether to buy or sell, by entering 'b' or 's'. Alternatively, "
          + "or enter 'Done' to finish.");
      String bs = sc.next();
      sc.nextLine();
      if (bs.equalsIgnoreCase("done")) {
        break;
      }

      if (!bs.equalsIgnoreCase("b") && !bs.equalsIgnoreCase("s")) {
        v.printLine("Please be sure to enter 'b' or 's'.");
        continue;
      }

      v.printLine("Please enter a ticker symbol");
      ticker = sc.next();
      sc.nextLine();
      boolean dateCheck = true;
      if (!p.validateTicker(ticker)) {
        v.printLine("Warning: the symbol you entered is not recognized.");
        v.printLine("Enter 'y' to continue with this symbol. Enter anything else to try again.");
        String response = sc.next();
        sc.nextLine();
        if (!response.equals("y")) {
          continue;
        } else {
          dateCheck = false;
        }
      }

      v.printLine("Please enter the stock count.");
      count = sc.next();
      sc.nextLine();
      try {
        int temp = Integer.parseInt(count);
        if (temp <= 0) {
          v.printLine("The count entered is not a positive integer above 0. Please try again.");
          continue;
        }
      } catch (Exception e) {
        v.printLine("The count entered was not an integer. Please try again.");
        continue;
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
        if (target.compareTo(upperLimit) > 0) {
          v.printLine("The date entered is out of bounds.");
          continue;
        }
      } catch (Exception e) {
        v.printLine("The date provided was not valid.");
        continue;
      }

      if (dateCheck) {
        if (!p.validateTicker(ticker, target)) {
          v.printLine("You cannot buy a stock before it is available. Please try again.");
          continue;
        }
      }

      if (bs.equalsIgnoreCase("s")) {
        boolean valid = p.checkFlexEdit(name, ticker, Float.parseFloat(count), target);
        if (!valid) {
          v.printLine("This sale would invalidate the existing portfolio.");
          continue;
        }
      }

      if (bs.equalsIgnoreCase("b")) {
        p.editFlexPortfolio(name, ticker, Float.parseFloat(count), target);
      } else {
        p.editFlexPortfolio(name, ticker, -1 * Float.parseFloat(count), target);
      }
    }
  }
}
