import model.Stock;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test class for testing
 * new object of type Stock.
 */
public class StockTest {

  @Test
  public void testStockBuilder() {
    Stock<String, Float> stock = new Stock<>("GOOG", (float) 10.20);
    assertEquals("GOOG", stock.getS());
    assertEquals((float) 10.20, stock.getF(), 0.0001);
  }

}
