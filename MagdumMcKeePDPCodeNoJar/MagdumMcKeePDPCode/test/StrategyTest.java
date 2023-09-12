import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.DCAStrategy;
import model.Stock;
import model.Strategy;
import org.junit.Test;

/**
 * A JUnit test class for the strategy interface's implementation.
 */
public class StrategyTest {

  private Strategy setup() throws ParseException {
    ArrayList<Stock<String, Float>> list = new ArrayList<>();
    list.add(new Stock<>("GOOG", 400f));
    list.add(new Stock<>("AAPL", 600f));
    list.add(new Stock<>("MSFT", 500f));
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Date d1 = formatter.parse("2010-01-01");
    Date d2 = formatter.parse("2020-01-01");
    return new DCAStrategy(list, d1, d2, 60);
  }

  @Test
  public void listTest() throws ParseException {
    Strategy strat = setup();

    assertEquals("GOOG", strat.getList().get(0).getS());
    assertEquals(500, strat.getList().get(2).getF(), 0.01);
  }

  @Test
  public void amountTest() throws ParseException {
    Strategy strat = setup();
    assertEquals(1500, strat.getAmount(), 0.01);
  }

  @Test
  public void startDateTest() throws ParseException {
    Strategy strat = setup();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    assertEquals(formatter.format(strat.getStartDate()), "2010-01-01");
  }

  @Test
  public void endDateTest() throws ParseException {
    Strategy strat = setup();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    assertEquals(formatter.format(strat.getEndDate()), "2020-01-01");
  }

  @Test
  public void frequencyTest() throws ParseException {
    Strategy strat = setup();
    assertEquals(60, strat.getFrequency());
  }
}
