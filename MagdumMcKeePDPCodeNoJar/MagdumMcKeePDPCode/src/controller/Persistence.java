package controller;

import model.DCAStrategy;
import model.Portfolio;
import model.FlexPortfolioImpl;
import model.Stock;
import model.PortfolioImpl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.Strategy;

/**
 * A specific instance of the persistence interface, focused on read/write to disc.
 */
public class Persistence implements PersistenceInterface {
  DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

  @Override
  public void saveSimpleCSV(Portfolio thisPortfolio) throws IOException {
    String[] tickers = thisPortfolio.getTickers();
    Float[] counts = thisPortfolio.getCounts();
    String portfolioName = thisPortfolio.getPortfolioName();
    try {
      Files.deleteIfExists(Path.of("./" + portfolioName + ".csv"));
    } catch (Exception e) {
      //do nothing
    }

    FileWriter writer = new FileWriter(portfolioName + ".csv");
    for (int i = 0; i < tickers.length; i++) {
      writer.append(tickers[i]);
      writer.append(",");
      writer.append(counts[i].toString());
      writer.append("\n");
    }
    ArrayList<Strategy> strategies = thisPortfolio.getStrategies();

    if (strategies.size() > 0) {
      for (int i = 0; i < strategies.size(); i++) {
        writer.append("Strategy");
        writer.append("\n");
        Strategy thisStrat = strategies.get(i);
        writer.append(formatter.format(thisStrat.getStartDate()));
        writer.append(",");
        writer.append(formatter.format(thisStrat.getEndDate()));
        writer.append(",");
        writer.append(String.valueOf(thisStrat.getFrequency()));
        writer.append("\n");
        ArrayList<Stock<String, Float>> list = thisStrat.getList();
        for (int j = 0; j < list.size(); j++) {
          writer.append(list.get(j).getS());
          writer.append(",");
          writer.append(String.valueOf(list.get(j).getF()));
          writer.append("\n");
        }
      }
    }
    writer.flush();
    writer.close();
  }

  @Override
  public void saveFlexCSV(Portfolio flexPort) throws IOException {
    String[] tickers = flexPort.getTickers();
    Float[] counts = flexPort.getCounts();
    Date[] dates = ((FlexPortfolioImpl) flexPort).getDates();
    String portfolioName = flexPort.getPortfolioName();

    try {
      Files.deleteIfExists(Path.of("./" + portfolioName + ".csv"));
    } catch (Exception e) {
      //do nothing
    }

    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    FileWriter writer = new FileWriter(portfolioName + ".csv");
    for (int i = 0; i < tickers.length; i++) {
      writer.append(tickers[i]);
      writer.append(",");
      writer.append(counts[i].toString());
      writer.append(",");
      writer.append(formatter.format(dates[i]));
      writer.append("\n");
    }

    ArrayList<Strategy> strategies = flexPort.getStrategies();
    if (strategies.size() > 0) {
      for (int i = 0; i < strategies.size(); i++) {
        writer.append("Strategy");
        writer.append("\n");
        Strategy thisStrat = strategies.get(i);
        writer.append(formatter.format(thisStrat.getStartDate()));
        writer.append(",");
        writer.append(formatter.format(thisStrat.getEndDate()));
        writer.append(",");
        writer.append(String.valueOf(thisStrat.getFrequency()));
        writer.append("\n");
        ArrayList<Stock<String, Float>> list = thisStrat.getList();
        for (int j = 0; j < list.size(); j++) {
          writer.append(list.get(j).getS());
          writer.append(",");
          writer.append(String.valueOf(list.get(j).getF()));
          writer.append("\n");
        }
      }
    }
    writer.flush();
    writer.close();
  }

