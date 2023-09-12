package model;

/**
 * An interface to introduce new object which stores stocks information containing
 * two components - ticker number and count of stocks.
 * @param <S> the ticker number as string
 * @param <F> the count of stocks as float
 */

public interface StockInterface<S,F> {

  /**
   * Get the first component - ticker number
   * of the new object.
   * @return the first component of object
   */
  public S getS();

  /**
   * Get the second component - count of stocks
   * of the new object.
   * @return the second component of object
   */
  public F getF();

}