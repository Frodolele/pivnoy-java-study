package ttv.poltoraha.pivka.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.Maclenak;

import java.util.List;

@Repository
public interface MaclenakReopsitory extends CrudRepository<Maclenak, Integer> {
    List <Maclenak> findAll();
}
