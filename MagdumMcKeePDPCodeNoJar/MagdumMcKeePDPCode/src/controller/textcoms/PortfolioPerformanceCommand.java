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
 * A TextCommand to view a portfolio's performance in a given timeframe.
 */
public class PortfolioPerformanceCommand implements TextCommand {

  @Override
  public void goDoStuff(Scanner sc, ViewInterface v, PortfolioManager p, API api) {
    HelpingCommittee helper = new HelpingCommittee();
    String name;
    try {
      name = helper.selectFlexPortfolio(v, sc, p);
    } catch (Exception e) {
      v.printLine(
          "There are either no flexible portfolios yet or the input was out of bounds.");
      sc.nextLine();
      v.showPortfolioScreen();
      return;
    }
    Date upperLimit = new Date();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    formatter.setLenient(false);
    Date lowerLimit;

    v.printLine(
        "Note that the date range includes the first date entered up to but not including"
            + " the second date entered");
    v.printLine("Please enter the starting year (4 digits):");
    String year = sc.nextLine();
    v.printLine("Please enter the starting month (2 digits):");
    String mon = sc.nextLine();
    v.printLine("Please enter the starting day (2 digits):");
    String day = sc.nextLine();
    Date target1;
    try {
      lowerLimit = formatter.parse("2000-01-01");
      target1 = formatter.parse(year + "-" + mon + "-" + day);
      if (target1.compareTo(upperLimit) > 0 || target1.compareTo(lowerLimit) < 0) {
        v.printLine("The date entered is out of bounds.");
        v.showPortfolioScreen();
        return;
      }
    } catch (Exception e) {
      v.printLine("The date provided was not valid.");
      v.showPortfolioScreen();
      return;
    }

    v.printLine("Please enter the ending year(4 digits):");
    year = sc.nextLine();
    v.printLine("Please enter the ending month (2 digits):");
    mon = sc.nextLine();
    v.printLine("Please enter the ending day (2 digits):");
    day = sc.nextLine();
    Date target2;
    try {
      target2 = formatter.parse(year + "-" + mon + "-" + day);
      if (target2.compareTo(upperLimit) > 0 || target1.compareTo(lowerLimit) < 0) {
        v.printLine("The date entered is out of bounds.");
        v.showPortfolioScreen();
        return;
      }
    } catch (Exception e) {
      v.printLine("The date provided was not valid.");
      v.showPortfolioScreen();
      return;
    }

    long interval = target2.getTime() - target1.getTime();
    long minimum = 1000L * 60 * 60 * 24;
    long maximum = 1000L * 60 * 60 * 24 * 365 * 20;
    if (interval < minimum || interval > maximum) {
      v.printLine("Please enter two dates, chronologically and at least 1 day apart,"
          + "but no more than 20 years apart.");
      v.showPortfolioScreen();
      return;
    }

    Date[] dates = new Date[0];
    try {
      dates = dateHelper(target1, target2);
    } catch (Exception e) {
      //do nothing
    }
    float[] values;
    try {
      v.printLine("Please wait while the API retrieves that information.");
      values = p.portfolioPerformance(name, dates, api);
    } catch (Exception e) {
      v.printLine("There was an error attempting to calculate the portfolio's performance.");
      v.showPortfolioScreen();
      return;
    }
    if (values.length == 0) {
      v.printLine("There was an error attempting to calculate the portfolio's performance.");
      v.showPortfolioScreen();
      return;
    }
    v.printLines(performanceOverTimeHelper(name, dates, values));
    v.printLine("Enter any key to return to the previous menu.");
    sc.nextLine();
    v.showPortfolioScreen();
  }

  private Date[] dateHelper(Date start, Date end) {
    long day = 1000L * 60 * 60 * 24;
    ArrayList<Date> tempDateList = new ArrayList<>();
    Date current = start;
    if (end.getTime() - start.getTime() < 5 * day) {
      while (current.compareTo(end) < 0) {
        tempDateList.add(current);
        current = new Date(current.getTime() + day);
      }
      Date[] out = new Date[tempDateList.size()];
      for (int i = 0; i < tempDateList.size(); i++) {
        out[i] = tempDateList.get(i);
      }
      return out;
    }

    Date fakeEnd = new Date(end.getTime() - day); //a day earlier

    boolean goodEnough = false;
    int days = (int) ((end.getTime() - start.getTime()) / day);
    int i = 0;
    int extra = 0;
    while (!goodEnough) {
      extra = 0;
      i++;
      if (days % i == 0 && days / i < 30 && days / i > 4) {
        goodEnough = true;
      } else {
        if (days / i < 30 && days / i > 4) {
          extra = days % i;
          goodEnough = true;
        }
      }
    }
    current = start;
    while (current.before(end)) {
      tempDateList.add(current);
      int extraDay = 0; //we want to apportion our extra days between target dates
      if (extra > 0) {
        extraDay = 1;
      }
      current = new Date(current.getTime() + i * day + extraDay * day);
      if (extra > 0) {
        extra--;
      }
    }

    Date[] out = new Date[tempDateList.size()];
    for (i = 0; i < tempDateList.size(); i++) {
      out[i] = tempDateList.get(i);
    }

    return out;
  }

  private String[] performanceOverTimeHelper(String name, Date[] dates, float[] values) {

    String[] out = new String[dates.length + 2];

    float min = values[0];
    float max = values[0];
    for (int i = 0; i < values.length; i++) {
      if (values[i] > max) {
        max = values[i];
      }
      if (values[i] < min) {
        min = values[i];
      }
    }

    int base = 1;

    while (min - Math.pow(10, base + 1) >= 0) {
      base = base + 1;
    }
    if (base < 2) {
      base = 0;
    } else {
      base = (int) Math.pow(10, base);
    }
    while (min - base * 2 > 0) {
      base = base * 2;
    }

    float ast = (max - base) / 40;
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    out[0] = "Contents of Flexible Portfolio: \"" + name + "\" from " + formatter.format(dates[0])
        + " to "
        + formatter.format(new Date(dates[dates.length - 1].getTime()
        + 1000L * 60 * 60 * 24))
        + "\n"; //we add a day, since the array's last date was a day before
    for (int i = 0; i < dates.length; i++) {
      int astCount = 1;
      if (values[i] > 0) {
        while (astCount * ast + base <= values[i]) {
          astCount++;
        }
      }
      out[i + 1] = formatter.format(dates[i]) + ": " + "*".repeat(astCount);
    }
    out[dates.length + 1] = "\nOne * represents up to: $" + String.format("%.02f", ast)
        + " above the base of $" + base;
    return out;
  }
}
