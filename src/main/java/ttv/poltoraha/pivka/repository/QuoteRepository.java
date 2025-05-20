package ttv.poltoraha.pivka.repository;

import org.springframework.data.repository.CrudRepository;
import ttv.poltoraha.pivka.entity.Quote;

import java.util.Optional;

public interface QuoteRepository extends CrudRepository<Quote, Integer> {
    public Optional<Quote> findById(int id);
}
