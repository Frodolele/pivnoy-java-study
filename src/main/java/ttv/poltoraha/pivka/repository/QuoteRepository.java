package ttv.poltoraha.pivka.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.Quote;
import ttv.poltoraha.pivka.entity.Reader;

import java.util.Optional;

@Repository
public interface QuoteRepository extends CrudRepository<Quote, Integer> {
}
