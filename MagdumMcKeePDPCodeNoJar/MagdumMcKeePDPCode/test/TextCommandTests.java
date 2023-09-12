import static org.junit.Assert.assertEquals;

import controller.API;
import controller.PersistenceInterface;
import controller.TextCommand;
import controller.textcoms.BuildFlexibleCommand;
import controller.textcoms.BuildSimpleCommand;
import controller.textcoms.DollarCostBuyCommand;
import controller.textcoms.EditFlexibleCommand;
import controller.textcoms.LoadCommand;
import controller.textcoms.ManualValuationCommand;
import controller.textcoms.PortfolioPerformanceCommand;
import controller.textcoms.PortfolioValueCommand;
import controller.textcoms.SaveAllCommand;
import controller.textcoms.SaveCommand;
import controller.textcoms.StrategyBuildCommand;
import controller.textcoms.StrategyCommand;
import controller.textcoms.ViewContentsCommand;
import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import model.FlexPortfolioImpl;
import model.Portfolio;
import model.PortfolioManager;
import model.PortfolioManagerImpl;
import model.Stock;
import org.junit.Test;
import view.ViewInterface;

/**
 * A JUnit test class for the controller's text commands.
 */
public class TextCommandTests {

  /**
   * A mock model.
   */
  public class MockPortfolioManager implements PortfolioManager {

    private StringBuilder log;

    /**
     * A constructor for the mock model.
     *
     * @param log a stringbuilder for logging
     */
    public MockPortfolioManager(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void portBuilder(ArrayList<String> tickerList, ArrayList<Float> floatList, String name) {
      log.append("portBuilder method called with " + tickerList + ", "
          + floatList + ", " + name + " ");
    }

    @Override
    public void portFlexBuilder(String name) {
      log.append("portFlexBuilder method called with " + name + " ");
    }

    @Override
    public String readPortfolioFile(String filename) {
      log.append("readPortfolioFile method called with " + filename + " ");
      return null;
    }

    @Override
    public void savePortfolio(String portfolioName) {
      log.append("savePortfolio method called with " + portfolioName + " ");
    }

    @Override
    public String[] getPortfolioNames() {
      log.append("getPortfolioNames method called ");
      return new String[1];
    }

    @Override
    public String[] getFlexPortfolioNames() {
      log.append("getFlexPortfolioNames method called ");
      return new String[1];
    }

    @Override
    public float[] getPortfolioValue(String name, Date date, controller.API api) {
      log.append("getPortfolioValue method called with " + name + " and " + date + " ");
      return new float[0];
    }

    @Override
    public boolean checkFlexEdit(String name, String ticker, float count, Date date) {
      log.append("checkFlexEdit method called with " + name + ", " + ticker + ", "
          + count + ", " + date + " ");
      return false;
    }

    @Override
    public boolean validateTicker(String ticker) {
      log.append("validateTicker method called with " + ticker + " ");
      return true;
    }

    @Override
    public boolean validateTicker(String ticker, Date date) {
      SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
      log.append(
          "validateTicker method called with " + ticker + " and " + formatter.format(date) + " ");
      return true;
    }

    @Override
    public void editFlexPortfolio(String name, String ticker, Float count, Date date)
        throws IllegalArgumentException {
      SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
      log.append("editFlexPortfolio method called with " + name + ", " + ticker + ", "
          + count + ", " + formatter.format(date) + " ");
    }

    @Override
    public float[] getCostBasis(String name, Date date, controller.API api) {
      log.append("getCostBasis method called with " + name + " and " + date + " ");
      return new float[0];
    }

    @Override
    public float[] portfolioPerformance(String name, Date[] dates, controller.API api) {
      log.append("portfolioPerformance method called with " + name + " ");
      return new float[0];
    }

    @Override
    public String[] getTickers(String name) {
      log.append("getTickers method called with " + name + " ");
      return new String[0];
    }

    @Override
    public Float[] getCounts(String name) {
      log.append("getCounts method called with " + name + " ");
      return new Float[0];
    }

    @Override
    public Date[] getDates(String name) throws IllegalArgumentException {
      log.append("getDates method called with " + name + " ");
      return new Date[0];
    }

    @Override
    public float getCommissionFee() {
      log.append("getCommissionFee method called ");
      return 0;
    }

    @Override
    public void setCommissionFee(float fee) {
      log.append(String.format("setCommissionFee method called with %.2f", fee) + " ");
    }

    @Override
    public void addStrategy(String portfolioName, ArrayList<Stock<String, Float>> list, Date start,
        Date end, int frequency) {
      log.append("addStrategy method called ");
    }

    @Override
    public void updateFromStrategy(String portfolioName, API api)
        throws IOException, ParseException {
      log.append("updateFromStrategy method called ");
    }
  }

