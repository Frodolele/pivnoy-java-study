package ttv.poltoraha.pivka.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.Chapter;

@Repository
public interface ChapterRepository extends CrudRepository<Chapter, Integer> {
}
