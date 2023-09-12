package view;

/**
 * This interface represents the methods used to interact
 * with the user through the Text-line Interface.
 */

public interface ViewInterface {

  /**
   * Show the welcome screen to user which contains
   * options to work with a portfolio.
   * This method returns nothing.
   */
  void showWelcomeScreen();

  /**
   * Show a screen with options for loading a portfolio
   * file.
   * This method returns nothing.
   */
  void showLoadScreen();

  /**
   * Show a screen with options to build
   * a portfolio.
   * This method returns nothing.
   */
  void showBuildScreen();

  /**
   * Show a screen with options to view
   * a portfolio.
   * This method returns nothing.
   */
  void showPortfolioScreen();

  /**
   * Show the screen to save one or all portfolios.
   */
  void showSaveScreen();

  /**
   * A method to display message sent by the controller
   * to the view panel.
   * This method returns nothing.
   * @param line message to display in String format
   */
  void printLine(String line);

  /**
   * A method to display multiple messages sent by the controller
   * to the view panel.
   * This method returns nothing.
   * @param lines String array of messages to display
   */
  void printLines(String[] lines);

}