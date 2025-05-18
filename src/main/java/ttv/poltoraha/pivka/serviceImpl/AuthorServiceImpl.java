package ttv.poltoraha.pivka.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.repository.AuthorRepository;
import ttv.poltoraha.pivka.repository.BookRepository;
import ttv.poltoraha.pivka.service.AuthorService;
import ttv.poltoraha.pivka.service.BookService;

import java.util.List;

// Имплементации интерфейсов с бизнес-логикой
@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;

    // todo как будто надо насрать всякими мапперами
    @Override
    public void create(AuthorDto authorDto) {

        if (authorDto.getBookId() != null) {
            bookRepository.findById(authorDto.getBookId())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Book with id = %d not found", authorDto.getBookId())));
        }

        Author mappedAuthor = Author.builder()
                .fullName(authorDto.getFullName())
                .avgRating(authorDto.getAvgRating())
                .build();

        authorRepository.save(mappedAuthor);
    }

    @Override
    public void delete(Integer id) {
        authorRepository.deleteById(id);
    }

    @Override
    public void addBooks(Integer id, List<Book> books) {
        val author = getOrThrow(id);

        author.getBooks().addAll(books);
    }

    @Override
    public void addBook(Integer id, Book book) {
        val author = getOrThrow(id);

        author.getBooks().add(book);
    }

    @Override
    public List<Author> getTopAuthorsByTag(String tag, int count) {
        Pageable pageable = PageRequest.of(0, count);
        val authors = authorRepository.findTopAuthorsByTag(tag);

        return authorRepository.findTopAuthorsByTag(tag, pageable);
    }

    private Author getOrThrow(Integer id) {
        val optionalAuthor = authorRepository.findById(id);
        val author = optionalAuthor.orElse(null);

        if (author == null) {
            throw new RuntimeException("Author with id = " + id + " not found");
        }

        return author;
    }

    public void updateAuthor(Integer authorId, AuthorDto updatedData) {
        Author existingAuthor = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with ID: " + authorId));

        existingAuthor.setFullName(updatedData.getFullName());
        existingAuthor.setAvgRating(updatedData.getAvgRating());

         authorRepository.save(existingAuthor);
    }
}
