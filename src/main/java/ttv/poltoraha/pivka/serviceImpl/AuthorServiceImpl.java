package ttv.poltoraha.pivka.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    @Override
    public void create(AuthorDto authorDto) {
        logger.debug("Проверка существования автора: {}", authorDto.getFullName());
        authorRepository.findAuthorByFullName(authorDto.getFullName()).ifPresent(
                author -> {
                    throw new AuthorAlreadyExistsException(String.format(
                            "Author with fullName = %s already exists", authorDto.getFullName())
                    );
                }
        );
        logger.debug("Создание нового автора");
        Author savedAuthor = authorMapper.mapFromDtoToEntity(authorDto);
        authorRepository.save(savedAuthor);
        logger.info("Автор сохранен в БД с ID: {}", savedAuthor.getId());
    }

    @Override
    public void delete(Integer id) {
        logger.debug("Проверка существования автора с ID: {}", id);
        Author obtainedAuthor = getOrThrow(id);
        logger.debug("Удаление нового автора");
        authorRepository.deleteById(obtainedAuthor.getId());
        logger.info("Автор удален из БД с ID: {}", id);
    }

    @Override
    public void addBooks(Integer id, List<Book> books) {
        logger.debug("Проверка существования автора с ID: {}", id);
        val author = getOrThrow(id);
        logger.debug("Добавление книг для автора с ID: {}", id);
        author.getBooks().addAll(books);
        logger.info("Книги добавлены в список книг автора с ID: {}", id);
    }

    @Override
    public void addBook(Integer id, Book book) {
        logger.debug("Проверка существования автора с ID: {}", id);
        val author = getOrThrow(id);
        logger.debug("Добавление книги для автора с ID: {}", id);
        author.getBooks().add(book);
        logger.info("Книга добавлены в список книг автора с ID: {}", id);
    }

    @Override
    public List<Author> getTopAuthorsByTag(String tag, int count) {
        Pageable pageable = PageRequest.of(0, count);
        val authors = authorRepository.findTopAuthorsByTag(tag);

        return authorRepository.findTopAuthorsByTag(tag, pageable);
    }

    private Author getOrThrow(Integer id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Не найден автор с ID: {}", id);
                    throw new AuthorNotFoundException("Author with id = " + id + " not found");
                });
    }
}
