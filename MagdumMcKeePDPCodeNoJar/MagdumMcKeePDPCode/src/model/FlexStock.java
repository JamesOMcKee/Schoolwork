package model;

/**
 * A class extending the Stock class, adding one more generic data item.
 *
 * @param <S> the S datatype
 * @param <F> the F datatype
 * @param <D> the D datatype
 */
public class FlexStock<S, F, D> extends Stock<S, F> {

  D dataThree;

  /**
   * A getter for the D data.
   *
   * @return the D date
   */
  public D getD() {
    return dataThree;
  }

  /**
   * A constructor for a flexible stock. This inherits from Stock, adding only a third datatype.
   *
   * @param newDataOne   the S data
   * @param newDataTwo   the F data
   * @param newDataThree the D data
   */
  public FlexStock(S newDataOne, F newDataTwo, D newDataThree) {
    super(newDataOne, newDataTwo);
    this.dataThree = newDataThree;
  }
}
