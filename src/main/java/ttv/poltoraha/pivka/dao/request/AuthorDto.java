package ttv.poltoraha.pivka.dao.request;

import lombok.Builder;
import lombok.Data;
import ttv.poltoraha.pivka.entity.Book;

import java.util.List;

@Data
@Builder
public class AuthorDto {
    private String fullName;
    private List<Book> books;
}
