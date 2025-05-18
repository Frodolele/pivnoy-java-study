package ttv.poltoraha.pivka.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ttv.poltoraha.pivka.entity.Categories;

@Repository
public interface CategoriesRepository  extends CrudRepository<Categories, Integer> {
}
