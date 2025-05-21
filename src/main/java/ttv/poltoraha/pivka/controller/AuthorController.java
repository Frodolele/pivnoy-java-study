package ttv.poltoraha.pivka.controller;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.metrics.CustomMetrics;
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
    private final CustomMetrics customMetrics;

    @PostMapping("/create")
    public void createAuthor(@RequestBody AuthorDto authorDto) {
        customMetrics.recordMyCounter();
        val timer = new StopWatch();

        logger.info("Получен запрос на /author/create");
        timer.start();
        authorService.create(authorDto);
        timer.stop();
        logger.info("Запрос на /author/create успешно выполнен");

        customMetrics.recordMyTimer(timer.getTime());
    }

    @PostMapping("/delete")
    public void deleteAuthorById(@RequestParam Integer id) {
        customMetrics.recordMyCounter();
        val timer = new StopWatch();

        logger.info("Получен запрос на /author/delete. Удаление автора с ID={}", id);
        timer.start();
        authorService.delete(id);
        timer.stop();
        logger.info("Запрос на /author/delete успешно выполнен. Автор с ID={} удалён. ", id);

        customMetrics.recordMyTimer(timer.getTime());
    }

    @PostMapping("/add/books")
    public void addBooksToAuthor(@RequestParam Integer id, @RequestBody List<Book> books) {
        customMetrics.recordMyCounter();
        val timer = new StopWatch();

        logger.info("Получен запрос на author/add/books. Добавление {} книг автору ID={}", books.size(), id);
        timer.start();
        authorService.addBooks(id, books);
        timer.stop();
        logger.info("Запрос на author/add/books успешно выполнен. Добавлено {} книг автору ID={}", books.size(), id);

        customMetrics.recordMyTimer(timer.getTime());
    }
}
