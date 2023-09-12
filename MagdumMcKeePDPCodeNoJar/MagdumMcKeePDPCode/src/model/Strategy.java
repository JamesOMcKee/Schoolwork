package model;

import java.util.ArrayList;
import java.util.Date;

/**
 * An interface to describe the general properties of an investment strategy.
 */
public interface Strategy {

  /**
   * Gets the breakdown of apportioning to each unique stock in the portfolio. The float value
   * represents the percentage given TIMES the amount. Such that the amount being spent is equal to
   * the sum of all the floats in the list.
   *
   * @return the list of ticker percentage pairs.
   */
  ArrayList<Stock<String, Float>> getList();

  /**
   * Gets the amount being spent each period.
   *
   * @return the amount being spent
   */
  Float getAmount();

  /**
   * Gets the date marking the beginning of the strategy.
   *
   * @return the starting date
   */
  Date getStartDate();

  /**
   * Gets the date marking the end of the strategy. Note that 2100-01-01 represents an unending
   * strategy.
   *
   * @return the end date of the strategy
   */
  Date getEndDate();

  /**
   * Gets the interval between buys, given as a number of days.
   *
   * @return the interval
   */
  int getFrequency();
}
