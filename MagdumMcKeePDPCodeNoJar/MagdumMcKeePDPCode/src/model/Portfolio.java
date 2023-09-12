package model;


import java.util.ArrayList;

/**
 * A portfolio interface to define the structure of a portfolio.
 */

public interface Portfolio {


  /**
   * Get the name of portfolio.
   * @return the portfolio name as a string
   */
  String getPortfolioName();

  /**
   * Get the list of ticker numbers in the portfolio.
   * @return string array of ticker numbers
   */
  String[] getTickers();

  /**
   * Get the list of counts of each stock in the portfolio.
   * @return float array of counts of stocks
   */
  Float[] getCounts();

  /**
   * Get the list of strategies held by this portfolio.
   *
   * @return the ArrayList of strategy
   */
  ArrayList<Strategy> getStrategies();

  /**
   * Add a strategy to a portfolio.
   *
   * @param strategy the strategy being added
   */
  void addStrategy(Strategy strategy);

}