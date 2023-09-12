package controller.guicoms;

import controller.API;
import controller.GuiCommand;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.PortfolioManager;
import view.GuiInterface;

/**
 * A GuiCommand to validate the starting information for a dollar cost buy.
 */
public class DollarCostStartCommand implements GuiCommand {

  @Override
  public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {
    Object[] o = f.getOperationalStuff();
    String amountString = o[0].toString();
    String year1 = o[1].toString();
    String month1 = o[2].toString();
    String day1 = o[3].toString();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date startingDate = new Date();
    float amount;

    try {
      amount = Float.parseFloat(amountString);
      if (amount < 1) {
        throw new IllegalArgumentException();
      }
    } catch (Exception e) {
      f.printLine("The amount entered was blank or, "
          + "not a dollar amount greater than or equal to $1.");
      f.setCurrScreen("Error");
      return;
    }

    try {
      startingDate = formatter.parse(year1 + "-" + month1 + "-" + day1);
    } catch (Exception e) {
      f.printLine("The starting date entered was blank or invalid.");
      f.setCurrScreen("Error");
      return;
    }

    Date upperLimit = new Date((new Date()).getTime() - 1000L * 60 * 60 * 24);
    Date lowerLimit;
    try {
      lowerLimit = formatter.parse("2000-01-01");
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    if (startingDate.before(lowerLimit) || startingDate.after(upperLimit)) {
      f.printLine("The date given was out of range. Please select a date between "
          + "2000-01-01 and yesterday.");
      f.setCurrScreen("Error");
      return;
    }

    String name = f.getPortfolioName();
    String[] tickers = p.getTickers(name);
    ArrayList<String> uniques = new ArrayList<>();
    for (String ticker : tickers) {
      if (!uniques.contains(ticker)) {
        uniques.add(ticker);
      }
    }
    String[] uniqueTickers = new String[uniques.size()];
    for (int i = 0; i < uniques.size(); i++) {
      uniqueTickers[i] = uniques.get(i);
      try {
        if (!p.validateTicker(uniqueTickers[i], startingDate)) {
          f.printLine("There is a stock in this portfolio which was not available on the "
              + "chosen date. Please choose a new date and try again.");
          f.setCurrScreen("Error");
          return;
        }
      } catch (Exception e) {
        f.printLine("There was an error reading the ticker list. Please try again.");
        f.setCurrScreen("Error");
        return;
      }
    }

    f.setConStuff(uniqueTickers);
    f.setCurrScreen("Proceed Dollar Cost");
  }
}
