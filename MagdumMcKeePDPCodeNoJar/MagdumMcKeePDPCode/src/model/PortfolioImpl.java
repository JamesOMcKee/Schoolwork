package model;

import java.util.ArrayList;

/**
 * A class representing the composition
 * of a portfolio.
 */


public class PortfolioImpl implements Portfolio {
  private final ArrayList<Stock<String, Float>> stockList;
  private final String portfolioName;
  private ArrayList<Strategy> strat = new ArrayList<>();

  /**
   * This constructor is used by the builder() method to
   * create the portfolio.
   *
   * @param tempStockList the list of stocks
   * @param name          name of the portfolio
   */
  private PortfolioImpl(ArrayList<Stock<String, Float>> tempStockList, String name) {
    stockList = tempStockList;
    portfolioName = name;
  }

  /**
   * This method creates and returns the object that builds the final portfolio.
   *
   * @return the object that builds the final portfolio
   */
  public static PortfolioBuilder builder() {
    return new PortfolioBuilder();
  }

  /**
   * A static class used for building portfolios.
   */

  public static final class PortfolioBuilder {
    private ArrayList<Stock<String, Float>> tempStockList;
    private String tempName = "";

    /**
     * Create a final portfolio after all the stocks information
     * has been entered and portfolio preparation is Done.
     *
     * @param list list of stocks entered
     * @param name name of the portfolio
     * @return a finalized portfolio
     */
    public PortfolioImpl build(ArrayList<Stock<String, Float>> list, String name) {
      tempName = name;
      tempStockList = list;
      return new PortfolioImpl(tempStockList, tempName);
    }

  }


  @Override
  public String getPortfolioName() {
    return portfolioName;
  }

  @Override
  public String[] getTickers() {
    int size = stockList.size();
    String[] tickers = new String[size];
    for (int i = 0; i < size; i++) {
      tickers[i] = stockList.get(i).getS();
    }
    return tickers;
  }

  @Override
  public Float[] getCounts() {
    int size = stockList.size();
    Float[] counts = new Float[size];
    for (int i = 0; i < size; i++) {
      counts[i] = stockList.get(i).getF();
    }
    return counts;
  }

  @Override
  public ArrayList<Strategy> getStrategies() {
    return strat;
  }

  @Override
  public void addStrategy(Strategy strategy) {
    strat.add(strategy);
  }
}
