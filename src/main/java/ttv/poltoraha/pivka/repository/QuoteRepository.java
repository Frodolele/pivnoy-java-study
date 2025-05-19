package ttv.poltoraha.pivka.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.Quote;

@Repository
public interface QuoteRepository extends CrudRepository<Quote, Integer> {
}
