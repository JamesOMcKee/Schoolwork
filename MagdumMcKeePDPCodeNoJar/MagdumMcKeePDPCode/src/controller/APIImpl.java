package controller;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A class representing methods to
 * retrieve and validate portfolio
 * information using API.
 */

public class APIImpl implements API {
  @Override
  public float[] getPrices(String[] tickerList, Date date) throws IOException, ParseException {
    String apiKey = "4U3NNSG5OHR1CBIG";
    URL url = null;
    float[] out = new float[tickerList.length];
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    date = format.parse(format.format(date));

    for (int i = 0; i < tickerList.length; i++) {
      try {
        url = new URL("https://alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol="
                + tickerList[i] + "&outputsize=full&apikey=" + apiKey + "&datatype=csv");
      } catch (MalformedURLException e) {
        throw new RuntimeException("There was an error retrieving that information from the API.");
      }
      if (!validateTicker(tickerList[i])) {
        out[i] = 0;
        continue;
      }
      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = reader.readLine();

        while (line != null) {
          String[] elements = line.split(",");

          Date rowDate = null;

          try {
            rowDate = format.parse(elements[0]);
          } catch (Exception e) {
            line = reader.readLine();
            continue;
          }

          if (rowDate.compareTo(date) < 1) {
            out[i] = Float.parseFloat(elements[5]);
            break;
          }
          line = reader.readLine();
        }

      } catch (IOException e) {
        throw new IOException("There was difficulty reading the input stream for " + tickerList[i]);
      }
    }
    return out;
  }

  @Override
  public float[] getPricesAfter(String[] tickerList, Date date) throws IOException, ParseException {
    String apiKey = "4U3NNSG5OHR1CBIG";
    URL url = null;
    float[] out = new float[tickerList.length];
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    date = format.parse(format.format(date));

    for (int i = 0; i < tickerList.length; i++) {
      try {
        url = new URL("https://alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol="
            + tickerList[i] + "&outputsize=full&apikey=" + apiKey + "&datatype=csv");
      } catch (MalformedURLException e) {
        throw new RuntimeException("There was an error retrieving that information from the API.");
      }
      if (!validateTicker(tickerList[i])) {
        out[i] = 0;
        continue;
      }
      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line = reader.readLine();

        String[] previousElements = null;

        while (line != null) {
          String[] elements = line.split(",");
          if (previousElements == null) {
            previousElements = elements;
          }

          Date rowDate = null;

          try {
            rowDate = format.parse(elements[0]);
          } catch (Exception e) {
            line = reader.readLine();
            continue;
          }

          if (rowDate.compareTo(date) == 0) {
            out[i] = Float.parseFloat(elements[5]);
            break;
          } else if (rowDate.compareTo(date) < 0) {
            out[i] = Float.parseFloat(previousElements[5]);
            break;
          } else {
            previousElements = line.split(",");
          }
          line = reader.readLine();
        }

      } catch (IOException e) {
        throw new IOException("There was difficulty reading the input stream for " + tickerList[i]);
      }
    }
    return out;
  }

  private boolean validateTicker(String ticker) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader("./Full Ticker List.csv"));
    String row = reader.readLine();

    while (row != null) {
      String[] elements = row.split(",");
      if (elements[0].equals(ticker)) {
        return true;
      }
      row = reader.readLine();
    }
    reader.close();
    return false;
  }
}

