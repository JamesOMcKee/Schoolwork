package model;

import controller.API;

import controller.PersistenceInterface;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class representing the methods to build, read, write, and get contents of the desired
 * portfolio.
 */

public class PortfolioManagerImpl implements PortfolioManager {

  private ArrayList<Portfolio> portfolios = new ArrayList<>();

  private PersistenceInterface pers;
  private float commissionFee;

  /**
   * A constructor for the portfolio manager that takes in a persistence object.
   *
   * @param pers the persistence object
   */
  public PortfolioManagerImpl(PersistenceInterface pers) {
    this.pers = pers;
  }

  @Override
  public void portBuilder(ArrayList<String> tickerList, ArrayList<Float> floatList, String name) {
    ArrayList<Stock<String, Float>> finalList = new ArrayList<>();
    for (int i = 0; i < tickerList.size(); i++) {
      finalList.add(new Stock<>(tickerList.get(i), floatList.get(i)));
    }
    PortfolioImpl newPort = PortfolioImpl.builder().build(finalList, name);
    portfolios.add(newPort);
  }

  @Override
  public void portFlexBuilder(String name) {
    Portfolio flexPort = new FlexPortfolioImpl(name);
    portfolios.add(flexPort);
  }

  private Portfolio getPortfolio(String name) {
    int size = portfolios.size();

    for (int i = 0; i < size; i++) {
      if (portfolios.get(i).getPortfolioName().equals(name)) {
        return portfolios.get(i);
      }
    }

    throw new IllegalArgumentException("No portfolio by that name was found.");
  }

  @Override
  public String readPortfolioFile(String filename) throws IllegalArgumentException,
      IOException, ParseException {

    if (filename.length() < 5) {
      throw new IllegalArgumentException("String given is not an accepted filename.");
    }
    if (filename.endsWith(".csv")) {
      Portfolio in = pers.loadCSV(filename);
      portfolios.add(in);
    } else {
      throw new IllegalArgumentException("String given is not an accepted filename.");
    }

    return filename.substring(0, filename.length() - 4);
  }

  @Override
  public void savePortfolio(String portfolioName) throws IOException {
    Portfolio thisPortfolio = getPortfolio(portfolioName);
    try {
      ((FlexPortfolioImpl) thisPortfolio).getDates();
      pers.saveFlexCSV(thisPortfolio);
    } catch (Exception e) {
      pers.saveSimpleCSV(thisPortfolio);
    }
  }

  @Override
  public String[] getPortfolioNames() {
    int size = portfolios.size();
    String[] listOfNames = new String[size];

    if (size == 0) {
      throw new IllegalArgumentException("There are no portfolios yet.");
    }
    for (int i = 0; i < size; i++) {
      listOfNames[i] = portfolios.get(i).getPortfolioName();
    }

    return listOfNames;
  }

  @Override
  public String[] getFlexPortfolioNames() {
    int j = 0;
    int size = portfolios.size();
    String[] listOfNames = new String[size];

    if (size == 0) {
      throw new IllegalArgumentException("There are no flexible portfolios yet.");
    }
    for (int i = 0; i < size; i++) {
      if (portfolios.get(i) instanceof FlexPortfolioImpl) {
        listOfNames[j] = portfolios.get(i).getPortfolioName();
        j++;
      }
    }

    if (j == 0) {
      throw new IllegalArgumentException("There are no flexible portfolios yet.");
    }

    String[] flexNames = new String[j];
    for (int i = 0; i < j; i++) {
      flexNames[i] = listOfNames[i];
    }

    return flexNames;
  }

  @Override
  public float[] getPortfolioValue(String name, Date target, controller.API api)
      throws IOException, ParseException {
    try {
      Portfolio subject = getPortfolio(name);
      //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      //Date target = format.parse(date);
      String[] startTickers = subject.getTickers();
      Float[] startCounts = subject.getCounts();
      Date[] startDates = ((FlexPortfolioImpl) subject).getDates();
      //DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      int j = 0;
      for (int i = 0; i < startDates.length; i++) {
        if (startDates[i].compareTo(target) < 1) {
          j++;
        }
      }
      String[] tickers = new String[j];
      Float[] counts = new Float[j];

      int k = 0;
      int l = 0;
      while (k < j) {
        if (startDates[l].compareTo(target) < 1) {
          tickers[k] = startTickers[l];
          counts[k] = startCounts[l];
          k++;
        }
        l++;
      }
      float[] values = api.getPrices(tickers, target);
      for (int i = 0; i < values.length; i++) {
        values[i] = values[i] * counts[i];
      }
      return values;


    } catch (Exception e) {
      Portfolio subject = getPortfolio(name);
      String[] tickers = subject.getTickers();
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      //Date target = format.parse(date);

      float[] values = api.getPrices(tickers, target);

      return values;
    }
  }

  @Override
  public boolean checkFlexEdit(String name, String ticker, float count, Date date) {
    FlexPortfolioImpl port = (FlexPortfolioImpl) getPortfolio(name);
    return port.checkEdit(ticker, count, date);
  }

