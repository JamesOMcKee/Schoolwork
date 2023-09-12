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
 * A TextCommand to edit a flexible portfolio.
 */
public class EditFlexibleCommand implements TextCommand {

  @Override
  public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
    HelpingCommittee helper = new HelpingCommittee();
    String name = null;
    try {
      name = helper.selectFlexPortfolio(v, sc, p);
    } catch (Exception e) {
      v.printLine(
          "There are either no flexible portfolios yet or the input was out of bounds.");
      sc.nextLine();
      v.showBuildScreen();
      return;
    }
    try {
      v.printLines(helper.contentsHelper(name, p));
      editFlexPortfolio(name, v, sc, p);
    } catch (Exception e) {
      v.printLine("There was difficulty editing the portfolio. Please try again.");
      v.showBuildScreen();
      return;
    }
    v.printLines(helper.contentsHelper(name, p));
    v.printLine("Enter any key to return to the previous menu.");
    sc.nextLine();
    v.showBuildScreen();
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