  @Override
  public Portfolio loadCSV(String filename) throws IOException, ParseException {
    String name = filename.substring(0, filename.length() - 4);
    ArrayList<String> tickerList = new ArrayList<>();
    ArrayList<Float> floatList = new ArrayList<>();
    ArrayList<Date> dateList = new ArrayList<>();

    ArrayList<ArrayList<String>> strategyTickers = new ArrayList<>();
    ArrayList<ArrayList<Float>> strategyCounts = new ArrayList<>();
    ArrayList<Date> startDates = new ArrayList<>();
    ArrayList<Date> endDates = new ArrayList<>();
    ArrayList<Integer> frequencies = new ArrayList<>();
    ArrayList<Strategy> strategies = new ArrayList<>();
    int strategyCount = 0;

    BufferedReader reader = new BufferedReader(new FileReader("./" + filename));

    String row = reader.readLine();

    boolean strategy = false;
    while (row != null) {
      if (!strategy) {
        String[] elements = row.split(",");
        if (elements[0].equals("Strategy")) {
          strategy = true;
          continue;
        }

        if (!(elements.length == 2 || elements.length == 3)) {
          throw new RuntimeException("File not properly formatted. Please ensure there"
              + "are no headers and only one string and one value per line.");
        }

        if (elements.length == 2) {
          try {
            float newFloat = Float.parseFloat(elements[1]);
          } catch (Exception e) {
            //do nothing
          }
          tickerList.add(elements[0]);
          floatList.add(Float.parseFloat(elements[1]));
          row = reader.readLine();
        } else {
          try {
            float newFloat = Float.parseFloat(elements[1]);
          } catch (Exception e) {
            //do nothing
          }
          DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
          tickerList.add(elements[0]);
          floatList.add(Float.parseFloat(elements[1]));
          dateList.add(format.parse(elements[2]));
          row = reader.readLine();
        }
      } else {
        strategyCount++;
        row = reader.readLine();
        String[] elements = row.split(",");

        ArrayList<String> theseTickers = new ArrayList<>();
        ArrayList<Float> theseCounts = new ArrayList<>();
        startDates.add(formatter.parse(elements[0]));
        endDates.add(formatter.parse(elements[1]));
        frequencies.add(Integer.parseInt(elements[2]));
        row = reader.readLine();

        while (row != null && !row.split(",")[0].equals("Strategy")) {
          elements = row.split(",");

          theseTickers.add(elements[0]);
          theseCounts.add(Float.parseFloat(elements[1]));
          row = reader.readLine();
        }
        strategyTickers.add(theseTickers);
        strategyCounts.add(theseCounts);
      }
    }

    reader.close();

    if (dateList.isEmpty() && !tickerList.isEmpty()) {
      ArrayList<Stock<String, Float>> finalList = new ArrayList<>();
      for (int i = 0; i < tickerList.size(); i++) {
        finalList.add(new Stock<>(tickerList.get(i), floatList.get(i)));
      }
      PortfolioImpl newPort = PortfolioImpl.builder().build(finalList, name);
      for (int i = 0; i < strategyCount; i++) {
        ArrayList<Stock<String, Float>> list = new ArrayList<>();
        for (int j = 0; j < strategyTickers.get(i).size(); j++) {
          list.add(new Stock<>(strategyTickers.get(i).get(j), strategyCounts.get(i).get(j)));
        }
        Strategy strat = new DCAStrategy(list, startDates.get(i),
            endDates.get(i), frequencies.get(i));
        newPort.addStrategy(strat);
      }
      return newPort;
    } else {
      FlexPortfolioImpl newPort = new FlexPortfolioImpl(name);
      for (int i = 0; i < tickerList.size(); i++) {
        if (floatList.get(i) < 0) {
          if (!newPort.checkEdit(tickerList.get(i), Math.abs(floatList.get(i)), dateList.get(i))) {
            throw new RuntimeException("Cannot load invalid flexible portfolios.");
          }
        }
        newPort.addFlexStock(tickerList.get(i), floatList.get(i), dateList.get(i));
      }
      for (int i = 0; i < strategyCount; i++) {
        ArrayList<Stock<String, Float>> list = new ArrayList<>();
        for (int j = 0; j < list.size(); j++) {
          list.add(new Stock<>(strategyTickers.get(i).get(j), strategyCounts.get(i).get(j)));
        }

        Strategy strat = new DCAStrategy(list, startDates.get(i),
            endDates.get(i), frequencies.get(i));
        newPort.addStrategy(strat);
      }
      return newPort;
    }
  }
}