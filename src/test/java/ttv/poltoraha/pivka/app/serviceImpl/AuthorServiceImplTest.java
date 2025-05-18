package ttv.poltoraha.pivka.app.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.repository.AuthorRepository;
import ttv.poltoraha.pivka.repository.BookRepository;
import ttv.poltoraha.pivka.serviceImpl.AuthorServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static ttv.poltoraha.pivka.app.model.Models.*;
import static ttv.poltoraha.pivka.app.model.Models.getAuthorDto;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Используйте H2 вместо реальной БД
@Transactional // Обеспечивает откат транзакций после каждого теста
public class AuthorServiceImplTest {

    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private AuthorRepository authorRepository;


    private Book book;
    private AuthorDto authorDto;
    private Author authorEntity;

    @BeforeEach
    public void setUp() {

        authorDto = getAuthorDto();
        authorEntity = getAuthor();
    }

    @Test
    public void testCreateAuthor_Success() {
        val beforeUpdateAuthorSize = authorRepository.count();

        authorService.create(authorDto);
        val afterUpdateAuthorSize = authorRepository.count();
        assertNotEquals(beforeUpdateAuthorSize, afterUpdateAuthorSize);

    }

    @Test
    public void testCreateAuthor_BookNotFound() {

        AuthorDto authorDto1 = getAuthorDto();

        authorDto1.setBookId(999);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            authorService.create(authorDto1);
        });
    }

    @Test
    public void testDeleteAuthor() {
        val updateAuthorId = authorRepository.save(authorEntity).getId();

        authorService.delete(updateAuthorId);

        assertFalse(authorRepository.existsById(updateAuthorId));
    }


    @Test
    public void testUpdateAuthor_Success() {
        val authorId = authorRepository.save(authorEntity).getId();

        authorDto.setAvgRating(1.99);
        authorDto.setFullName("Changed-106-Xan");
        authorService.updateAuthor(authorId, authorDto);

        Author updatedAuthor = authorRepository.findById(authorId).orElseThrow();
        assertEquals(updatedAuthor.getAvgRating(), 1.99);
        assertEquals(updatedAuthor.getFullName(), "Changed-106-Xan");
    }

    @Test
    public void testUpdateAuthor_AuthorNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            authorService.updateAuthor(999, authorDto);
        });
    }

}
