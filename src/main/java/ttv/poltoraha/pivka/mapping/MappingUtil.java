package ttv.poltoraha.pivka.mapping;

import org.springframework.stereotype.Component;
import ttv.poltoraha.pivka.dao.request.AuthorDto;
import ttv.poltoraha.pivka.dao.request.ReviewRequestDto;
import ttv.poltoraha.pivka.entity.Author;
import ttv.poltoraha.pivka.entity.Book;
import ttv.poltoraha.pivka.entity.Review;

// Маппер - класс для того, чтобы превращать наши дто в Entity и наоборот
@Component
public class MappingUtil {
    public static Review fromRequestDto(ReviewRequestDto dto, Book book) {
        return Review.builder()
                .book(book)
                .text(dto.getText())
                .readerUsername(dto.getReaderUsername())
                .rating(dto.getRating())
                .build();
    }

    public static Review updateFromRequestDto(Review review, ReviewRequestDto dto) {
        review.setText(dto.getText());
        review.setRating(dto.getRating());

        return review;
    }

    public static Author AuthorFromDto(AuthorDto dto){
          return Author.builder()
                  .books(dto.getBooks())
                  .fullName(dto.getFullName())
                  .build();
    }
}
