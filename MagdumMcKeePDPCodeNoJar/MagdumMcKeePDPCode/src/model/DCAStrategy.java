package model;

import java.util.ArrayList;
import java.util.Date;

/**
 * An implementation of the strategy class which represents a dollar cost averaging strategy.
 */
public class DCAStrategy implements Strategy {

  private float amount;
  private Date startDate;
  private Date endDate;
  private int frequency;
  private ArrayList<Stock<String, Float>> list = new ArrayList<>();

  /**
   * A constructor for a strategy.
   *
   * @param lst the list of ticker percentage pairs
   * @param sd the starting date of the strategy
   * @param ed the ending date of the strategy
   * @param freq the interval between buys
   */
  public DCAStrategy(ArrayList<Stock<String, Float>> lst, Date sd, Date ed, int freq) {
    this.list = lst;
    float amt = 0;
    for (int i = 0; i < lst.size(); i++) {
      amt += lst.get(i).getF();
    }
    this.amount = amt;
    this.startDate = sd;
    this.endDate = ed;
    this.frequency = freq;
  }

  @Override
  public ArrayList<Stock<String, Float>> getList() {
    return this.list;
  }

  @Override
  public Float getAmount() {
    return amount;
  }

  @Override
  public Date getStartDate() {
    return startDate;
  }

  @Override
  public Date getEndDate() {
    return endDate;
  }

  @Override
  public int getFrequency() {
    return frequency;
  }
}
