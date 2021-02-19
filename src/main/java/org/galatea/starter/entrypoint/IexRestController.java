package org.galatea.starter.entrypoint;

import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.aspect4log.Log;
import net.sf.aspect4log.Log.Level;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.galatea.starter.domain.IexHistoricalPrice;
import org.galatea.starter.service.IexService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Log(enterLevel = Level.INFO, exitLevel = Level.INFO)
@Validated
@RestController
@RequiredArgsConstructor
public class IexRestController {

  @NonNull
  private IexService iexService;

  /**
   * Exposes an endpoint to get all of the symbols available on IEX.
   *
   * @return a list of all IexStockSymbols.
   */
  @GetMapping(value = "${mvc.iex.getAllSymbolsPath}", produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<IexSymbol> getAllStockSymbols() {
    return iexService.getAllSymbols();
  }

  /**
   * Get the last traded price for each of the symbols passed in.
   *
   * @param symbols list of symbols to get last traded price for.
   * @return a List of IexLastTradedPrice objects for the given symbols.
   */
  @GetMapping(value = "${mvc.iex.getLastTradedPricePath}", produces = {
      MediaType.APPLICATION_JSON_VALUE})
  public List<IexLastTradedPrice> getLastTradedPrice(
      @RequestParam(value = "symbols") final List<String> symbols) {
    return iexService.getLastTradedPriceForSymbols(symbols);
  }

  /**
   * Get the historical price data for a symbol over a range of dates
   *
   * @param symbol the symbol to get the price data for.
   * @param range the range option.  See "https://iexcloud.io/docs/api/#historical-prices" for
   *     options.
   * @param date the date to get the data from.  YYYYMMDD format.
   * @return a historical price object for the symbol passed in.
   */
  @GetMapping(value = "${mvc.iex.getHistoricalPricePath}",
      produces = {MediaType.APPLICATION_JSON_VALUE})
  public List<IexHistoricalPrice> getHistoricalPrice(
      @RequestParam(value = "symbol", required = true) final String symbol,
      @RequestParam(value = "range", required = false) final String range,
      @RequestParam(value = "date", required = false) final String date) {
    if (date == null) {
      if (range == null) {
        return iexService.getHistoricalPrice(symbol);
      } else {
        return iexService.getHistoricalPrice(symbol, range);
      }
    } else {
      return iexService.getHistoricalPrice(symbol, range, date);
    }
  }
}
