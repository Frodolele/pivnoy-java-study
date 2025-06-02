package ttv.poltoraha.pivka.app.serviceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ttv.poltoraha.pivka.app.util.DataAuthorUtils;
import ttv.poltoraha.pivka.dao.dto.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.exception.AuthorAlreadyExistsException;
import ttv.poltoraha.pivka.exception.AuthorNotFoundException;
import ttv.poltoraha.pivka.mapping.AuthorMapper;
import ttv.poltoraha.pivka.repository.AuthorRepository;
import ttv.poltoraha.pivka.serviceImpl.AuthorServiceImpl;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

//@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Используйте H2 вместо реальной БД
@Transactional // Обеспечивает откат транзакций после каждого теста
@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;
    @Mock
    private AuthorMapper authorMapper;
    @InjectMocks
    private AuthorServiceImpl serviceUnderTest;

    @Test
    @DisplayName("Test create author functionality")
    public void givenAuthorToCreate_whenCreateNewAuthor_thenRepositoryIsCalled() {
        //given
        AuthorDto authorToCreate = DataAuthorUtils.getLevTolstoyTransientDto();
        Author expectedAuthor = DataAuthorUtils.getLevTolstoyPersisted();

        given(authorRepository.findAuthorByFullName(anyString()))
                .willReturn(Optional.empty());

        given(authorMapper.mapFromDtoToEntity(any(AuthorDto.class)))
                .willReturn(expectedAuthor);

        given(authorRepository.save(any(Author.class)))
                .willReturn(expectedAuthor);
        //when
        serviceUnderTest.create(authorToCreate);
        //then
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    @DisplayName("Test create author with duplicate full name functionality")
    public void givenAuthorToCreateWithDuplicateFullName_whenCreateNewAuthor_thenExceptionIsThrow() {
        //given
        AuthorDto authorToCreate = DataAuthorUtils.getLevTolstoyTransientDto();
        given(authorRepository.findAuthorByFullName(authorToCreate.getFullName()))
                .willReturn(Optional.of(DataAuthorUtils.getLevTolstoyPersisted()));
        //when
        assertThrows(
                AuthorAlreadyExistsException.class, () -> serviceUnderTest.create(authorToCreate)
        );
        //then
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    @DisplayName("Test delete author by id functionality")
    public void givenId_whenDeleteById_thenRepositoryIsCalled() {
        //given
        given(authorRepository.findById(anyInt()))
                .willReturn(Optional.of(DataAuthorUtils.getLevTolstoyPersisted()));
        //when
        serviceUnderTest.delete(1);
        //then
        verify(authorRepository, times(1)).deleteById(anyInt());
    }

    @Test
    @DisplayName("Test delete author by incorrect id functionality")
    public void givenIncorrectId_whenDeleteById_thenExceptionIsThrow() {
        //given
        given(authorRepository.findById(anyInt()))
                .willReturn(Optional.empty());
        //when
        assertThrows(
                AuthorNotFoundException.class, () -> serviceUnderTest.delete(1)
        );
        //then
        verify(authorRepository, never()).deleteById(anyInt());
    }

    @Test
    @DisplayName("Test add book for author by id functionality")
    public void givenBookForAuthorById_whenAddBookByAuthorId_thenBookAddedInListBook() {
        // given
        Author author = spy(Author.builder()
                .id(1)
                .fullName("Лев Толстой")
                .books(new ArrayList<>())
                .build());

        Book book = Book.builder()
                .author(author)
                .article("War and Peace")
                .build();

        given(authorRepository.findById(anyInt()))
                .willReturn(Optional.of(author));

        // when
        serviceUnderTest.addBook(1, book);

        // then
        assertThat(author.getBooks())
                .hasSize(1)
                .containsExactly(book);
        assertThat(book.getAuthor()).isEqualTo(author);
    }

    @Test
    @DisplayName("Test add book for author by incorrect id functionality")
    public void givenBookForAuthorByIncorrectId_whenAddBookByIncorrectAuthorId_thenExceptionIsThrow() {
        //given
        //when
        //then
    }

    @Test
    @DisplayName("Test add books for author by id functionality")
    public void givenBooksForAuthorById_whenAddBooksByAuthorId_thenBooksAddedInListBooks() {
        // given
        Author author = spy(Author.builder()
                .id(1)
                .fullName("Лев Толстой")
                .books(new ArrayList<>())
                .build());

        Book book1 = Book.builder()
                .author(author)
                .article("War and Peace")
                .build();

        Book book2 = Book.builder()
                .author(author)
                .article("Spider man")
                .build();

        Book book3 = Book.builder()
                .author(author)
                .article("Clean code")
                .build();

        List<Book> books = new ArrayList<>(Arrays.asList(book1,book2, book3));

        given(authorRepository.findById(anyInt()))
                .willReturn(Optional.of(author));

        // when
        serviceUnderTest.addBooks(1, books);

        // then
        assertThat(CollectionUtils.isEmpty(author.getBooks())).isFalse();
        assertThat(author.getBooks()).hasSize(3);
    }

    @Test
    @DisplayName("Test add books for author by incorrect id functionality")
    public void givenBooksForAuthorByIncorrectId_whenAddBooksByIncorrectAuthorId_thenExceptionIsThrow() {
        //given
        //when
        //then
    }


}

