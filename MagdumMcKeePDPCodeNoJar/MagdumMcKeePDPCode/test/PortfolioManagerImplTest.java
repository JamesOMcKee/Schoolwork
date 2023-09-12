import controller.API;
import controller.Persistence;
import controller.PersistenceInterface;
import java.io.StringReader;
import model.DCAStrategy;
import model.FlexPortfolioImpl;
import model.Portfolio;
import model.Stock;
import model.PortfolioManager;
import model.PortfolioManagerImpl;
import model.FlexStock;

import model.Strategy;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * A JUnit test class for portfolio manager implementation class.
 */

public class PortfolioManagerImplTest {

  /**
   * A mock API to be passed to the portfolio manager.
   */
  public class MockAPI implements controller.API {

    private StringBuilder log;

    public MockAPI(StringBuilder log) {
      this.log = log;
    }

    @Override
    public float[] getPrices(String[] tickerList, Date date) {
      log.append("getPrices method called ");
      float[] mFloat = new float[1];
      mFloat[0] = (float) 100;
      return mFloat;
    }

    @Override
    public float[] getPricesAfter(String[] tickerList, Date date)
        throws IOException, ParseException {
      log.append("getPricesAfter called ");
      float[] mFloat = new float[1];
      mFloat[0] = (float) 100;
      return mFloat;
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

  Persistence pers = new Persistence();

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testPortfolioBuilder() {
    ArrayList<Stock<String, Float>> stockList = new ArrayList<>();
    stockList.add(new Stock<>("GOOG", (float) 10.20));
    stockList.add(new Stock<>("AAPL", (float) 11.20));
    stockList.add(new Stock<>("MSFT", (float) 14.80));

    ArrayList<String> tickerList = new ArrayList<>(Arrays.asList("GOOG", "AAPL", "MSFT"));
    ArrayList<Float> floatList = new ArrayList<>(Arrays.asList((float) 10.20,
        (float) 11.20, (float) 14.80));

    PortfolioManager portManager = new PortfolioManagerImpl(pers);
    portManager.portBuilder(tickerList, floatList, "My Portfolio");

    assertEquals(stockList.size(), portManager.getTickers("My Portfolio").length);
    assertEquals("My Portfolio", portManager.getPortfolioNames()[0]);

    assertEquals("GOOG", portManager.getTickers("My Portfolio")[0]);
    assertEquals("AAPL", portManager.getTickers("My Portfolio")[1]);
    assertEquals("MSFT", portManager.getTickers("My Portfolio")[2]);

    assertEquals((float) 10.20, portManager.getCounts("My Portfolio")[0], 0.0001);
    assertEquals((float) 11.20, portManager.getCounts("My Portfolio")[1], 0.0001);
    assertEquals((float) 14.80, portManager.getCounts("My Portfolio")[2], 0.0001);

  }

  @Test
  public void testFlexPortfolioBuilder() {
    ArrayList<FlexStock<String, Float, Date>> flexStockList = new ArrayList<>();
    PortfolioManager portManager = new PortfolioManagerImpl(pers);
    portManager.portFlexBuilder("My Portfolio");

    assertEquals("My Portfolio", portManager.getPortfolioNames()[0]);
    assertEquals("My Portfolio", portManager.getFlexPortfolioNames()[0]);
  }

  @Test
  public void testGetSimplePortfolioNames() throws IllegalArgumentException {

    ArrayList<String> tickerList = new ArrayList<>(Arrays.asList("GOOG", "AAPL", "MSFT"));
    ArrayList<Float> floatList = new ArrayList<>(Arrays.asList((float) 10.20,
        (float) 11.20, (float) 14.80));

    PortfolioManager portManager = new PortfolioManagerImpl(pers);
    portManager.portBuilder(tickerList, floatList, "My Portfolio_1");
    portManager.portBuilder(tickerList, floatList, "My Portfolio_2");

    assertEquals("My Portfolio_1", portManager.getPortfolioNames()[0]);
    assertEquals("My Portfolio_2", portManager.getPortfolioNames()[1]);

    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("There are no flexible portfolios yet.");
    portManager.getFlexPortfolioNames();
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("There are no flexible portfolios yet.");
    portManager.getFlexPortfolioNames();
  }

  @Test
  public void testGetFlexPortfolioNames() {
    ArrayList<FlexStock<String, Float, Date>> flexStockList = new ArrayList<>();
    PortfolioManager portManager = new PortfolioManagerImpl(pers);
    portManager.portFlexBuilder("My Portfolio");

    assertEquals("My Portfolio", portManager.getPortfolioNames()[0]);
    assertEquals("My Portfolio", portManager.getFlexPortfolioNames()[0]);
  }

  @Test
  public void testGetSimplePortfolioValue() {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    StringBuilder log = new StringBuilder();
    controller.API mockA = new MockAPI(log);
    ArrayList<String> tickerList = new ArrayList<>(Arrays.asList("GOOG"));
    ArrayList<Float> floatList = new ArrayList<>(Arrays.asList((float) 10.00));

    PortfolioManager portManager = new PortfolioManagerImpl(pers);
    portManager.portBuilder(tickerList, floatList, "My Portfolio");
    float[] output = new float[0];
    try {
      output = portManager.getPortfolioValue("My Portfolio", formatter.parse("2012-05-24"), mockA);
    } catch (IOException e) {
      fail();
    } catch (ParseException e) {
      fail();
    }
    assertEquals(100, output[0], 0.001);
  }

  @Test
  public void testGetFlexPortfolioValue() throws ParseException {
    StringBuilder log = new StringBuilder();
    controller.API mockA = new MockAPI(log);
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    Date d1 = formatter.parse("01-01-2018");
    String ticker = "GOOG";
    Float count = (float) 100;
    PortfolioManager flex = new PortfolioManagerImpl(pers);
    flex.portFlexBuilder("My Portfolio");
    flex.editFlexPortfolio("My Portfolio", ticker, count, d1);

    float[] output = new float[0];
    try {
      output = flex.getPortfolioValue("My Portfolio", formatter.parse("2012-05-24"), mockA);
    } catch (IOException e) {
      fail();
    } catch (ParseException e) {
      fail();
    }
    assertEquals(100, output[0], 0.001);
  }

  @Test
  public void testReadWriteSimplePortfolio() {
    ArrayList<String> tickerList = new ArrayList<>(Arrays.asList("GOOG", "AAPL", "MSFT"));
    ArrayList<Float> floatList = new ArrayList<>(Arrays.asList((float) 10.00,
        (float) 11.00, (float) 14.00));

    PortfolioManager portManager = new PortfolioManagerImpl(pers);
    portManager.portBuilder(tickerList, floatList, "My Portfolio");
    File file = new File("My Portfolio.csv");
    if (file.exists()) {
      file.delete();
    }

    try {
      portManager.savePortfolio("My Portfolio");
    } catch (Exception e) {
      fail();
    }

    PortfolioManager portManager2 = new PortfolioManagerImpl(pers);
    try {
      portManager2.readPortfolioFile("My Portfolio.csv");
    } catch (Exception e) {
      fail();
    }

    assertEquals("GOOG", portManager2.getTickers("My Portfolio")[0]);
    assertEquals("AAPL", portManager2.getTickers("My Portfolio")[1]);
    assertEquals("MSFT", portManager2.getTickers("My Portfolio")[2]);

    assertEquals((int) 10.00, portManager2.getCounts("My Portfolio")[0], 0.0001);
    assertEquals((int) 11.00, portManager2.getCounts("My Portfolio")[1], 0.0001);
    assertEquals((int) 14.00, portManager2.getCounts("My Portfolio")[2], 0.0001);

    tickerList = new ArrayList<>(Arrays.asList("GOOG", "AAPL", "MSFT"));
    floatList = new ArrayList<>(Arrays.asList((float) 10.20,
        (float) 11.00, (float) 14.00));

    portManager = new PortfolioManagerImpl(pers);
    portManager.portBuilder(tickerList, floatList, "My Portfolio");
    file = new File("My Portfolio2.csv");
    if (file.exists()) {
      file.delete();
    }

    boolean passed = false;
    portManager2 = new PortfolioManagerImpl(pers);
    try {
      portManager2.readPortfolioFile("My Portfolio2.csv");
    } catch (Exception e) {
      passed = true;
    }

    if (!passed) {
      fail();
    }
  }

  @Test
  public void testReadWriteFlexPortfolio() throws ParseException {
    StringBuilder log = new StringBuilder();
    controller.API mockA = new MockAPI(log);
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    Date d1 = formatter.parse("01-01-2018");
    String ticker = "GOOG";
    Float count = (float) 100;
    PortfolioManager flex = new PortfolioManagerImpl(pers);
    flex.portFlexBuilder("My Portfolio");
    flex.editFlexPortfolio("My Portfolio", ticker, count, d1);

    File file = new File("My Portfolio.csv");
    if (file.exists()) {
      file.delete();
    }

    try {
      flex.savePortfolio("My Portfolio");
    } catch (Exception e) {
      fail();
    }

    PortfolioManager flex2 = new PortfolioManagerImpl(pers);
    try {
      flex2.readPortfolioFile("My Portfolio.csv");
    } catch (Exception e) {
      fail();
    }

    assertEquals("GOOG", flex2.getTickers("My Portfolio")[0]);
    assertEquals((int) 100.00, flex2.getCounts("My Portfolio")[0], 0.0001);
    assertEquals("01-01-2018", formatter.format(flex2.getDates("My Portfolio")[0]));

    ticker = "GOOG";
    count = (float) 100;
    d1 = formatter.parse("01-01-2018");

    flex = new PortfolioManagerImpl(pers);
    flex.portFlexBuilder("My Portfolio");
    flex.editFlexPortfolio("My Portfolio", ticker, count, d1);
    file = new File("My Portfolio2.csv");
    if (file.exists()) {
      file.delete();
    }

    boolean passed = false;
    flex2 = new PortfolioManagerImpl(pers);
    try {
      flex2.readPortfolioFile("My Portfolio2.csv");
    } catch (Exception e) {
      passed = true;
    }

    if (!passed) {
      fail();
    }
  }

  @Test
  public void validateTickerTest() throws IOException {
    PortfolioManager portManager = new PortfolioManagerImpl(pers);
    boolean trueBool = portManager.validateTicker("GOOG");
    boolean falseBool = portManager.validateTicker("GEEG");
    if (!trueBool) {
      fail();
    }
    if (falseBool) {
      fail();
    }
  }

  @Test
  public void validateDateTickerTest() throws IOException {
    PortfolioManager portManager = new PortfolioManagerImpl(pers);
    boolean trueBool = portManager.validateTicker("GOOG");
    boolean falseBool = portManager.validateTicker("GEEG");
    if (!trueBool) {
      fail();
    }
    if (falseBool) {
      fail();
    }
  }

  @Test
  public void testCheckFlexEdit() throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    Date d1 = formatter.parse("01-01-2018");
    String ticker = "GOOG";
    Float count = (float) 100;
    PortfolioManager flex = new PortfolioManagerImpl(pers);
    flex.portFlexBuilder("My Portfolio");
    flex.editFlexPortfolio("My Portfolio", ticker, count, d1);

    Date checkDate = formatter.parse("01-01-2019");
    boolean res = flex.checkFlexEdit("My Portfolio", "GOOG", 200, checkDate);
    assertEquals(false, res);

    Date checkDate1 = formatter.parse("01-01-2017");
    boolean res1 = flex.checkFlexEdit("My Portfolio", "GOOG", 50, checkDate1);
    assertEquals(false, res1);

    Date checkDate2 = formatter.parse("01-01-2019");
    boolean res2 = flex.checkFlexEdit("My Portfolio", "GOOG", 50, checkDate2);
    assertEquals(true, res2);

    Date checkDate3 = formatter.parse("01-01-2017");
    boolean res3 = flex.checkFlexEdit("My Portfolio", "GOOG", 200, checkDate3);
    assertEquals(false, res3);

  }

