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
import ttv.poltoraha.pivka.repository.AuthorRepository;
import ttv.poltoraha.pivka.serviceImpl.AuthorServiceImpl;
import static org.junit.jupiter.api.Assertions.*;
import static ttv.poltoraha.pivka.app.model.Models.getAuthorDto;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class AuthorServiceImplTest {
    @Autowired
    private AuthorServiceImpl authorService;

    @Autowired
    private AuthorRepository authorRepository;

    private Author author;
    private AuthorDto authorDto;

    @BeforeEach
    public void setUp() {
authorDto = getAuthorDto();
    }

    @Test
    public void testCreateAuthor_Success() {

        val beforeCreateAuthorSize = authorRepository.findAll().size();
        authorService.create(authorDto);
        val afterCreateAuthorSize = authorRepository.findAll().size();
        assertNotEquals(beforeCreateAuthorSize, afterCreateAuthorSize);
    }
    @Test
    public void testDeleteAuthor_Success(){

        authorService.create(authorDto);
        val beforeDeleteAuthorSize = authorRepository.findAll().size();
        authorService.delete(11);
        val afterDeleteAuthorSize = authorRepository.findAll().size();
    }
    @Test
    public void testDelete_AuthorNotFound(){
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            authorService.delete(666);
        });
    }
}
