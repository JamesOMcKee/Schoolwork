import model.FlexStock;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test class for testing new object of type Stock.
 */
public class FlexStockTest {

  @Test
  public void testFlexStockBuilder() throws ParseException {

    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    Date date = formatter.parse("01-01-2018");
    FlexStock<String, Float, Date> flexStock = new FlexStock<>("GOOG", (float) 10.20, date);
    assertEquals("GOOG", flexStock.getS());
    assertEquals((float) 10.20, flexStock.getF(), 0.0001);
    assertEquals("01-01-2018", formatter.format(flexStock.getD()));
  }

}