  /**
   * A mock view to be passed to the portfolio controller for testing purposes.
   */
  public class MockView implements ViewInterface {

    private StringBuilder log;

    /**
     * A constructor for the mock view.
     *
     * @param log a stringbuilder for logging
     */
    public MockView(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void showWelcomeScreen() {
      log.append("showWelcomeScreen method called ");
    }

    @Override
    public void showLoadScreen() {
      log.append("showLoadScreen method called ");
    }

    @Override
    public void showBuildScreen() {
      log.append("showBuildScreen method called ");
    }

    @Override
    public void showPortfolioScreen() {
      log.append("showPortfolioScreen method called ");
    }

    @Override
    public void showSaveScreen() {
      log.append("showSaveScreen method called ");
    }

    @Override
    public void printLine(String line) {
      log.append("printLine method called ");
    }

    @Override
    public void printLines(String[] lines) {
      log.append("printLines method called ");
    }
  }

  /**
   * A mock API for testing.
   */
  public class MockAPI implements API {

    private StringBuilder log;

    /**
     * A constructor for the mock API.
     *
     * @param log a stringbuilder for logging
     */
    public MockAPI(StringBuilder log) {
      this.log = log;
    }

    @Override
    public float[] getPrices(String[] tickerList, Date date) throws IOException, ParseException {
      log.append("getPrices method called ");
      return new float[tickerList.length];
    }

    @Override
    public float[] getPricesAfter(String[] tickerList, Date date)
        throws IOException, ParseException {
      log.append("getPricesAfter method called ");
      float[] out = new float[tickerList.length];
      for (int i = 0; i < tickerList.length; i++) {
        out[i] = 1;
      }
      return out;
    }
  }

  /**
   * A mock class for the persistence object.
   */
  public class MockPersistence implements PersistenceInterface {

    private StringBuilder log;

    /**
     * A constructor for the mock persistence object.
     *
     * @param log a stringbuilder for logging
     */
    public MockPersistence(StringBuilder log) {
      this.log = log;
    }

    @Override
    public void saveSimpleCSV(Portfolio simplePort) throws IOException {
      log.append("saveSimpleCSV method called ");
    }

    @Override
    public void saveFlexCSV(Portfolio flexPort) throws IOException {
      log.append("saveFlexCSV method called ");
    }

    @Override
    public Portfolio loadCSV(String filename) throws IOException, ParseException {
      log.append("loadCSV method called with " + filename + " ");
      return new FlexPortfolioImpl("dummy");
    }
  }

  //TESTS

  @Test
  public void buildSimpleCommandTest() {
    Readable in = new StringReader("dummy\n GOOG\n 10\n done\n");
    StringBuilder log = new StringBuilder();
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    TextCommand simpleC = new BuildSimpleCommand();
    simpleC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLine method called getPortfolioNames method called "
        + "printLine method called validateTicker method called with GOOG "
        + "printLine method called printLine method called "
        + "portBuilder method called with [GOOG], [10.0], dummy "
        + "getTickers method called with dummy getCounts method called with dummy "
        + "getDates method called with dummy printLines method called printLine method called "
        + "showBuildScreen method called ", log.toString());

  }

  @Test
  public void buildFlexibleCommandTest() {
    Readable in = new StringReader("dummy\n b\n GOOG\n 10\n 2010\n 01\n 01\n done\n");
    StringBuilder log = new StringBuilder();
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    TextCommand flexC = new BuildFlexibleCommand();
    flexC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLine method called getPortfolioNames method called "
        + "portFlexBuilder method called with dummy printLine method called "
        + "printLine method called validateTicker method called with GOOG "
        + "printLine method called printLine method called printLine method called "
        + "printLine method called validateTicker method called with GOOG and 01-01-2010 "
        + "editFlexPortfolio method called with dummy, GOOG, 10.0, 01-01-2010 "
        + "printLine method called getTickers method called with dummy "
        + "getCounts method called with dummy getDates method called with dummy "
        + "printLines method called printLine method called "
        + "showBuildScreen method called ", log.toString());
  }

