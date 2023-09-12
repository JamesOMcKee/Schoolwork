import static org.junit.Assert.assertEquals;

import controller.API;
import controller.GuiCommand;
import controller.PersistenceInterface;
import controller.guicoms.CostBasisGuiCommand;
import controller.guicoms.DollarCostBuyGuiCommand;
import controller.guicoms.DollarCostStartCommand;
import controller.guicoms.EditFlexibleCommandGui;
import controller.guicoms.LoadCommandGui;
import controller.guicoms.PortfolioValueGuiCommand;
import controller.guicoms.SaveGuiCommand;
import controller.guicoms.StrategyGuiCommand;
import controller.guicoms.StrategyValidateInfoGuiCommand;
import controller.guicoms.StrategyValidateStockGuiCommand;
import controller.guicoms.ViewContentsGuiCommand;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.FlexPortfolioImpl;
import model.Portfolio;
import model.PortfolioManager;
import model.Stock;
import org.junit.Test;
import view.GuiInterface;

/**
 * A JUnit test class for testing GUI commands.
 */
public class GuiCommandTests {

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
   * A mock class for the GUI for testing.
   */
  public class MockGui implements GuiInterface {

    private StringBuilder log;
    private Object[] stuff;
    private String portName;


    /**
     * A constructor for the mock GUI.
     *
     * @param log a stringbuilder for logging
     */
    public MockGui(StringBuilder log) {
      this.log = log;
    }

    @Override
    public String printWarning(String line) {
      log.append("printWarning called with " + line + " ");
      return null;
    }

    @Override
    public Object[] getOperationalStuff() {
      log.append("getOperationalStuff called ");
      return this.stuff;
    }

    @Override
    public String getPortfolioName() {
      log.append("getPortfolioName called ");
      return this.portName;
    }

    @Override
    public void setConStuff(Object[] o) {
      this.stuff = o;
      log.append("setConStuff called ");
    }

    @Override
    public void setCurrScreen(String str) {
      log.append("setCurrScreen called ");
    }

    @Override
    public String getCurrScreen() {
      log.append("getCurrScreen called ");
      return null;
    }

    @Override
    public void showWelcomeScreen() {
      log.append("showWelcomeScreen called ");
    }

    @Override
    public void showLoadScreen() {
      log.append("showLoadScreen called ");
    }

    @Override
    public void showBuildScreen() {
      log.append("showBuildScreen called ");
    }

    @Override
    public void showPortfolioScreen() {
      log.append("showPortfolioScreen called ");
    }

    @Override
    public void showSaveScreen() {
      log.append("showSaveScreen called ");
    }

    @Override
    public void printLine(String line) {
      log.append("printLine called ");
    }

    @Override
    public void printLines(String[] lines) {
      log.append("printLines called ");
    }

    /**
     * A method to change internal variables for testing.
     *
     * @param name the name of the portfolio
     */
    public void setPortName(String name) {
      this.portName = name;
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
  public void costBasisCommandTest() {
    StringBuilder log = new StringBuilder();
    GuiInterface gui = new MockGui(log);
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    GuiCommand cmd = new CostBasisGuiCommand();
    Object[] o = new Object[8];
    o[0] = "2019";
    o[1] = "01";
    o[2] = "01";
    o[3] = "1.00";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getPortfolioName called getOperationalStuff "
        + "called setCommissionFee method called with 1.00 getTickers method called with "
        + "null getCounts method called with null getDates method called with null "
        + "getCostBasis method called with null and Tue Jan 01 00:00:00 EST 2019 printLine "
        + "called getCommissionFee method called getCommissionFee method called "
        + "setConStuff called setCurrScreen called ", log.toString());
  }

  @Test
  public void dollarCostBuyCommandTest() {
    //success
    StringBuilder log = new StringBuilder();
    GuiInterface gui = new MockGui(log);
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    GuiCommand cmd = new DollarCostBuyGuiCommand();
    Object[] o = new Object[6];
    o[0] = "1000";
    o[1] = "2019";
    o[2] = "01";
    o[3] = "01";
    o[4] = "GOOG";
    o[5] = "100";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called getPortfolioName "
        + "called getPricesAfter method called printLine called editFlexPortfolio "
        + "method called with null, GOOG, 1000.0, 01-01-2019 getPortfolioName called "
        + "getTickers method called with null getCounts method called with null "
        + "getDates method called with null setConStuff called setCurrScreen "
        + "called ", log.toString());
    log.delete(0, log.toString().length());

    //bad percentage
    o = new Object[6];
    o[0] = "1000";
    o[1] = "2019";
    o[2] = "01";
    o[3] = "01";
    o[4] = "GOOG";
    o[5] = "100k";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called getPortfolioName "
        + "called printLine called setCurrScreen called ", log.toString());
    log.delete(0, log.toString().length());

