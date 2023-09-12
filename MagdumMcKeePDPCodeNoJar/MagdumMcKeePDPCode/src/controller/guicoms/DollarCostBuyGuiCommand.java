package controller.guicoms;

import controller.API;
import controller.GuiCommand;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.PortfolioManager;
import model.Stock;
import view.GuiInterface;

/**
 * A GuiCommand to finish a dollar cost buy.
 */
public class DollarCostBuyGuiCommand implements GuiCommand {

  @Override
  public void goDoStuff(GuiInterface f, PortfolioManager p, API api) {
    Object[] o = f.getOperationalStuff();
    String name = f.getPortfolioName();
    String amountString = o[0].toString();
    String year1 = o[1].toString();
    String month1 = o[2].toString();
    String day1 = o[3].toString();

    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date target = null;
    float amount;
    try {
      amount = Float.parseFloat(amountString);
      target = formatter.parse(year1 + "-" + month1 + "-" + day1);
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }

    ArrayList<String> tickerList = new ArrayList<>();
    ArrayList<Stock<String, Float>> list = new ArrayList<>();

    if (o.length <= 4) {
      f.printLine("A strategy cannot be applied without specified stocks, please try again.");
      f.setCurrScreen("Error");
      return;
    }

    float[] percentages = new float[(o.length - 4)];
    float sum = 0;
    int j = 0;
    for (int i = 0; i < (o.length - 4); i += 2) {
      float percent;
      try {
        percent = Float.parseFloat(o[i + 5].toString());
      } catch (Exception e) {
        f.printLine("One of the percentages entered was invalid. Please try again.");
        f.setCurrScreen("Error");
        return;
      }
      tickerList.add(o[i + 4].toString());
      percentages[j] = percent;
      sum += percent;
      j++;
    }
    if (Math.abs(100 - sum) > 0.1) {
      f.printLine("The given apportioning does not add up to 100%. Please try again.");
      f.setCurrScreen("Error");
      return;
    }

    String[] tickers = new String[tickerList.size()];
    for (int i = 0; i < tickerList.size(); i++) {
      tickers[i] = tickerList.get(i);
    }

    float[] prices;
    try {
      prices = api.getPricesAfter(tickers, target);
    } catch (Exception e) {
      f.printLine("There was an error during the API call. Please try again.");
      f.setCurrScreen("Error");
      return;
    }
    for (int k = 0; k < prices.length; k++) {
      if (prices[k] == 0) {
        f.printLine("There was an error retrieving prices. Please try again.");
        f.setCurrScreen("Error");
        return;
      }
    }

    f.printLine("Please wait while API is loading the information...");
    for (j = 0; j < tickers.length; j++) {
      float countBuy = (amount * percentages[j] * 0.01f) / prices[j];
      p.editFlexPortfolio(name, tickers[j], countBuy, target);
    }

    new ViewContentsGuiCommand().goDoStuff(f, p, api);
  }
}
