import org.junit.Test;

import view.ViewImpl;
import view.ViewInterface;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

/**
 * A JUnit test class to test the view panel.
 */
public class ViewImplTest {
  @Test
  public void testShowWelcomeScreen() {
    OutputStream out = new ByteArrayOutputStream();
    ViewInterface v = new ViewImpl(new PrintStream(out));
    v.showWelcomeScreen();
    assertEquals("\n=================================\n"
            + "    Welcome to 'GROW MONEY'!\n"
            + "=================================\n"
            + "\n"
            + "Please enter a choice number\n"
            + "\n"
            + "1. Load a portfolio\n"
            + "2. Build/Edit a portfolio\n"
            + "3. View a portfolio\n"
            + "4. Save a portfolio\n"
            + "5. Save all portfolios\n"
            + "6. Exit\n", out.toString());
  }

  @Test
  public void testLoadScreen() {
    OutputStream out = new ByteArrayOutputStream();
    ViewInterface v = new ViewImpl(new PrintStream(out));
    v.showLoadScreen();
    assertEquals("\n"
            + "====== Load a portfolio ======\n"
            + "\n"
            + "Please enter a choice number\n"
            + "\n"
            + "1. Enter the portfolio filename\n"
            + "2. Go Back\n", out.toString());
  }

  @Test
  public void testBuildScreen() {
    OutputStream out = new ByteArrayOutputStream();
    ViewInterface v = new ViewImpl(new PrintStream(out));
    v.showBuildScreen();

    assertEquals("\n"
            + "====== Build/Edit a portfolio ======\n"
            + "\n"
            + "Please enter a choice number\n"
            + "\n"
            + "1. Begin building a simple portfolio\n"
            + "2. Begin building a flexible portfolio\n"
            + "3. Edit a flexible portfolio\n"
            + "4. Go Back\n", out.toString());

  }

  @Test
  public void testPortfolioScreen() {
    OutputStream out = new ByteArrayOutputStream();
    ViewInterface v = new ViewImpl(new PrintStream(out));
    v.showPortfolioScreen();
    assertEquals("\n====== View a portfolio ======\n"
            + "\n"
            + "Please enter a choice number\n"
            + "\n"
            + "1. View the stocks list in the portfolio\n"
            + "2. View the value of a portfolio on a certain date\n"
            + "3. View the cost basis of a flexible portfolio\n"
            + "4. View the performance over time for flexible portfolio\n"
            + "5. View the value of a portfolio with manually input prices\n"
            + "6. Go back\n", out.toString());

  }

  @Test
  public void testPrintLine() {
    OutputStream out = new ByteArrayOutputStream();
    ViewInterface v = new ViewImpl(new PrintStream(out));
    v.printLine("PrintLine method");
    assertEquals("PrintLine method\n", out.toString());
  }

  @Test
  public void testPrintLines() {
    OutputStream out = new ByteArrayOutputStream();
    ViewInterface v = new ViewImpl(new PrintStream(out));
    String[] str = new String[2];
    str[0] = "PrintLine1";
    str[1] = "PrintLine2";
    v.printLines(str);
    assertEquals("PrintLine1\nPrintLine2\n", out.toString());
  }

}