package org.galatea.starter.domain.rpsy;

import java.util.List;
import java.util.Optional;
import org.galatea.starter.domain.IexHistoricalPriceEntity;
import org.springframework.data.repository.CrudRepository;

public interface IexHistoricalPriceEntityRspy
    extends CrudRepository<IexHistoricalPriceEntity, String> {

    List<IexHistoricalPriceEntity> findBySymbolIgnoreCase(String symbol);


}
