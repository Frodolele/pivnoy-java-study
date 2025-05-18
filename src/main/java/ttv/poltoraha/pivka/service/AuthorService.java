package ttv.poltoraha.pivka.service;

import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;

import java.util.List;
// Сервис - это классы с основной логикой. Хорошей практикой является создание интерфейса.
// Интерфейс позволяет нам удобно расширять функционал или делать несколько реализаций без изменения, например, вызова метода в контроллере
public interface AuthorService {
    public void create(AuthorDto author);
    public void delete(Integer id);
    public void addBook(Integer id, Book book);
    public void addBooks(Integer id, List<Book> books);
    public List<Author> getTopAuthorsByTag(String tag, int count);
}
