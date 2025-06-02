package ttv.poltoraha.pivka.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ttv.poltoraha.pivka.dao.dto.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.exception.AuthorAlreadyExistsException;
import ttv.poltoraha.pivka.exception.AuthorNotFoundException;
import ttv.poltoraha.pivka.service.AuthorService;
import ttv.poltoraha.pivka.serviceImpl.AuthorServiceImpl;

import java.util.List;

// Контроллеры - это классы для создания внешних http ручек. Чтобы к нам могли прийти по http, например, через постман
// Или если у приложухи есть веб-морда, каждое действие пользователя - это http запросы
@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    @PostMapping("/create")
    public void createAuthor(@RequestBody AuthorDto authorDto) {
        logger.info("Получен запрос на создание автора: {}", authorDto);
        try {
            authorService.create(authorDto);
            logger.info("Автор успешно создан: {}", authorDto);
        } catch (AuthorAlreadyExistsException exception) {
            logger.error("Ошибка при создании автора", exception);
            throw exception;
        }
    }

    @DeleteMapping("/delete")
    public void deleteAuthorById(@RequestParam Integer id) {
        logger.info("Получен запрос на создание автора с ID: {}", id);
        try {
            authorService.delete(id);
            logger.info("Автор с ID успешно удален: {}", id);
        } catch (AuthorNotFoundException exception) {
            logger.error("Ошибка при удаление автора с ID: {}",id, exception);
            throw exception;
        }
    }

    @PostMapping("/add/books")
    public void addBooksToAuthor(@RequestParam Integer id, @RequestBody List<Book> books) {
        logger.info("Получен запрос на добавление списка книг для автора с ID {}:", id);
        try {
            authorService.addBooks(id, books);
            logger.info("Список книг для автора с ID успешно добавлен: {}", id);
        } catch (AuthorNotFoundException exception) {
            logger.error("Ошибка при добавлении списка книг для автора с ID: {}",id, exception);
            throw exception;
        }
    }
}
