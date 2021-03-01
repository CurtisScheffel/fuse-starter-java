package org.galatea.starter.service;

import java.util.List;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * A Feign Declarative REST Client to access endpoints from the Free and Open IEX API to get market
 * data. This client uses a different url to the other IexClient.
 */
@FeignClient(name = "IEXCloud", url = "${spring.rest.iexCloudPath}")
public interface IexClientCloud {

  /**
   * Get the price data for a stock symbol over a date range. https://iexcloud.io/docs/api/#historical-prices.
   *
   * @param symbol stock symbol to get the price data for.
   * @param range date range parameter.  Options listed at URL above.
   * @param date specific date to get data for YYYYMMDD. (NOTE the date must be within the last
   *     30 days from today)
   * @return an IexHistoricalPrice object corresponding to the requested data.
   */
  @GetMapping("/stock/{symbol}/chart/{range}/{date}?token=${spring.rest.iexApiToken}")
  List<IexHistoricalPrice> getHistoricalPrice(
      @PathVariable(value = "symbol") String symbol,
      @PathVariable(value = "range") String range,
      @PathVariable(value = "date") String date);

  /**
   * Get the price data for a stock symbol over a date range. https://iexcloud.io/docs/api/#historical-prices.
   *
   * @param symbol stock symbol to get the price data for.
   * @param range date range parameter.  Options listed at URL above.
   * @return an IexHistoricalPrice object corresponding to the requested data.
   */
  @GetMapping("/stock/{symbol}/chart/{range}?token=${spring.rest.iexApiToken}")
  List<IexHistoricalPrice> getHistoricalPrice(
      @PathVariable(value = "symbol") String symbol,
      @PathVariable(value = "range") String range);

  /**
   * Get the price data for a stock symbol over a date range. https://iexcloud.io/docs/api/#historical-prices.
   *
   * @param symbol stock symbol to get the price data for.
   * @return an IexHistoricalPrice object corresponding to the requested data.
   */
  @GetMapping("/stock/{symbol}/chart?token=${spring.rest.iexApiToken}")
  List<IexHistoricalPrice> getHistoricalPrice(
      @PathVariable(value = "symbol") String symbol);
}