package controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

/**
 * An interface that represents methods to retrieve, validate information using API.
 */

public interface API {

  /**
   * Get the prices of stocks on a certain date from API. If that date has no information, it gets
   * the price of the previous trading day.
   *
   * @param tickerList array of tickers for several stocks
   * @param date date at which price is to be known
   * @return the list of prices of every stock on the certain date or before
   * @throws IOException if there is an issue with the API or reading the validation file
   * @throws ParseException if there is an issue reading the validation file
   */
  public float[] getPrices(String[] tickerList, Date date) throws IOException, ParseException;

  /**
   * Get the prices of stocks on a certain date from API. If that date has no information, it gets
   * the price of the next trading day.
   *
   * @param tickerList array of tickers for several stocks
   * @param date the date to get prices for
   * @return the prices for the stocks on the given date or after
   * @throws IOException if there is an issue with the API or reading the validation file
   * @throws ParseException if there is an issue reading the validation file
   */
  public float[] getPricesAfter(String[] tickerList, Date date) throws IOException, ParseException;


}
