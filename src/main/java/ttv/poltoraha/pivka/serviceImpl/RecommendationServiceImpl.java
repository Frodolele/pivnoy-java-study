package ttv.poltoraha.pivka.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.entity.*;
import ttv.poltoraha.pivka.repository.BookRepository;
import ttv.poltoraha.pivka.repository.ReaderRepository;
import ttv.poltoraha.pivka.repository.ReadingRepository;
import ttv.poltoraha.pivka.service.AuthorService;
import ttv.poltoraha.pivka.service.RecommendationService;
import util.MyUtility;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendationServiceImpl implements RecommendationService {
    private final ReaderRepository readerRepository;
    private final AuthorService authorService;
    private final BookRepository bookRepository;
    private final ReadingRepository readingRepository;

    /**
     * Чё делает метод и чё он должен делать:
     * 1. Пользак заходит на сайт, у него есть уже список прочитанных книг
     * (это допущение, по-хорошему нужно и сценарий, когда у него нет прочитанных книг сделать тоже)
     * 2. Он хочет получить рекомендацию по Авторам, какие его могут заинтересовать
     * 3. Мы берём все его прочитанные книги. Отбираем 2 самых популярных тега среди них
     * 4. Дальше ищем авторов с самым высоким рейтингом, у которых есть хотя бы одна книга с популярным тегом
     * 5. 3 авторов рекомендуем по самому популярному тегу среди прочитанных, ещё 2х по второму по популярности
     *
     * @param username - уникальный идентификатор читателя, его логин
     * @return список авторов для рекомендации. По-умолчанию это число 5, но по-хорошему такими параметрами надо управлять
     */
    @Override
    public List<Author> recommendAuthor(String username) {
        val reader = MyUtility.findEntityById(readerRepository.findByUsername(username), "reader", username);

        val mostPopularTags = reader.getReadings().stream()
                .map(Reading::getBook) // Получаем книги
                .flatMap(book -> book.getTags().stream()) // Получаем теги из каждой книги
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting())) // Группируем теги и считаем их количество
                .entrySet()
                .stream() // Преобразуем в поток
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()) // Сортируем по количеству вхождений (по убыванию)
                .limit(2) // Берем только два самых популярных тега
                .map(Map.Entry::getKey) // Извлекаем только теги
                .toList(); // Собираем в список

        if (mostPopularTags.size() < 2) {
            // todo по-хорошему прокинуть ошибку, да и вообще сделать ControllerAdvice
            return null;
        }

        val mostRecommendedAuthor = authorService.getTopAuthorsByTag(mostPopularTags.get(0), 3);
        val secondMostRecommendedAuthor = authorService.getTopAuthorsByTag(mostPopularTags.get(1), 2);

        return Stream.concat(
                        mostRecommendedAuthor.stream(),
                        secondMostRecommendedAuthor.stream()
                )
                .distinct() // Убираем дубликаты, если это необходимо
                .toList(); // Собираем в список
    }

    /**
     * Чё должен делать метод
     * 1. Пользак заходит на сайт, хочет получить список книг, которые ему можно прочитать
     * 2. Мы берём все его прочитанные книги
     * 3. Отбираем среди них 2 самых популярных тега
     * 4. По первому популярному тэгу отбираем 3 книги с максимальным рейтингом
     * 5. По второму популярному тэгу отбираем 2 книги с максимальным рейтингом
     * 6. Возвращаем пользаку эти книжонку
     *
     * @return
     */
    @Override
    public List<Book> recommendBook(String username) {
        return null;
    }

    /**
     * Пользователь хочет перед тем как начать читать книгу, посмотреть её цитаты (мало ли что зацепит)
     * Мы берём книгу. Берём все цитаты по ней.
     * Фильтруем пользователей, в предпочтении пользователи, у которых наибольшее кол-во прочитанных книг
     * Выбираем 5 самых подходящих цитат.
     *
     * @return список подходящих цитат для определённой книги
     */
    @Override
    public List<Quote> recommendQuoteByBook(Integer book_id) {
        if (!bookRepository.existsById(book_id)) {
            throw new EntityNotFoundException(String.format("Entity book with id = %s was not found", book_id));
        }

        val readings = readingRepository.findAllByBook_id(book_id);

        val topReader = readings.stream()
                .map(Reading::getReader)
                .sorted(Comparator.comparingInt(reader -> reader.getReadings().size()))
                .toList();

        return topReader.stream()
                .flatMap(reader -> reader.getQuotes().stream())
                .filter(quote -> Objects.equals(quote.getBook().getId(), book_id))
                .limit(5)
                .toList();
    }
}
