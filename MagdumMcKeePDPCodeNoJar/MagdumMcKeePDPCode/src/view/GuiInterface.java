package view;

/**
 * An interface extending the ViewInterface. It supports a GUI with multiple additional commands.
 */
public interface GuiInterface extends ViewInterface {

  /**
   * A method to display a warning to the user.
   *
   * @param line the string containing the warning text
   * @return
   */
  public String printWarning(String line);

  /**
   * A method to retrieve information entered into the GUI by the user.
   *
   * @return an object array with relevant material
   */
  public Object[] getOperationalStuff();

  /**
   * A method to get the name of the portfolio currently relevant to the GUI.
   *
   * @return the portfolio name
   */
  public String getPortfolioName();

  /**
   * A method to send relevant information to the GUI for display.
   *
   * @param o an object array with the relevant info
   */
  public void setConStuff(Object[] o);

  /**
   * A method to help instruct the view as to what's going on.
   *
   * @param str the string identifier for the current screen being shown
   */
  public void setCurrScreen(String str);

  /**
   * A method to retrieve the relevant screen from the GUI for use in controller logic.
   *
   * @return the string identifier for the current screen being shown
   */
  public String getCurrScreen();
}