    //bad math
    o = new Object[6];
    o[0] = "1000";
    o[1] = "2019";
    o[2] = "01";
    o[3] = "01";
    o[4] = "GOOG";
    o[5] = "110";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called getPortfolioName "
        + "called printLine called setCurrScreen called ", log.toString());
  }

  @Test
  public void dollarCostStartCommandTest() {
    //success
    StringBuilder log = new StringBuilder();
    GuiInterface gui = new MockGui(log);
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    GuiCommand cmd = new DollarCostStartCommand();
    Object[] o = new Object[6];
    o[0] = "1000";
    o[1] = "2019";
    o[2] = "01";
    o[3] = "01";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called getPortfolioName called "
        + "getTickers method called with null setConStuff called setCurrScreen "
        + "called ", log.toString());
    log.delete(0, log.toString().length());

    //bad date
    o = new Object[6];
    o[0] = "1000";
    o[1] = "2asdf019";
    o[2] = "01";
    o[3] = "01";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called printLine "
        + "called setCurrScreen called ", log.toString());
    log.delete(0, log.toString().length());

    //bad amount
    o = new Object[6];
    o[0] = "-1000";
    o[1] = "2019";
    o[2] = "01";
    o[3] = "01";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called "
        + "printLine called setCurrScreen called ", log.toString());
    log.delete(0, log.toString().length());

    //early date
    o = new Object[6];
    o[0] = "1000";
    o[1] = "1998";
    o[2] = "01";
    o[3] = "01";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called "
        + "printLine called setCurrScreen called ", log.toString());
    log.delete(0, log.toString().length());

    //late date
    o = new Object[6];
    o[0] = "1000";
    o[1] = "2025";
    o[2] = "01";
    o[3] = "01";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called "
        + "printLine called setCurrScreen called ", log.toString());
    log.delete(0, log.toString().length());
  }

  @Test
  public void editFlexibleCommandTest() {
    //buy
    StringBuilder log = new StringBuilder();
    GuiInterface gui = new MockGui(log);
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    GuiCommand cmd = new EditFlexibleCommandGui();
    Object[] o = new Object[8];
    o[0] = "Buy Stock";
    o[1] = "GOOG";
    o[2] = "100";
    o[3] = "2019";
    o[4] = "01";
    o[5] = "01";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getPortfolioName called getOperationalStuff "
        + "called validateTicker method called with GOOG validateTicker method called with "
        + "GOOG and 01-01-2019 editFlexPortfolio method called with null, "
        + "GOOG, 100.0, 01-01-2019 printLine called ", log.toString());
    log.delete(0, log.toString().length());

    //sell
    o = new Object[8];
    o[0] = "Sell Stock";
    o[1] = "GOOG";
    o[2] = "100";
    o[3] = "2019";
    o[4] = "01";
    o[5] = "01";

    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getPortfolioName called getOperationalStuff "
        + "called validateTicker method called with GOOG validateTicker method called with "
        + "GOOG and 01-01-2019 checkFlexEdit method called with null, GOOG, 100.0, Tue "
        + "Jan 01 00:00:00 EST 2019 printLine called ", log.toString());
  }

  @Test
  public void portfolioValueCommandTest() {
    //buy
    StringBuilder log = new StringBuilder();
    GuiInterface gui = new MockGui(log);
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    GuiCommand cmd = new PortfolioValueGuiCommand();
    Object[] o = new Object[8];
    o[0] = "2019";
    o[1] = "01";
    o[2] = "01";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getPortfolioName called getOperationalStuff "
        + "called printLine called getTickers method called with null getCounts "
        + "method called with null getDates method called with null getPortfolioValue "
        + "method called with null and Tue Jan 01 00:00:00 EST 2019 "
        + "setConStuff called setCurrScreen called ", log.toString());
  }

  @Test
  public void strategyCommandTest() {
    //success
    StringBuilder log = new StringBuilder();
    GuiInterface gui = new MockGui(log);
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    GuiCommand cmd = new StrategyGuiCommand();
    Object[] o = new Object[10];
    o[0] = "1000";
    o[1] = "100";
    o[2] = "2019";
    o[3] = "01";
    o[4] = "01";
    o[5] = "2020";
    o[6] = "01";
    o[7] = "01";
    o[8] = "GOOG";
    o[9] = "100";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getPortfolioName called getOperationalStuff "
        + "called validateTicker method called with GOOG and 01-01-2019 printLine called "
        + "addStrategy method called updateFromStrategy method called getPortfolioName "
        + "called getTickers method called with null getCounts method called with null "
        + "getDates method called with null setConStuff called setCurrScreen "
        + "called ", log.toString());
    log.delete(0, log.toString().length());

  }

  @Test
  public void strategyValidateInfoCommandTest() {
    //success
    StringBuilder log = new StringBuilder();
    GuiInterface gui = new MockGui(log);
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    GuiCommand cmd = new StrategyValidateInfoGuiCommand();
    Object[] o = new Object[8];
    o[0] = "1000";
    o[1] = "100";
    o[2] = "2019";
    o[3] = "01";
    o[4] = "01";
    o[5] = "2020";
    o[6] = "01";
    o[7] = "01";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called "
        + "getPortfolioName called getTickers method called with null "
        + "setCurrScreen called setConStuff called ", log.toString());
    log.delete(0, log.toString().length());

    //success with no end date
    o = new Object[8];
    o[0] = "1000";
    o[1] = "100";
    o[2] = "2019";
    o[3] = "01";
    o[4] = "01";
    o[5] = "";
    o[6] = "";
    o[7] = "";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called "
        + "getPortfolioName called getTickers method called with "
        + "null setCurrScreen called setConStuff called ", log.toString());
    log.delete(0, log.toString().length());

    //bad start date
    o = new Object[8];
    o[0] = "1000";
    o[1] = "100";
    o[2] = "2019sdf";
    o[3] = "01";
    o[4] = "01";
    o[5] = "2020";
    o[6] = "01";
    o[7] = "01";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called "
        + "printLine called setCurrScreen called ", log.toString());
    log.delete(0, log.toString().length());

    //bad end date
    o = new Object[8];
    o[0] = "1000";
    o[1] = "100";
    o[2] = "2019";
    o[3] = "01";
    o[4] = "01";
    o[5] = "2020okg";
    o[6] = "01";
    o[7] = "01";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called "
        + "printLine called setCurrScreen called ", log.toString());
    log.delete(0, log.toString().length());

    //bad amount
    o = new Object[8];
    o[0] = "1000kk";
    o[1] = "100";
    o[2] = "2019";
    o[3] = "01";
    o[4] = "01";
    o[5] = "2020";
    o[6] = "01";
    o[7] = "01";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called "
        + "printLine called setCurrScreen called ", log.toString());
    log.delete(0, log.toString().length());

    //bad interval
    o = new Object[8];
    o[0] = "1000";
    o[1] = "100kk";
    o[2] = "2019";
    o[3] = "01";
    o[4] = "01";
    o[5] = "2020";
    o[6] = "01";
    o[7] = "01";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff "
        + "called printLine called setCurrScreen called ", log.toString());
    log.delete(0, log.toString().length());

    //dates out of order
    o = new Object[8];
    o[0] = "1000";
    o[1] = "100";
    o[2] = "2020";
    o[3] = "01";
    o[4] = "01";
    o[5] = "2019";
    o[6] = "01";
    o[7] = "01";
    gui.setConStuff(o);
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff "
        + "called printLine called setCurrScreen called ", log.toString());
  }

  @Test
  public void strategyValidateStockCommandTest() {
    //success
    StringBuilder log = new StringBuilder();
    GuiInterface gui = new MockGui(log);
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    Object[] o = new Object[7];
    o[2] = "2020";
    o[3] = "01";
    o[4] = "01";
    o[5] = "GOOG";
    gui.setConStuff(o);
    GuiCommand cmd = new StrategyValidateStockGuiCommand();
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called validateTicker "
        + "method called with GOOG and 01-01-2020 setCurrScreen called ", log.toString());

    //bad ticker
    log.delete(0, log.toString().length());
    o = new Object[7];
    o[2] = "2020";
    o[3] = "01";
    o[4] = "01";
    o[5] = "asdfasdf";
    gui.setConStuff(o);
    cmd = new StrategyValidateStockGuiCommand();
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called validateTicker method "
        + "called with asdfasdf and 01-01-2020 setCurrScreen called ", log.toString());

    //early ticker
    log.delete(0, log.toString().length());
    o = new Object[7];
    o[2] = "2010";
    o[3] = "01";
    o[4] = "01";
    o[5] = "GOOG";
    gui.setConStuff(o);
    cmd = new StrategyValidateStockGuiCommand();
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("setConStuff called getOperationalStuff called validateTicker "
        + "method called with GOOG and 01-01-2010 setCurrScreen called ", log.toString());
  }

  @Test
  public void viewContentsCommandTest() {
    StringBuilder log = new StringBuilder();
    GuiInterface gui = new MockGui(log);
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    GuiCommand cmd = new ViewContentsGuiCommand();
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("getPortfolioName called getTickers method called with null "
        + "getCounts method called with null getDates method called with "
        + "null setConStuff called setCurrScreen called ", log.toString());
  }

  @Test
  public void saveAndLoadCommandTests() {
    StringBuilder log = new StringBuilder();
    GuiInterface gui = new MockGui(log);
    PortfolioManager mockP = new MockPortfolioManager(log);
    API mockA = new MockAPI(log);
    GuiCommand cmd = new SaveGuiCommand();
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("getPortfolioName called savePortfolio method "
        + "called with null printLine called ", log.toString());

    cmd = new LoadCommandGui();
    log.delete(0, log.toString().length());
    cmd.goDoStuff(gui, mockP, mockA);
    assertEquals("getPortfolioName called getPortfolioNames method called "
        + "readPortfolioFile method called with null.csv getPortfolioName called "
        + "getTickers method called with null getCounts method called with null "
        + "getDates method called with null setConStuff called "
        + "setCurrScreen called ", log.toString());
  }
}
