package ttv.poltoraha.pivka.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.controller.AuthorController;
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

    private final Logger logger = LoggerFactory.getLogger(AuthorServiceImpl.class);

    // todo как будто надо насрать всякими мапперами
    @Override
    public void create(AuthorDto authorDto) {
        logger.info("Вход в метод create() для Dto_имя: {}", authorDto.getFullName());

        if (authorDto.getBookId() != null) {
            logger.info("Отправляется запрос к БД: bookRepository.findById() по BookId из Dto: {}", authorDto.getBookId());
            bookRepository.findById(authorDto.getBookId())
                    .orElseThrow(() -> {
                        logger.warn("Книга с id ={} не найдена при создании автора", authorDto.getBookId());
                        return new EntityNotFoundException(String.format("Book with id = %d not found", authorDto.getBookId()));
                    });
            logger.info("Получен ответ от БД: bookRepository.FindById() нашёл книгу по bookId из DTO: {}", authorDto.getBookId());
        }

        Author mappedAuthor = Author.builder()
                .fullName(authorDto.getFullName())
                .avgRating(authorDto.getAvgRating())
                .build();

        logger.info("Отправляется запрос к БД: authorRepository.save() для создание автора с именем: {}", mappedAuthor.getFullName());
        authorRepository.save(mappedAuthor);
        logger.info("Получен ответ от БД: authorRepository.save() автор создан с ID={}", mappedAuthor.getId());
    }

    @Override
    public void delete(Integer id) {
        logger.info("Отправляется запрос к БД: authorRepository.deleteById() с ID: {}", id);
        authorRepository.deleteById(id);
        logger.info("Получен ответ от БД: authorRepository.deleteById() с ID: {} выполнено", id);
    }

    @Override
    public void addBooks(Integer id, List<Book> books) {
        val author = getOrThrow(id);

        logger.info("Пытается добавить в author с ID={} книги [{} книг]", id, books.size());
        author.getBooks().addAll(books);
        logger.info("{} книг успешно добавлено в author с ID={}", books.size(), id);
    }

    @Override
    public void addBook(Integer id, Book book) {
        val author = getOrThrow(id);

        logger.info("Пытается добавить в author с ID={} книгу: {}", id, book.getArticle());
        author.getBooks().add(book);
        logger.info("Книга: [{}] успешно добавлена в author с ID={}",book.getArticle(), id);
    }

    @Override
    public List<Author> getTopAuthorsByTag(String tag, int count) {
        Pageable pageable = PageRequest.of(0, count);
        logger.info("Пытается получить топ {} авторов по тегу: {}", count, tag);
        val authors = authorRepository.findTopAuthorsByTag(tag);
        logger.info("Успешно получен топ {} авторов по тегу: {}", count, tag);
        return authorRepository.findTopAuthorsByTag(tag, pageable);
    }

    private Author getOrThrow(Integer id) {
        logger.info("Отправляется запрос к БД: authorRepository.findById() с ID: {}", id);
        val optionalAuthor = authorRepository.findById(id);
        logger.info("Получен ответ от БД: authorRepository.findById() с ID: {} выполнен", id);
        val author = optionalAuthor.orElse(null);

        logger.info("Проверка author на null");
        if (author == null) {
            logger.warn("author равен null");
            throw new RuntimeException("Author with id = " + id + " not found");
        }
        logger.info("Проверка author на null прошла успешно");

        return author;
    }

    public void updateAuthor(Integer authorId, AuthorDto updatedData) {
        logger.info("Отправляется запрос к БД: authorRepository.findById() с ID: {} на поиск автора", authorId);
        Author existingAuthor = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found with ID: " + authorId));

        logger.info("Получен ответ от БД: authorRepository.findById() с ID: {} на поиск автора. Успешно", authorId);
        existingAuthor.setFullName(updatedData.getFullName());
        existingAuthor.setAvgRating(updatedData.getAvgRating());

        logger.info("Отправляется запрос к БД: authorRepository.save() с ID: {} на обновление автора", authorId);
         authorRepository.save(existingAuthor);
        logger.info("Получен ответ от БД: authorRepository.save() с ID: {} на обновление автора. Успешно", authorId);
    }
}