  @Test
  public void strategyBuildCommandTest() {
    //regular
    Readable in = new StringReader("dummy\n 1000\n60\n 2016\n 01\n 01\n "
        + "2020\n 01\n 01\n GOOG\n done\n 100\n \n \n");
    StringBuilder log = new StringBuilder();
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    TextCommand sbC = new StrategyBuildCommand();
    sbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLine method called getPortfolioNames method called "
        + "portFlexBuilder method called with dummy printLine method called printLine "
        + "method called printLine method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called validateTicker method called with GOOG validateTicker "
        + "method called with GOOG and 01-01-2016 printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called printLine method called printLine method called printLines "
        + "method called printLine method called addStrategy method called printLine method "
        + "called updateFromStrategy method called getTickers method called with dummy "
        + "getCounts method called with dummy getDates method called with dummy printLines "
        + "method called printLine method called showBuildScreen method called ", log.toString());

    //no end date
    in = new StringReader("dummy\n 1000\n60\n 2016\n 01\n 01\n"
        + "done\n GOOG\n done\n 100\n \n \n");
    log.delete(0, log.toString().length());
    sbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLine method called getPortfolioNames method called "
        + "portFlexBuilder method called with dummy printLine method called printLine "
        + "method called printLine method called printLine method called printLine method "
        + "called printLine method called printLine method called validateTicker method "
        + "called with GOOG validateTicker method called with GOOG and 01-01-2016 printLine "
        + "method called printLine method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called printLines method called printLine method called "
        + "addStrategy method called printLine method called updateFromStrategy method "
        + "called getTickers method called with dummy getCounts method called with dummy "
        + "getDates method called with dummy printLines method called printLine method "
        + "called showBuildScreen method called ", log.toString());

    //bad percentage
    in = new StringReader("dummy\n 1000\n60\n 2016\n 01\n 01\n"
        + "done\n GOOG\n done\n 90\n \n \n");
    log.delete(0, log.toString().length());
    sbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLine method called getPortfolioNames method called "
        + "portFlexBuilder method called with dummy printLine method called printLine "
        + "method called printLine method called printLine method called printLine method "
        + "called printLine method called printLine method called validateTicker method "
        + "called with GOOG validateTicker method called with GOOG and 01-01-2016 printLine "
        + "method called printLine method called printLine method called printLine method called "
        + "printLine method called printLine method called printLine method called printLine "
        + "method called printLine method called showBuildScreen method called "
        + "showBuildScreen method called ", log.toString());

    //bad ticker
    in = new StringReader("dummy\n 1000\n60\n 2016\n 01\n 01\n"
        + "done\n GO  G\n done\n 90\n \n \n");
    log.delete(0, log.toString().length());
    sbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLine method called getPortfolioNames method called portFlexBuilder "
        + "method called with dummy printLine method called printLine method called printLine "
        + "method called printLine method called printLine method called printLine method "
        + "called printLine method called validateTicker method called with GO validateTicker "
        + "method called with GO and 01-01-2016 printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called printLine method called printLine method called printLine "
        + "method called showBuildScreen method called showBuildScreen "
        + "method called ", log.toString());

  }

  @Test
  public void editFlexibleCommandTest() {
    Readable in = new StringReader("1\n b\n GOOG\n 10\n 2010\n 01\n 01\n done\n \n");
    StringBuilder log = new StringBuilder();
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    TextCommand flexC = new EditFlexibleCommand();
    flexC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("getFlexPortfolioNames method called printLines method called "
        + "printLine method called getTickers method called with null "
        + "getCounts method called with null getDates method called with null "
        + "printLines method called printLine method called printLine method called "
        + "validateTicker method called with GOOG printLine method called "
        + "printLine method called printLine method called printLine method called "
        + "validateTicker method called with GOOG and 01-01-2010 "
        + "editFlexPortfolio method called with null, GOOG, 10.0, 01-01-2010 "
        + "printLine method called getTickers method called with null "
        + "getCounts method called with null getDates method called with null "
        + "printLines method called printLine method called "
        + "showBuildScreen method called ", log.toString());
  }

  @Test
  public void strategyCommandTest() {
    //regular
    Readable in = new StringReader("1\n 1000\n60\n 2016\n 01\n 01\n "
        + "2020\n 01\n 01\n done\n 100\n \n \n");
    StringBuilder log = new StringBuilder();
    PortfolioManager mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    mockP.editFlexPortfolio("dummy", "GOOG", 10f, new Date());
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    TextCommand sbC = new StrategyCommand();
    sbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called printLine method called printLine method called printLine "
        + "method called printLine method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called printLine method called printLines method called "
        + "printLine method called printLine method called getPricesAfter method called "
        + "getPricesAfter method called getPricesAfter method called getPricesAfter "
        + "method called getPricesAfter method called getPricesAfter method called "
        + "getPricesAfter method called getPricesAfter method called getPricesAfter "
        + "method called getPricesAfter method called getPricesAfter method called "
        + "getPricesAfter method called getPricesAfter method called getPricesAfter "
        + "method called getPricesAfter method called getPricesAfter method called "
        + "getPricesAfter method called getPricesAfter method called getPricesAfter "
        + "method called getPricesAfter method called getPricesAfter method called "
        + "getPricesAfter method called getPricesAfter method called getPricesAfter "
        + "method called getPricesAfter method called printLines method called "
        + "printLine method called showBuildScreen method called ", log.toString());

    //no end date
    in = new StringReader("1\n 1000\n60\n 2016\n 01\n 01\n"
        + "done\n done\n 100\n \n \n");
    log.delete(0, log.toString().length());
    mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    mockP.editFlexPortfolio("dummy", "GOOG", 10f, new Date());
    sbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called printLine method called printLine method called printLine "
        + "method called printLine method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLines method called printLine method called printLine method called "
        + "getPricesAfter method called getPricesAfter method called getPricesAfter method "
        + "called getPricesAfter method called getPricesAfter method called getPricesAfter "
        + "method called getPricesAfter method called getPricesAfter method called "
        + "getPricesAfter method called getPricesAfter method called getPricesAfter method "
        + "called getPricesAfter method called getPricesAfter method called getPricesAfter "
        + "method called getPricesAfter method called getPricesAfter method called "
        + "getPricesAfter method called getPricesAfter method called getPricesAfter method "
        + "called getPricesAfter method called getPricesAfter method called getPricesAfter "
        + "method called getPricesAfter method called getPricesAfter method called "
        + "getPricesAfter method called getPricesAfter method called getPricesAfter method "
        + "called getPricesAfter method called getPricesAfter method called getPricesAfter "
        + "method called getPricesAfter method called getPricesAfter method called "
        + "getPricesAfter method called getPricesAfter method called getPricesAfter method "
        + "called getPricesAfter method called getPricesAfter method called getPricesAfter "
        + "method called getPricesAfter method called getPricesAfter method called "
        + "getPricesAfter method called getPricesAfter method called getPricesAfter method "
        + "called printLines method called printLine method called "
        + "showBuildScreen method called ", log.toString());

    //bad percentage
    in = new StringReader("1\n 1000\n60\n 2016\n 01\n 01\n"
        + "done\n done\n 90\n \n \n");
    log.delete(0, log.toString().length());
    mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    mockP.editFlexPortfolio("dummy", "GOOG", 10f, new Date());
    sbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called printLine "
        + "method called printLine method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called printLine method called printLine method called "
        + "printLine method called printLine method called printLine method called printLine "
        + "method called printLine method called showBuildScreen method called "
        + "showBuildScreen method called ", log.toString());

    //bad ticker
    in = new StringReader("1\n 1000\n60\n 2016\n 01\n 01\n"
        + "done\n done\n 100\n \n \n");
    log.delete(0, log.toString().length());
    mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    mockP.editFlexPortfolio("dummy", "GOG", 10f, new Date());
    sbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called printLine method called printLine method called "
        + "showBuildScreen method called showBuildScreen method called ", log.toString());

  }

  @Test
  public void dollarCostBuyCommandTest() {
    Readable in = new StringReader("1\n 1000\n 2015\n 01\n 01\n 100\n \n");
    StringBuilder log = new StringBuilder();
    PortfolioManager mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    try {
      mockP.editFlexPortfolio("dummy", "GOOG", 100f,
          formatter.parse("2016-01-01"));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    //working properly
    mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    try {
      mockP.editFlexPortfolio("dummy", "GOOG", 100f,
          formatter.parse("2016-01-01"));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    TextCommand dcbC = new DollarCostBuyCommand();
    dcbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called printLines method called printLine method called "
        + "printLine method called printLine method called getPricesAfter method called "
        + "printLines method called printLine method called "
        + "showBuildScreen method called ", log.toString());

    //bad date
    mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    try {
      mockP.editFlexPortfolio("dummy", "GOOG", 100f,
          formatter.parse("2016-01-01"));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    log.delete(0, log.toString().length());
    in = new StringReader("1\n 1000\n 202k\n 01\n 01\n \n");
    dcbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called printLine method "
        + "called printLine method called printLine method called "
        + "printLine method called printLine method called "
        + "showBuildScreen method called ", log.toString());

    //date too early
    mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    try {
      mockP.editFlexPortfolio("dummy", "GOOG", 100f,
          formatter.parse("2016-01-01"));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    log.delete(0, log.toString().length());
    in = new StringReader("1\n 1000\n 2023\n 01\n 01\n \n");
    dcbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method "
        + "called printLine method called showBuildScreen method called ", log.toString());

    //date too late
    mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    try {
      mockP.editFlexPortfolio("dummy", "GOOG", 100f,
          formatter.parse("2016-01-01"));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    log.delete(0, log.toString().length());
    in = new StringReader("1\n 1000\n 1999\n 01\n 01\n");
    dcbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method "
        + "called printLine method called showBuildScreen method called ", log.toString());

    //ticker not yet available
    mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    try {
      mockP.editFlexPortfolio("dummy", "GOOG", 100f,
          formatter.parse("2016-01-01"));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    log.delete(0, log.toString().length());
    in = new StringReader("1\n 1000\n 2010\n 01\n 01\n \n");
    dcbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called showBuildScreen method called ", log.toString());

    //bad percentage given
    mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    try {
      mockP.editFlexPortfolio("dummy", "GOOG", 100f,
          formatter.parse("2016-01-01"));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    log.delete(0, log.toString().length());
    in = new StringReader("1\n 1000\n 2016\n 01\n 01\n k\n 100\n \n");
    dcbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called printLines method called printLine method called "
        + "printLine method called printLine method called printLine method called "
        + "printLine method called printLine method called getPricesAfter method called "
        + "printLines method called printLine method called "
        + "showBuildScreen method called ", log.toString());

    //negative percentage given
    mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    try {
      mockP.editFlexPortfolio("dummy", "GOOG", 100f,
          formatter.parse("2016-01-01"));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    log.delete(0, log.toString().length());
    in = new StringReader("1\n 1000\n 2016\n 01\n 01\n -25\n 100\n \n");
    dcbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called printLines method called printLine method called "
        + "printLine method called printLine method called printLine method called "
        + "printLine method called printLine method called getPricesAfter method called "
        + "printLines method called printLine method called "
        + "showBuildScreen method called ", log.toString());

    //percentages dont add to 100
    mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    try {
      mockP.editFlexPortfolio("dummy", "GOOG", 100f,
          formatter.parse("2016-01-01"));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    log.delete(0, log.toString().length());
    in = new StringReader("1\n 1000\n 2016\n 01\n 01\n 50\n ");
    dcbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called "
        + "printLine method called printLine method called printLine method called "
        + "printLine method called printLine method called printLines method called "
        + "printLine method called printLine method called printLine method called "
        + "printLine method called showBuildScreen method called ", log.toString());

    //bad amount
    mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    try {
      mockP.editFlexPortfolio("dummy", "GOOG", 100f,
          formatter.parse("2016-01-01"));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    log.delete(0, log.toString().length());
    in = new StringReader("1\n -1000\n 2016\n 01\n 01\n ");
    dcbC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called printLine method "
        + "called printLine method called showBuildScreen method called ", log.toString());
  }

  @Test
  public void viewContentsCommandTest() {
    Readable in = new StringReader("1\n \n");
    StringBuilder log = new StringBuilder();
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    PortfolioManager mockP = new PortfolioManagerImpl(new MockPersistence(log));
    mockP.portFlexBuilder("dummy");
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    try {
      mockP.editFlexPortfolio("dummy", "GOOG", 100f,
          formatter.parse("2016-01-01"));
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
    TextCommand vcC = new ViewContentsCommand();
    vcC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine "
        + "method called printLines method called printLine method "
        + "called showPortfolioScreen method called ", log.toString());
  }

  @Test
  public void portfolioValueCommandTest() {
    Readable in = new StringReader("1\n 2020\n 01\n 01\n \n");
    StringBuilder log = new StringBuilder();
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    PortfolioManager mockP = new MockPortfolioManager(log);
    TextCommand vcC = new PortfolioValueCommand();
    vcC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("getPortfolioNames method called printLines method called printLine "
        + "method called printLine method called printLine method called printLine method "
        + "called printLine method called getTickers method called with null getCounts method "
        + "called with null getDates method called with null getPortfolioValue method "
        + "called with null and Wed Jan 01 00:00:00 EST 2020 printLines method called "
        + "printLine method called showPortfolioScreen method called ", log.toString());
  }

  @Test
  public void costBasisCommandTest() {
    Readable in = new StringReader("1\n 2020\n 01\n 01\n 0\n");
    StringBuilder log = new StringBuilder();
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    PortfolioManager mockP = new MockPortfolioManager(log);
    TextCommand vcC = new PortfolioValueCommand();
    vcC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("getPortfolioNames method called printLines method called printLine "
        + "method called printLine method called printLine method called printLine method "
        + "called printLine method called getTickers method called with null getCounts method "
        + "called with null getDates method called with null getPortfolioValue method "
        + "called with null and Wed Jan 01 00:00:00 EST 2020 printLines method called "
        + "printLine method called showPortfolioScreen method called ", log.toString());
  }

  @Test
  public void portfolioPerformanceCommandTest() {
    Readable in = new StringReader("1\n 2020\n 01\n 01\n 2021\n 01\n 01\n \n");
    StringBuilder log = new StringBuilder();
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    PortfolioManager mockP = new MockPortfolioManager(log);
    TextCommand vcC = new PortfolioPerformanceCommand();
    vcC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("getFlexPortfolioNames method called printLines method called printLine"
        + " method called printLine method called printLine method called printLine method "
        + "called printLine method called printLine method called printLine method called "
        + "printLine method called printLine method called portfolioPerformance method "
        + "called with null printLine method called showPortfolioScreen "
        + "method called ", log.toString());
  }

  @Test
  public void manualValuationCommandTest() {
    Readable in = new StringReader("1\n ");
    StringBuilder log = new StringBuilder();
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    TextCommand manualC = new ManualValuationCommand();
    manualC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("getPortfolioNames method called printLines method called "
        + "printLine method called getTickers method called with null "
        + "getCounts method called with null printLine method called printLines method called "
        + "printLine method called showPortfolioScreen method called ", log.toString());
  }

  @Test
  public void saveCommandTest() {
    Readable in = new StringReader(" ");
    StringBuilder log = new StringBuilder();
    PortfolioManager mockP = new PortfolioManagerImpl(new MockPersistence(log));
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    TextCommand saveC = new SaveCommand();
    saveC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLine method called showSaveScreen method called ", log.toString());

    mockP.portFlexBuilder("dummy");
    in = new StringReader("2\n ");
    log.delete(0, log.toString().length());

    saveC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called "
        + "printLine method called showSaveScreen method called ", log.toString());

    in = new StringReader("1\n");
    log.delete(0, log.toString().length());

    saveC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLines method called printLine method called "
        + "saveFlexCSV method called printLine method called "
        + "showSaveScreen method called ", log.toString());
  }

  @Test
  public void saveAllCommandTest() {
    Readable in = new StringReader(" ");
    StringBuilder log = new StringBuilder();
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    TextCommand saveC = new SaveAllCommand();
    saveC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("getPortfolioNames method called savePortfolio method "
        + "called with null printLine method called "
        + "showSaveScreen method called ", log.toString());

  }

  @Test
  public void loadCommandTest() {
    Readable in = new StringReader("flex.csv");
    StringBuilder log = new StringBuilder();
    PortfolioManager mockP = new PortfolioManagerImpl(new MockPersistence(log));
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    TextCommand loadC = new LoadCommand();
    loadC.goDoStuff(new Scanner(in), mockV, mockP, mockA);
    assertEquals("printLine method called loadCSV method called with flex.csv "
        + "printLine method called showLoadScreen method called ", log.toString());
  }
}
