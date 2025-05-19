package ttv.poltoraha.pivka.app.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.dao.request.ReviewRequestDto;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Review;
import ttv.poltoraha.pivka.repository.BookRepository;
import ttv.poltoraha.pivka.repository.ReviewRepository;
import ttv.poltoraha.pivka.serviceImpl.ReviewServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ttv.poltoraha.pivka.app.model.Models.getReview;
import static ttv.poltoraha.pivka.app.model.Models.getReviewRequestDto;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // Используйте H2 вместо реальной БД
@Transactional // Обеспечивает откат транзакций после каждого теста
public class ReviewServiceImplTest {
    @Autowired
    private ReviewServiceImpl reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    public final String TEXT_UPDATED = "TEXT_UPDATE";

    private Book book;
    private ReviewRequestDto reviewRequestDto;
    private Review reviewEntity;

    @BeforeEach
    public void setUp() {
        book = bookRepository.findById(1).get();
        reviewEntity = getReview(book);

        reviewRequestDto = getReviewRequestDto(1);
    }

    @Test
    public void testCreateReview_Success() {
        val beforeUpdateReviewSize = reviewRepository.findAll().size();

        reviewService.createReview(reviewRequestDto);
        val afterCreateReviewSize = reviewRepository.findAll().size();
        // Проверяем, что отзыв был сохранен в базе данных
        assertNotEquals(beforeUpdateReviewSize, afterCreateReviewSize);
    }

    @Test
    public void testCreateReview_BookNotFound() {
        reviewRequestDto.setBookId(999); // Устанавливаем несуществующий ID книги

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            reviewService.createReview(reviewRequestDto);
        });
    }
//
    @Test
    public void testDeleteReview() {
        // Сначала создаем отзыв
        val updateReviewId = reviewRepository.save(reviewEntity).getId();

        // Удаляем отзыв
        reviewService.deleteReview(updateReviewId);

        // Проверяем, что отзыв был удален
        assertFalse(reviewRepository.existsById(updateReviewId));
    }
//
    @Test
    public void testUpdateReview_Success() {
        // Сначала создаем отзыв
        val reviewId = reviewRepository.save(reviewEntity).getId();

        // Обновляем отзыв
        reviewRequestDto.setText(TEXT_UPDATED);
        reviewRequestDto.setRating(4);
        reviewService.updateReview(reviewId, reviewRequestDto);

        // Проверяем, что отзыв был обновлен
        Review updatedReview = reviewRepository.findById(reviewId).orElseThrow();
        assertEquals(updatedReview.getText(), TEXT_UPDATED);
        assertEquals(updatedReview.getRating(), 4);
    }

    @Test
    public void testUpdateReview_ReviewNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            reviewService.updateReview(999, reviewRequestDto); // Используем несуществующий ID
        });
    }
}
