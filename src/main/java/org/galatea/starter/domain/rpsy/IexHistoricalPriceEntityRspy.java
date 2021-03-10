package org.galatea.starter.domain.rpsy;

import java.time.LocalDate;
import java.util.List;
import org.galatea.starter.domain.IexHistoricalPriceEntity;
import org.springframework.data.repository.CrudRepository;

public interface IexHistoricalPriceEntityRspy
    extends CrudRepository<IexHistoricalPriceEntity, String> {

    List<IexHistoricalPriceEntity> findBySymbolIgnoreCase(String symbol);

    List<IexHistoricalPriceEntity> findBySymbolIgnoreCaseAndDateBetween(String symbol, LocalDate startDate, LocalDate endDate);

    List<IexHistoricalPriceEntity> findBySymbolIgnoreCaseAndDate(String symbol, LocalDate date);
}
