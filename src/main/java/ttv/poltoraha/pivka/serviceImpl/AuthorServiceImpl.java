package ttv.poltoraha.pivka.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.dao.dto.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.exception.AuthorAlreadyExistsException;
import ttv.poltoraha.pivka.exception.AuthorNotFoundException;
import ttv.poltoraha.pivka.mapping.AuthorMapper;
import ttv.poltoraha.pivka.repository.AuthorRepository;
import ttv.poltoraha.pivka.service.AuthorService;

import java.util.List;

// Имплементации интерфейсов с бизнес-логикой
@Service
@Transactional
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    // todo как будто надо насрать всякими мапперами
    @Override
    public void create(AuthorDto authorDto) {
        authorRepository.findAuthorByFullName(authorDto.getFullName()).ifPresent(
                author -> {
                    throw new AuthorAlreadyExistsException(String.format(
                            "Author with fullName = %s already exists", authorDto.getFullName())
                    );
                }
        );

        Author savedAuthor = authorMapper.mapFromDtoToEntity(authorDto);
        authorRepository.save(savedAuthor);
    }

    @Override
    public void delete(Integer id) {
        Author obtainedAuthor = getOrThrow(id);
        authorRepository.deleteById(obtainedAuthor.getId());
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
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author with id = " + id + " not found"));
    }
}
