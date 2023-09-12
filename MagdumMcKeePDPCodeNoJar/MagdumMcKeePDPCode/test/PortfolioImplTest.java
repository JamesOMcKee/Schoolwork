
import model.Stock;
import model.Portfolio;

import org.junit.Test;

import java.util.ArrayList;

import static model.PortfolioImpl.builder;
import static org.junit.Assert.assertEquals;

/**
 * A JUnit test class for portfolio implementation class.
 */

public class PortfolioImplTest {

  @Test
  public void testPortfolioBuilder() {
    ArrayList<Stock<String, Float>> stockList = new ArrayList<>();
    stockList.add(new Stock<>("GOOG", (float) 10.20));
    stockList.add(new Stock<>("AAPL", (float) 11.20));
    stockList.add(new Stock<>("MSF", (float) 14.80));

    Portfolio port = builder().build(stockList, "My Portfolio");
    assertEquals("My Portfolio", port.getPortfolioName());
    assertEquals("GOOG", port.getTickers()[0]);
    assertEquals("AAPL", port.getTickers()[1]);
    assertEquals("MSF", port.getTickers()[2]);

    assertEquals((float) 10.20, port.getCounts()[0], 0.0001);
    assertEquals((float) 11.20, port.getCounts()[1], 0.0001);
    assertEquals((float) 14.80, port.getCounts()[2], 0.0001);

  }

  @Test
  public void testImmutablePortfolio() {
    ArrayList<Stock<String, Float>> stockList = new ArrayList<>();
    stockList.add(new Stock<>("GOOG", (float) 10.20));
    stockList.add(new Stock<>("AAPL", (float) 11.20));
    stockList.add(new Stock<>("MSF", (float) 14.80));

    Portfolio port = builder().build(stockList, "My Portfolio");

    String portName = port.getPortfolioName();
    portName = "My Port";
    assertEquals("My Portfolio", port.getPortfolioName());

    String[] str = port.getTickers();
    str[0] = "GOO";
    assertEquals("GOOG", port.getTickers()[0]);

    Float[] flts = port.getCounts();
    flts[0] = 10.00f;
    assertEquals((float) 10.20, port.getCounts()[0], 0.00001);

  }

}