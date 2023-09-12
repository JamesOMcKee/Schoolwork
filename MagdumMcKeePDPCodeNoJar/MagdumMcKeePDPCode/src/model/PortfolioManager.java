package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import controller.API;

/**
 * An interface containing methods to perform operations on a portfolio.
 */
public interface PortfolioManager {

  /**
   * Build a portfolio from scratch by getting several stocks containing
   * ticker number and stock count from user, and the portfolio name in
   * which the input stocks are to be added.
   *
   * @param tickerList a list of stocks with ticker number
   * @param floatList  a list of counts of stocks
   * @param name       name of the portfolio
   */
  public void portBuilder(ArrayList<String> tickerList, ArrayList<Float> floatList, String name);

  /**
   * Build a flexible portfolio that is editable.
   *
   * @param name the name of the portfolio
   */
  public void portFlexBuilder(String name);

  /**
   * Read a portfolio file in the program.
   *
   * @param filename name of the portfolio file to be read
   * @throws FileNotFoundException if the file is missing or format is incorrect
   */
  public String readPortfolioFile(String filename) throws IOException, ParseException;

  /**
   * Attempts to write the selected portfolio out to a csv. The file will be given the name of
   * the portfolio.
   *
   * @param portfolioName denotes the title of the portfolio to be saved
   * @throws IOException when there's trouble writing the file
   */
  public void savePortfolio(String portfolioName) throws IOException;

  /**
   * Get names of all the portfolios in the system.
   *
   * @return the string array containing names
   *         of portfolios
   */
  public String[] getPortfolioNames();

  /**
   * Get names of all the flexible portfolios in the system.
   *
   * @return the string array containing names
   *         of flexible portfolios
   */
  public String[] getFlexPortfolioNames();

  /**
   * Get the value of a specified portfolio for a certain input date.
   *
   * @param name name of the portfolio
   * @param date certain date at which portfolio value
   *             needs to be known
   * @return the content of portfolio and the total value in
   *         a string array
   * @throws IOException if there is difficulty reading in files
   * @throws ParseException if there is an incorrect entry in a file being read
   */
  public float[] getPortfolioValue(String name, Date date, API api)
      throws IOException, ParseException;

  /**
   * Checks to see if an edit invalidates the rules of the portfolio and returns a boolean.
   *
   * @param name the name of the portfolio being edited
   * @param ticker the ticker of the stock edit
   * @param count the count of the stock edit, negative for sales
   * @param date the date of the stock edit
   * @return a boolean indicating whether the edit is valid
   */
  public boolean checkFlexEdit(String name, String ticker, float count, Date date);

  /**
   * Check whether the ticker is on the approved list of stocks.
   *
   * @param ticker the symbol being checked
   * @return true if the ticker is recognized, false otherwise
   * @throws IOException if there is difficulty reading the reference file
   */
  public boolean validateTicker(String ticker) throws IOException;

  /**
   * Check whether the ticker is on the approved and the given date is after the stock's IPO.
   *
   * @param ticker the symbol being checked
   * @return true if the ticker is recognized, false otherwise
   * @throws IOException if there is difficulty reading the reference file
   * @throws ParseException if there is difficulty reading the date off the reference file
   */
  public boolean validateTicker(String ticker, Date date) throws IOException, ParseException;

  /**
   * Adds an element to the portfolio's stocks list.
   *
   * @param name the name of the portfolio being added to
   * @param ticker the ticker of the stock being added
   * @param count the count of the stock being added, negative for sales
   * @param date the date of the transaction
   * @throws IllegalArgumentException if this edit violates the rules of the portfolio
   */
  public void editFlexPortfolio(String name, String ticker, Float count, Date date)
                                throws IllegalArgumentException;

  /**
   * Takes in a date and api, gets a portfolio, then figures out which stocks to include based
   * on the date. Finally, calls the API to value those stocks on the given date.
   *
   * @param name the name of the portfolio
   * @param date a string of the form "yyyy-MM-dd"
   * @param api the api object that gets the prices
   * @return an array of values representing prices
   * @throws ParseException if the date is not given correctly
   * @throws IOException if the API has issues
   */
  public float[] getCostBasis(String name, Date date, API api)
      throws ParseException, IOException;

  /**
   * Gets the prices for a portfolios stocks based on the dates given, via portfolioValueByDate(),
   * which takes the API object.
   *
   * @param name the portfolio's name
   * @param dates the dates we want prices for
   * @param api the object making API calls
   * @return a float array of prices
   * @throws IOException if the API struggles to receive information
   * @throws ParseException if the API struggles to read what it receives
   */
  public float[] portfolioPerformance(String name, Date[] dates, API api)
      throws IOException, ParseException;

  /**
   * Get the ticker numbers for all stocks in a portfolio.
   *
   * @param name name of the portfolio
   * @return the string array of all tickers in the
   *         specified portfolio
   */
  public String[] getTickers(String name);

  /**
   * Get the count of stocks for all stocks in a portfolio.
   *
   * @param name name of the portfolio
   * @return the float array of all the stock counts
   *         in the specified portfolio
   */
  public Float[] getCounts(String name);

  /**
   * Get the purchased/sold date for all stocks in a portfolio.
   *
   * @param name name of the portfolio
   * @return the date array for all the stocks
   *         in the specified portfolio
   */
  public Date[] getDates(String name) throws IllegalArgumentException;

  /**
   * Accesses and returns the commission fee for cost-basis use.
   */
  public float getCommissionFee();

  /**
   * Sets the commission fee.
   *
   * @param fee the fee
   */
  public void setCommissionFee(float fee);

  /**
   * Allows a strategy to be added to a portfolio.
   *
   * @param portfolioName the portfolio receiving the strategy
   * @param list the list of stocks being invested, with counts = ($amount)*(% for given stock)
   * @param start the starting date of the strategy
   * @param end the ending date of the strategy, 2100-01-01 represents "no end date"
   * @param frequency the interval in days between purchases of these stocks
   */
  public void addStrategy(String portfolioName, ArrayList<Stock<String, Float>> list, Date start,
      Date end, int frequency);

  /**
   * Updates a portfolio using its last given strategy. If there is no strategy in the portfolio,
   * nothing happens.
   *
   * @param portfolioName the portfolio to update
   */
  public void updateFromStrategy(String portfolioName, API api) throws IOException, ParseException;

}


