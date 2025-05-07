package ttv.poltoraha.pivka.repository;

import org.springframework.data.repository.CrudRepository;
import ttv.poltoraha.pivka.entity.Quote;

public interface QuoteRepository extends CrudRepository<Quote, Integer> {}