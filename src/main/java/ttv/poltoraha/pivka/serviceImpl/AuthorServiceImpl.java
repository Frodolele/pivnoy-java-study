package ttv.poltoraha.pivka.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.repository.AuthorRepository;
import ttv.poltoraha.pivka.service.AuthorService;

import java.util.List;

// Имплементации интерфейсов с бизнес-логикой
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    // todo как будто надо насрать всякими мапперами
    @Override
    @Transactional
    public void create(Author author) {
        authorRepository.save(author);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        authorRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void addBooks(Integer id, List<Book> books) {
        val author = getOrThrow(id);

        author.getBooks().addAll(books);
    }

    @Override
    @Transactional
    public void addBook(Integer id, Book book) {
        val author = getOrThrow(id);

        author.getBooks().add(book);
    }

    @Override
    @Transactional
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
}
