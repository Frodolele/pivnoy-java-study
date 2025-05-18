package ttv.poltoraha.pivka.controller;

import lombok.RequiredArgsConstructor;
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

    @PostMapping("/create")
    public void createAuthor(@RequestBody AuthorDto author) {
        authorService.create(author);
    }

    @PostMapping("/delete")
    public void deleteAuthorById(@RequestParam Integer id) {
        authorService.delete(id);
    }

    @PostMapping("/add/books")
    public void addBooksToAuthor(@RequestParam Integer id, @RequestBody List<Book> books) {
        authorService.addBooks(id, books);
    }
}
