package controller;

import model.Portfolio;

import java.io.IOException;
import java.text.ParseException;

/**
 * This interface describes a persistence object to broaden possible forms of persistence. Changes:
 */
public interface PersistenceInterface {

  /**
   * Saves a simple portfolio to disc as a CSV.
   *
   * @param simplePort the portfolio to save as a CSV
   * @throws IOException if there is trouble writing the file
   */
  public void saveSimpleCSV(Portfolio simplePort) throws IOException;

  /**
   * Saves a flexible portfolio to disc as a CSV.
   *
   * @param flexPort the flexible portfolio
   * @throws IOException if there is an issue writing the file
   */
  public void saveFlexCSV(Portfolio flexPort) throws IOException;

  /**
   * Loads in a portfolio from the disc.
   *
   * @param filename the path, name, and extension of the file in question
   * @return the portfolio being loaded
   * @throws IOException    if there is difficulty reading the file
   * @throws ParseException if there is an issue reading the file, or an item read is invalid
   */
  public Portfolio loadCSV(String filename) throws IOException, ParseException;
}