  @Test
  public void testEditFlexPortfolio() throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    Date d1 = formatter.parse("01-01-2018");
    Date d2 = formatter.parse("01-31-2019");
    Date d3 = formatter.parse("11-11-2020");

    String[] tickerList = new String[3];
    tickerList[0] = "GOOG";
    tickerList[1] = "AAPL";
    tickerList[2] = "MSF";
    Float[] floatList = new Float[3];
    floatList[0] = (float) 10.20;
    floatList[1] = (float) 11.20;
    floatList[2] = (float) 14.80;
    Date[] dateList = new Date[3];
    dateList[0] = d1;
    dateList[1] = d2;
    dateList[2] = d3;

    PortfolioManager portManager = new PortfolioManagerImpl(pers);
    portManager.portFlexBuilder("My Portfolio");

    for (int i = 0; i < tickerList.length; i++) {
      portManager.editFlexPortfolio("My Portfolio", tickerList[i], floatList[i], dateList[i]);
    }

    assertEquals("GOOG", portManager.getTickers("My Portfolio")[0]);
    assertEquals("AAPL", portManager.getTickers("My Portfolio")[1]);
    assertEquals("MSF", portManager.getTickers("My Portfolio")[2]);

    assertEquals((float) 10.20, portManager.getCounts("My Portfolio")[0], 0.0001);
    assertEquals((float) 11.20, portManager.getCounts("My Portfolio")[1], 0.0001);
    assertEquals((float) 14.80, portManager.getCounts("My Portfolio")[2], 0.0001);

