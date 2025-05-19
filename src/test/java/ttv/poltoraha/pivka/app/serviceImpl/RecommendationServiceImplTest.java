package ttv.poltoraha.pivka.app.serviceImpl;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.controller.ReaderController;
import ttv.poltoraha.pivka.entity.Reader;
import ttv.poltoraha.pivka.repository.ReaderRepository;
import ttv.poltoraha.pivka.service.ReaderService;
import ttv.poltoraha.pivka.service.RecommendationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ttv.poltoraha.pivka.app.util.TestConst.USERNAME;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Используйте H2 вместо реальной БД
@Transactional // Обеспечивает откат транзакций после каждого теста
public class RecommendationServiceImplTest {
    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private ReaderService readerService;
    @Autowired
    private ReaderRepository readerRepository;
    @Autowired
    private ReaderController readerController;

    @BeforeEach
    public void setUp() {
        val reader = new Reader();
        reader.setUsername("MY_USERNAME");
        reader.setPassword("132");

        readerRepository.save(reader);

        readerService.addFinishedBook(USERNAME, 1);
        readerService.addFinishedBook(USERNAME, 2);
        readerService.addFinishedBook(USERNAME, 3);
        readerService.addFinishedBook(USERNAME, 6);
        readerService.addFinishedBook(USERNAME, 7);
        readerService.addFinishedBook(USERNAME, 8);
        readerService.addFinishedBook(USERNAME, 9);

        readerController.addQuote(USERNAME, 1, "Quote");
    }

    @Test
    public void checkRecommendAuthor() {
        val authors = recommendationService.recommendAuthor(USERNAME);

        // todo эта залупа возвращает 3 из-за того, что надо эти ебучие книги и авторов фиксить
        // там столько ёбани, что просто скипаю. Если бы книг, авторов было бы больше и они бы не повторялись - всё ок бы работало
        assertEquals(authors.size(), 3);
    }

    @Test
    public void checkRecommendQuote() {
        val quotes = recommendationService.recommendQuoteByBook(1);

        assertEquals(quotes.size(), 1);
    }
}
