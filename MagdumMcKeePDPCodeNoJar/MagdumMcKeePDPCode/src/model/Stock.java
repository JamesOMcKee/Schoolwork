package model;

/**
 * A class that defines new object and its methods containing two components - ticker number and
 * count of stocks.
 *
 * @param <S> the ticker number as string
 * @param <F> the count of stocks as float
 */

public class Stock<S, F> implements StockInterface {

  S dataOne;
  F dataTwo;

  @Override
  public S getS() {
    return dataOne;
  }

  @Override
  public F getF() {
    return dataTwo;
  }

  /**
   * A constructor for a stock object. Takes in two pieces of data and stores them.
   *
   * @param newDataOne the first piece of data
   * @param newDataTwo the second piece of data
   */
  public Stock(S newDataOne, F newDataTwo) {
    dataOne = newDataOne;
    dataTwo = newDataTwo;
  }

}