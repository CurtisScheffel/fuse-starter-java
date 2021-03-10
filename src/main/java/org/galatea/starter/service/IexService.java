package org.galatea.starter.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice.Local;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexHistoricalPriceEntity;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.galatea.starter.domain.rpsy.IexHistoricalPriceEntityRspy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * A layer for transformation, aggregation, and business required when retrieving data from IEX.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IexService {

  @NonNull
  private IexClient iexClient;
  @NonNull
  private IexClientCloud iexClientCloud;
  @NonNull
  private IexHistoricalPriceEntityRspy historicalPriceEntityRspy;


  /**
   * Get all stock symbols from IEX.
   *
   * @return a list of all Stock Symbols from IEX.
   */
  public List<IexSymbol> getAllSymbols() {
    return iexClient.getAllSymbols();
  }

  /**
   * Get the last traded price for each Symbol that is passed in.
   *
   * @param symbols the list of symbols to get a last traded price for.
   * @return a list of last traded price objects for each Symbol that is passed in.
   */
  public List<IexLastTradedPrice> getLastTradedPriceForSymbols(final List<String> symbols) {
    if (CollectionUtils.isEmpty(symbols)) {
      return Collections.emptyList();
    } else {
      return iexClient.getLastTradedPriceForSymbols(symbols.toArray(new String[0]));
    }
  }

  /**
   * Get the historical price data for a symbol over a range of dates
   *
   * @param symbol the symbol to get the price data for.
   * @param range the range option.  See "https://iexcloud.io/docs/api/#historical-prices" for
   *     options.
   * @param date the date to get the data from.  YYYYMMDD format.
   * @return a list of historical price objects for the symbol passed in.
   */
  public List<IexHistoricalPrice> getHistoricalPrice(
      final String symbol,
      final String range,
      final String date) {
    log.info("Retrieving historical price data for symbol: {}, range: {}, date: {}", symbol, range,
        date);

    // Declare Variables
    List<IexHistoricalPrice> historicalPrices;
    List<IexHistoricalPriceEntity> historicalEntities;
    LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyyMMdd"));

    // Check if date is in database
    historicalEntities = historicalPriceEntityRspy.findBySymbolIgnoreCaseAndDate(symbol, localDate);
    if (historicalEntities.isEmpty()) {
      log.info("Retrieving data from Iex website and saving to database.");
      historicalPrices = iexClientCloud.getHistoricalPrice(symbol, range, date);
      historicalEntities = IexHistoricalPriceEntity.createFromHistoricalPriceList(historicalPrices);
      historicalPriceEntityRspy.saveAll(historicalEntities);
    } else {
      log.info("Retrieving data from database");
      historicalPrices = IexHistoricalPrice.createFromHistoricalEntityList(historicalEntities);
    }

    return historicalPrices;
  }

  /**
   * Get the historical price data for a symbol over a range of dates
   *
   * @param symbol the symbol to get the price data for.
   * @param range the range option.  See "https://iexcloud.io/docs/api/#historical-prices" for
   *     options.
   * @return a list of historical price objects for the symbol passed in.
   */
  public List<IexHistoricalPrice> getHistoricalPrice(
      final String symbol,
      final String range) {
    log.info("Retrieving historical price data for Symbol: {}, range: {}", symbol, range);

    // Declare Variables

    // This was used to test the methods.  It works! :) -- I'll erase it when I finish.
//    List<IexHistoricalPrice> prices = iexClientCloud.getHistoricalPrice(symbol, range);
//    List<IexHistoricalPriceEntity> historicalEntities =
//        IexHistoricalPriceEntity.createFromHistoricalPriceList(prices);
//    historicalPriceEntityRspy.saveAll(historicalEntities);
//
//
//    LocalDate end = LocalDate.of(2021,3,9);
//    LocalDate start = LocalDate.of(2020,12,1);
//    List<IexHistoricalPriceEntity> test = historicalPriceEntityRspy.findBySymbolAndDateBetween("FB",start, end);
//    log.info(test.toString());
    return iexClientCloud.getHistoricalPrice(symbol, range);
  }

  /**
   * Get the historical price data for a symbol over a range of dates
   *
   * @param symbol the symbol to get the price data for.
   * @return a list of historical price objects for the symbol passed in.
   */
  public List<IexHistoricalPrice> getHistoricalPrice(
      final String symbol) {
    log.info("Retrieving historical price data for Symbol: {}", symbol);

    // Declare Variables
    List<IexHistoricalPrice> historicalPrices;
    List<IexHistoricalPriceEntity> historicalEntities;

    // Check database for symbols
    /*
      I need a more accurate way to test if the database has the symbols rather than isEmpty().
      If I get a specific date and then try to run this method, if that date was within the last
       30 days, it'll just return that.  How can I do this?
     */
    historicalEntities = historicalPriceEntityRspy.findBySymbolIgnoreCase(symbol);
    if (historicalEntities.isEmpty()) {
      log.info("Retrieving data from Iex Website and saving to database");
      historicalPrices = iexClientCloud.getHistoricalPrice(symbol);

      // Create list of entity objects
      historicalEntities = IexHistoricalPriceEntity.createFromHistoricalPriceList(historicalPrices);

      // Save entities in repo
      historicalPriceEntityRspy.saveAll(historicalEntities);
    } else {
      log.info("Retrieving data from database");
      historicalPrices = IexHistoricalPrice.createFromHistoricalEntityList(historicalEntities);
    }

    return (historicalPrices);
  }
}
