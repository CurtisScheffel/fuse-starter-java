package org.galatea.starter.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * This class represents the response from the GET /stock/{symbol}/cart/{range}/{date} historical
 * prices IEX endpoint.  It is used in the getHistoricalPrices function.
 */
@Data
@Builder
public class IexHistoricalPrice {

  private String symbol;
  private BigDecimal close;
  private BigDecimal high;
  private BigDecimal low;
  private BigDecimal open;
  private BigDecimal volume;
  private String date;

  public static List<IexHistoricalPrice> createFromHistoricalEntityList(
      List<IexHistoricalPriceEntity> historicalEntities) {
    List<IexHistoricalPrice> historicalPrices =
        new ArrayList<>();

    for (IexHistoricalPriceEntity entity : historicalEntities) {
      historicalPrices
          .add(new IexHistoricalPrice(
              entity.getSymbol(),
              entity.getClose(),
              entity.getHigh(),
              entity.getLow(),
              entity.getOpen(),
              entity.getVolume(),
              entity.getDateString()));
    }
    return historicalPrices;
  }
}