    assertEquals("01-01-2018", formatter.format(portManager.getDates("My Portfolio")[0]));
    assertEquals("01-31-2019", formatter.format(portManager.getDates("My Portfolio")[1]));
    assertEquals("11-11-2020", formatter.format(portManager.getDates("My Portfolio")[2]));

  }

  @Test
  public void testGetCostBasis() throws ParseException, IOException {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date d1 = formatter.parse("01-01-2018");
    String name = "My Portfolio";
    String ticker = "GOOG";
    Float count = (float) 100;
    StringBuilder log = new StringBuilder();
    controller.API mockA = new MockAPI(log);

    PortfolioManager flex = new PortfolioManagerImpl(pers);
    flex.portFlexBuilder("My Portfolio");
    flex.editFlexPortfolio("My Portfolio", ticker, count, d1);
    float[] res = flex.getCostBasis(name, formatter.parse("01-01-2019"), mockA);

    assertEquals("getPrices method called ", log.toString());
    assertEquals((float) 100, res[0], 0.00001);

  }

  @Test
  public void testPortfolioPerformance() throws ParseException, IOException {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date[] d1 = new Date[1];
    d1[0] = formatter.parse("01-01-2018");
    String name = "My Portfolio";
    String ticker = "GOOG";
    Float count = (float) 100;
    StringBuilder log = new StringBuilder();
    controller.API mockA = new MockAPI(log);

    PortfolioManager flex = new PortfolioManagerImpl(pers);
    flex.portFlexBuilder("My Portfolio");
    flex.editFlexPortfolio("My Portfolio", ticker, count, d1[0]);
    float[] res = flex.portfolioPerformance(name, d1, mockA);

    assertEquals("getPrices method called ", log.toString());
    assertEquals((float) 10000, res[0], 0.00001);
  }

