package ttv.poltoraha.pivka.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.repository.AuthorRepository;
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
    private final AuthorRepository authorRepository;

    @PostMapping("/create")
    public void createAuthor(@RequestBody AuthorDto author) {
        logger.info("до создания автора: {}", author);
        authorService.create(author);
        logger.info("после создания автора: {}", author);
    }

    @PostMapping("/delete")
    public void deleteAuthorById(@RequestParam Integer id) {
        logger.info("до удаления автора: {}", authorRepository.findAll().size());
        authorService.delete(id);
        logger.info("после удаления автора: {}", authorRepository.findAll().size());
    }

    @PostMapping("/add/books")
    public void addBooksToAuthor(@RequestParam Integer id, @RequestBody List<Book> books) {
        logger.info("до добавления книг: {}", authorRepository.findById(id).get().getBooks());
        authorService.addBooks(id, books);
        logger.info("после добавления книг: {}", authorRepository.findById(id).get().getBooks());
    }
}
