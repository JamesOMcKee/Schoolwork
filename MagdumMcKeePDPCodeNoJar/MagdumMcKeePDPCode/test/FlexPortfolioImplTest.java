import model.FlexPortfolioImpl;
import model.Portfolio;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * This is a JUnit test for the FlexPortfolioImplTest class.
 */
public class FlexPortfolioImplTest {

  @Test
  public void testGetPortfolioName() {
    Portfolio port = new FlexPortfolioImpl("FlexPort");
    assertEquals("FlexPort", port.getPortfolioName());
  }

  @Test
  public void testGetTickerCountDate() {
    Portfolio port = new FlexPortfolioImpl("FlexPort");
    assertEquals("FlexPort", port.getPortfolioName());
  }

  @Test
  public void testCheckEdit() throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    Date d1 = formatter.parse("01-01-2018");
    String ticker = "GOOG";
    Float count = (float) 100;
    FlexPortfolioImpl flex = new FlexPortfolioImpl("My Portfolio");
    flex.addFlexStock(ticker, count, d1);

    Date checkDate = formatter.parse("01-01-2019");
    boolean res = flex.checkEdit("GOOG", 200, checkDate);
    assertEquals(false, res);

    Date checkDate1 = formatter.parse("01-01-2017");
    boolean res1 = flex.checkEdit("GOOG", 50, checkDate1);
    assertEquals(false, res1);

    Date checkDate2 = formatter.parse("01-01-2019");
    boolean res2 = flex.checkEdit("GOOG", 50, checkDate2);
    assertEquals(true, res2);

    Date checkDate3 = formatter.parse("01-01-2017");
    boolean res3 = flex.checkEdit("GOOG", 200, checkDate3);
    assertEquals(false, res3);

  }

  @Test
  public void testAddFlexStock() throws ParseException {
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

    FlexPortfolioImpl flex = new FlexPortfolioImpl("My Portfolio");
    for (int i = 0; i < tickerList.length; i++) {
      flex.addFlexStock(tickerList[i], floatList[i], dateList[i]);
    }

    assertEquals("GOOG", flex.getTickers()[0]);
    assertEquals("AAPL", flex.getTickers()[1]);
    assertEquals("MSF", flex.getTickers()[2]);

    assertEquals((float) 10.20, flex.getCounts()[0], 0.0001);
    assertEquals((float) 11.20, flex.getCounts()[1], 0.0001);
    assertEquals((float) 14.80, flex.getCounts()[2], 0.0001);

    assertEquals("01-01-2018", formatter.format(flex.getDates()[0]));
    assertEquals("01-31-2019", formatter.format(flex.getDates()[1]));
    assertEquals("11-11-2020", formatter.format(flex.getDates()[2]));


  }
}