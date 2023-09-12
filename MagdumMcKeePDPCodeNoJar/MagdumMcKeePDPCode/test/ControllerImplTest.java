import controller.API;
import controller.ControllerImpl;
import controller.InputController;
import controller.PersistenceInterface;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.FlexPortfolioImpl;
import model.Portfolio;
import model.PortfolioManager;
import model.Stock;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import view.ViewInterface;

/**
 * A JUnit test for the ControllerImpl.
 */
public class ControllerImplTest {

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
  public void loadTest() {
    Readable in = new StringReader("text\n1\n1\ndummyTest.csv\nk\n2\n5\n");
    StringBuilder log = new StringBuilder();
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    InputController con = new ControllerImpl(mockV, mockP, in, System.out, mockA);
    con.start();
    assertEquals("printLine method called showWelcomeScreen method called showLoadScreen "
        + "method called printLine method called getPortfolioNames method called readPortfolioFile"
        + " method called with dummyTest.csv getTickers method called with dummyTest getCounts "
        + "method called with dummyTest getDates method called with dummyTest printLines method "
        + "called printLine method called showLoadScreen method called showWelcomeScreen method "
        + "called ", log.toString());
  }

  @Test
  public void buildSimpleTest() {
    Readable in = new StringReader("text\n2\n1\ndummy\nGOOG\n10\ndone\nk\n7\n5\n");
    StringBuilder log = new StringBuilder();
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    InputController con = new ControllerImpl(mockV, mockP, in, System.out, mockA);
    con.start();
    assertEquals("printLine method called showWelcomeScreen method called showBuildScreen "
        + "method called printLine method called getPortfolioNames method called printLine "
        + "method called validateTicker method called with GOOG printLine method called "
        + "printLine method called portBuilder method called with [GOOG], [10.0], dummy "
        + "getTickers method called with dummy getCounts method called with dummy getDates "
        + "method called with dummy printLines method called printLine method called "
        + "showBuildScreen method called showWelcomeScreen method called ", log.toString());
  }

  @Test
  public void buildFlexTest() {
    Readable in = new StringReader("text\n2\n2\ndummy\nb\nGOOG\n10\n2016\n01\n01\n"
        + "done\nk\n7\n5\n");
    StringBuilder log = new StringBuilder();
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    ViewInterface mockV = new MockView(log);
    InputController con = new ControllerImpl(mockV, mockP, in, System.out, mockA);
    con.start();
    assertEquals("printLine method called showWelcomeScreen method called "
        + "showBuildScreen method called printLine method called getPortfolioNames method"
        + " called portFlexBuilder method called with dummy printLine method called printLine "
        + "method called validateTicker method called with GOOG printLine method called "
        + "printLine method called printLine method called printLine method called "
        + "validateTicker method called with GOOG and 01-01-2016 editFlexPortfolio "
        + "method called with dummy, GOOG, 10.0, 01-01-2016 printLine method called "
        + "getTickers method called with dummy getCounts method called with dummy "
        + "getDates method called with dummy printLines method called printLine method "
        + "called showBuildScreen method called showWelcomeScreen method called ", log.toString());
  }

}
