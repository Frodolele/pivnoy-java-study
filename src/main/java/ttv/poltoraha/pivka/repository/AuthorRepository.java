package ttv.poltoraha.pivka.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.Author;

import java.util.List;

// Репозиторий - это интерфейс, позволяющий нам писать изолированные от sql логики запросы в БД
@Repository
public interface AuthorRepository extends CrudRepository<Author, Integer> {
    //    "SELECT * FROM AUTHOR WHERE author.avgRaiting= " + avgRaiting " ;"
    public List<Author> findAllByAvgRating(Double avgRating);

    List<Author> findAll();
    @Query("SELECT DISTINCT a FROM author a JOIN a.books b WHERE b.tags LIKE %:tag% ORDER BY a.avgRating DESC, a.id ASC")
    List<Author> findTopAuthorsByTag(@Param("tag") String tag, Pageable pageable);

    @Query("SELECT DISTINCT a FROM author a JOIN a.books b WHERE b.tags LIKE %:tag% ORDER BY a.avgRating DESC, a.id ASC")
    List<Author> findTopAuthorsByTag(@Param("tag") String tag);
}
