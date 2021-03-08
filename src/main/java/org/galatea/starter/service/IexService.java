package org.galatea.starter.service;

import java.util.Collections;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
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
    return iexClientCloud.getHistoricalPrice(symbol, range, date);
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
    return iexClientCloud.getHistoricalPrice(symbol);
  }
}