  @Test
  public void testSetGetCommission() {
    PortfolioManager flex = new PortfolioManagerImpl(pers);
    flex.portFlexBuilder("My Portfolio");
    flex.setCommissionFee((float) 100.10);
    assertEquals((float) 100.10, flex.getCommissionFee(), 0.00001);
  }

  @Test
  public void strategyTest() throws ParseException {
    Readable in = new StringReader("1\n \n");
    StringBuilder log = new StringBuilder();
    API mockA = new MockAPI(log);
    PortfolioManager p = new PortfolioManagerImpl(pers);
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    p.portFlexBuilder("dummyTest");
    p.editFlexPortfolio("dummyTest", "AAPL", 10f,
        formatter.parse("2016-01-01"));

    Date s = formatter.parse("2016-01-01");
    Date e = formatter.parse("2017-01-01");
    ArrayList<Stock<String, Float>> list = new ArrayList<>();
    list.add(new Stock<>("AAPL", 100f));
    Strategy strat = new DCAStrategy(list, s, e, 60);

    try {
      p.savePortfolio("dummyTest");
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    p = new PortfolioManagerImpl(pers);
    try {
      p.readPortfolioFile("dummyTest.csv");
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    assertEquals("AAPL", p.getTickers("dummyTest")[0]);
    assertEquals(10, p.getCounts("dummyTest")[0], 0.01);
    assertEquals("2016-01-01", formatter.format(p.getDates("dummyTest")[0]));

  }
}
