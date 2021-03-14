package org.galatea.starter.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * This class represents an entity of the IexHistoricalPrice object.  It will be used to store these
 * objects in an h2 database.
 */

@Data
@Builder
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IexHistoricalPriceEntity {

  @Id
  protected String symbol_date;

  @NonNull
  protected LocalDate date;

  @NonNull
  protected String symbol;

  @NonNull
  protected BigDecimal close;

  @NonNull
  protected BigDecimal high;

  @NonNull
  protected BigDecimal low;

  @NonNull
  protected BigDecimal open;

  @NonNull
  protected BigDecimal volume;

  @NonNull
  String dateString;


  public static List<IexHistoricalPriceEntity> createFromHistoricalPriceList(
      List<IexHistoricalPrice> historicalPrices) {
    List<IexHistoricalPriceEntity> historicalPriceEntities =
        new ArrayList<>();
    String symbol_date;
    LocalDate date;

    for (IexHistoricalPrice price : historicalPrices) {
      symbol_date = price.getSymbol() + price.getDate();
      date = LocalDate.parse(price.getDate());
      historicalPriceEntities
          .add(new IexHistoricalPriceEntity(
              symbol_date,
              date,
              price.getSymbol(),
              price.getClose(),
              price.getHigh(),
              price.getLow(),
              price.getOpen(),
              price.getVolume(),
              price.getDate()));
    }
    return historicalPriceEntities;
  }
}
