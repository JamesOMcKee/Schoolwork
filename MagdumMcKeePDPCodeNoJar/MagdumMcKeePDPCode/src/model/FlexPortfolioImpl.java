package model;

import java.util.ArrayList;
import java.util.Date;

/**
 * A different portfolio implementation that is editable and whose elements contain additional
 * information pertaining to transaction dates.
 */
public class FlexPortfolioImpl implements Portfolio {

  private ArrayList<FlexStock<String, Float, Date>> stockList = new ArrayList<>();
  private final String portfolioName;
  private ArrayList<Strategy> strat = new ArrayList<>();

  /**
   * A constructor for the flexible portfolio. This constructor only takes in a name. The portfolio
   * is allowed to exist otherwise empty.
   *
   * @param portfolioName the name to give the portfolio
   */
  public FlexPortfolioImpl(String portfolioName) {
    this.portfolioName = portfolioName;
  }

  /**
   * Get the name of portfolio.
   *
   * @return the portfolio name as a string
   */
  @Override
  public String getPortfolioName() {
    return portfolioName;
  }

  /**
   * Get the list of ticker numbers in the portfolio.
   *
   * @return string array of ticker numbers
   */
  @Override
  public String[] getTickers() {
    String[] tickers = new String[stockList.size()];
    for (int i = 0; i < stockList.size(); i++) {
      tickers[i] = stockList.get(i).getS();
    }
    return tickers;
  }

  /**
   * Get the list of counts of each stock in the portfolio.
   *
   * @return float array of counts of stocks
   */
  @Override
  public Float[] getCounts() {
    Float[] counts = new Float[stockList.size()];
    for (int i = 0; i < stockList.size(); i++) {
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
    this.strat.add(strategy);
  }

  /**
   * Get the list of purchase dates of each stock in the portfolio.
   *
   * @return date array of purchase dates
   */
  public Date[] getDates() {
    Date[] dates = new Date[stockList.size()];
    for (int i = 0; i < stockList.size(); i++) {
      dates[i] = stockList.get(i).getD();
    }
    return dates;
  }

  /**
   * Ensures that this edit doesn't violate the rules: at all times every sell needs to have
   * supporting buys (buys before the sale, of at least as much as is being sold) or the sale is
   * invalid.
   *
   * @param ticker the ticker of the edit
   * @param count  the count of the edit
   * @param date   the date of the transaction
   * @return a boolean representing the validity of the edit
   */
  public boolean checkEdit(String ticker, float count, Date date) {
    String[] tickers = getTickers();
    Float[] counts = getCounts();
    Date[] dates = getDates();
    float sum = 0;
    for (int i = 0; i < tickers.length; i++) {
      if (tickers[i].equals(ticker)) {
        sum += counts[i];
      }
    }
    if (sum < count) {
      return false;
    }

    sum = 0;
    for (int i = 0; i < tickers.length; i++) {
      if (tickers[i].equals(ticker) && dates[i].compareTo(date) < 0) {
        sum += counts[i];
      }
    }
    return (sum >= count);
  }

  /**
   * A method to add new stock items to the portfolio's list.
   *
   * @param ticker the ticker being added
   * @param count  the count of the stock being added
   * @param date   the date of the transaction
   */
  public void addFlexStock(String ticker, float count, Date date) {
    stockList.add(new FlexStock<>(ticker, count, date));
  }
}