  /**
   * This method performs a check on a submitted ticker symbol to see if it matches those in our
   * database.
   *
   * @param ticker the symbol being checked
   * @return a boolean for whether the symbol was or was not recognized
   * @throws IOException when there is difficulty reading the reference file
   */
  public boolean validateTicker(String ticker) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("./Full Ticker List.csv"));
    String row = reader.readLine();

    while (row != null) {
      String[] elements = row.split(",");
      if (elements[0].equals(ticker)) {
        return true;
      }
      row = reader.readLine();
    }
    reader.close();
    return false;
  }

  @Override
  public boolean validateTicker(String ticker, Date date) throws IOException, ParseException {
    BufferedReader reader = new BufferedReader(new FileReader("./Full Ticker List.csv"));
    String row = reader.readLine();

    while (row != null) {
      String[] elements = row.split(",");
      if (elements[0].equals(ticker)) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date readDate = format.parse(elements[1]);
        return (readDate.compareTo(date) < 0);
      }
      row = reader.readLine();
    }
    reader.close();
    return false;
  }

  @Override
  public void editFlexPortfolio(String name, String ticker, Float count, Date date)
      throws IllegalArgumentException {
    FlexPortfolioImpl port = (FlexPortfolioImpl) getPortfolio(name);
    port.addFlexStock(ticker, count, date);
  }

  @Override
  public float[] getCostBasis(String name, Date target, controller.API api)
      throws ParseException, IOException {
    FlexPortfolioImpl subject = (FlexPortfolioImpl) getPortfolio(name);
    String[] startTickers = subject.getTickers();
    Float[] startCounts = subject.getCounts();
    Date[] startDates = subject.getDates();
    //DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    //Date target = formatter.parse(date);
    int j = 0;
    for (int i = 0; i < startDates.length; i++) {
      if (startDates[i].compareTo(target) < 1 && startCounts[i] > 0) {
        j++;
      }
    }
    String[] tickers = new String[j];
    Date[] dates = new Date[j];
    int k = 0;
    int l = 0;
    while (k < j) {
      if (startDates[l].compareTo(target) < 1 && startCounts[l] > 0) {
        tickers[k] = startTickers[l];
        dates[k] = startDates[l];
        k++;
      }
      l++;
    }
    float[] values = new float[j];
    for (int i = 0; i < tickers.length; i++) {
      String[] temp = new String[1];
      temp[0] = tickers[i];
      float[] apiOut = api.getPrices(temp, dates[i]);
      values[i] = apiOut[0];
    }
    return values;
  }

  @Override
  public float[] portfolioPerformance(String name, Date[] dates, API api)
      throws IOException, ParseException {
    float[] out = new float[dates.length];

    for (int i = 0; i < dates.length; i++) {
      float[] values = getPortfolioValue(name, dates[i], api);
      float sum = 0;
      for (int j = 0; j < values.length; j++) {
        sum += values[j];
      }
      out[i] = sum;
    }
    return out;
  }

  @Override
  public String[] getTickers(String name) {
    return getPortfolio(name).getTickers();
  }

  @Override
  public Float[] getCounts(String name) {
    return getPortfolio(name).getCounts();
  }

  @Override
  public Date[] getDates(String name) throws IllegalArgumentException {

    try {
      Portfolio flexPort = getPortfolio(name);
      return ((FlexPortfolioImpl) flexPort).getDates();
    } catch (Exception e) {
      throw new IllegalArgumentException("Portfolio must be a flexible portfolio "
          + "in order to get dates.");
    }

  }

  @Override
  public float getCommissionFee() {
    return commissionFee;
  }

  @Override
  public void setCommissionFee(float cf) throws IllegalArgumentException {
    if (cf < 0) {
      throw new IllegalArgumentException();
    }
    this.commissionFee = cf;
  }

  @Override
  public void addStrategy(String portfolioName, ArrayList<Stock<String, Float>> list, Date start,
      Date end, int frequency) {
    Strategy newStrat = new DCAStrategy(list, start, end, frequency);
    getPortfolio(portfolioName).addStrategy(newStrat);
  }

  @Override
  public void updateFromStrategy(String portfolioName, API api) throws IOException, ParseException {
    ArrayList<Strategy> stratList = getPortfolio(portfolioName).getStrategies();
    Strategy strat = stratList.get(stratList.size() - 1);
    ArrayList<Stock<String, Float>> list = strat.getList();
    String[] tickers = new String[list.size()];
    Float[] percentages = new Float[list.size()];
    float amount = 0;
    for (int i = 0; i < list.size(); i++) {
      tickers[i] = list.get(i).getS();
      percentages[i] = list.get(i).getF();
      amount += percentages[i];
    }
    int freq = strat.getFrequency();
    Date start = strat.getStartDate();
    Date end = strat.getEndDate();
    long interval = freq * 1000L * 60 * 60 * 24;

    ArrayList<String> tickerBuys = new ArrayList<>();
    ArrayList<Float> countBuys = new ArrayList<>();
    ArrayList<Date> dateBuys = new ArrayList<>();

    Date upperLimit = new Date();
    upperLimit = new Date(upperLimit.getTime() - (1000L * 60 * 60 * 24)); //yesterday

    Date current = start;
    while (current.before(upperLimit) && current.before(end)) {
      float[] prices;

      prices = api.getPricesAfter(tickers, current);

      for (int k = 0; k < prices.length; k++) {
        if (prices[k] == 0) {
          throw new IOException();
        }
      }
      for (int j = 0; j < tickers.length; j++) {
        if (percentages[j] == 0.0) {
          continue;
        }
        float countBuy = (percentages[j]) / prices[j];
        tickerBuys.add(tickers[j]);
        countBuys.add(countBuy);
        dateBuys.add(current);
      }

      current = new Date(current.getTime() + interval);
    }

    for (int i = 0; i < tickerBuys.size(); i++) {
      editFlexPortfolio(portfolioName, tickerBuys.get(i), countBuys.get(i), dateBuys.get(i));
    }
  }

}