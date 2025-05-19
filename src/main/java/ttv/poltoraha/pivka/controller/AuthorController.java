package ttv.poltoraha.pivka.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.service.AuthorService;

import java.util.List;

// Контроллеры - это классы для создания внешних http ручек. Чтобы к нам могли прийти по http, например, через постман
// Или если у приложухи есть веб-морда, каждое действие пользователя - это http запросы
@RestController
@RequestMapping("/author")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;

    private final Logger logger = LoggerFactory.getLogger(AuthorController.class);


    @PostMapping("/create")
    public void createAuthor(@RequestBody AuthorDto authorDto) {
        logger.info("Получен запрос на /author/create");
        authorService.create(authorDto);
        logger.info("Запрос на /author/create успешно выполнен");
    }

    @PostMapping("/delete")
    public void deleteAuthorById(@RequestParam Integer id) {
        logger.info("Получен запрос на /author/delete. Удаление автора с ID={}", id);
        authorService.delete(id);
        logger.info("Запрос на /author/delete успешно выполнен. Автор с ID={} удалён. ", id);
    }

    @PostMapping("/add/books")
    public void addBooksToAuthor(@RequestParam Integer id, @RequestBody List<Book> books) {
        logger.info("Получен запрос на author/add/books. Добавление {} книг автору ID={}", books.size(), id);
        authorService.addBooks(id, books);
        logger.info("Запрос на author/add/books успешно выполнен. Добавлено {} книг автору ID={}", books.size(), id);
    }
}
