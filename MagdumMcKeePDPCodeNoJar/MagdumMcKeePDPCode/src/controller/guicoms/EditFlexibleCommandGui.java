package controller.guicoms;

import controller.API;
import controller.GuiCommand;
import model.PortfolioManager;
import view.GuiInterface;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A GuiCommand to edit a flexible portfolio.
 */
public class EditFlexibleCommandGui implements GuiCommand {

  @Override
  public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {
    String name = null;
    try {
      name = f.getPortfolioName();
    } catch (Exception e) {
      f.printLine("There are either no flexible portfolios yet or the input was out of bounds.");
      f.setCurrScreen("Error");
      return;
    }
    try {
      editFlexPortfolio(name, f, p);
    } catch (Exception e) {
      f.printLine("There was difficulty editing the portfolio. Please try again.");
      f.setCurrScreen("Error");
      return;
    }
  }

  private void editFlexPortfolio(String name, GuiInterface f, PortfolioManager p)
      throws IllegalArgumentException, IOException, ParseException {

    //get data from view
    Object[] o = f.getOperationalStuff();

    String ticker;
    String count;

    String bs = o[0].toString();

    ticker = o[1].toString();
    if (ticker.equals("")) {
      f.printLine("Please enter a ticker.");
      return;
    }
    boolean dateCheck = true;
    if (!p.validateTicker(ticker)) {
      String response = f.printWarning("Warning: the symbol you entered is not recognized. "
          + "Click 'YES' to continue with this symbol. Else click 'No'");
      if (!response.equals("y")) {
        return;
      } else {
        dateCheck = false;
      }
    }

    count = o[2].toString();
    if (count.equals("")) {
      f.printLine("Please enter a stock count.");
      return;
    }
    try {
      int temp = Integer.parseInt(count);
      if (temp <= 0) {
        f.printLine("The count entered is not a positive integer above 0. Please try again.");
        return;
      }
    } catch (Exception e) {
      f.printLine("The count entered was not an integer. Please try again.");
      return;
    }

    String year = o[3].toString();
    String mon = o[4].toString();
    String day = o[5].toString();
    Date target = null;
    try {
      DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
      date.setLenient(false);
      target = date.parse(year + "-" + mon + "-" + day);
      Date upperLimit = new Date();
      if (target.compareTo(upperLimit) > 0) {
        f.printLine("The date entered is out of bounds.");
        return;
      }
    } catch (Exception e) {
      f.printLine("The date provided was not valid.");
      return;
    }

    if (dateCheck) {
      if (!p.validateTicker(ticker, target)) {
        f.printLine("You cannot buy a stock before it is available. Please try again.");
        return;
      }
    }

    if (bs.equalsIgnoreCase("Sell Stock")) {
      boolean valid = p.checkFlexEdit(name, ticker, Float.parseFloat(count), target);
      if (!valid) {
        f.printLine("This sale would invalidate the existing portfolio.");
        return;
      }
    }

    if (bs.equalsIgnoreCase("Buy Stock")) {
      p.editFlexPortfolio(name, ticker, Float.parseFloat(count), target);
      f.printLine("Stock purchase details updated successfully!");
    } else {
      p.editFlexPortfolio(name, ticker, -1 * Float.parseFloat(count), target);
      f.printLine("Stock sale details updated successfully!");
    }

  }
}
