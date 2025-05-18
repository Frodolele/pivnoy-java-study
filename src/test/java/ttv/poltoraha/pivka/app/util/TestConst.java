package ttv.poltoraha.pivka.app.util;

import ttv.poltoraha.pivka.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class TestConst {
    public static final String USERNAME = "poltoraha_pivka";
    public static final String REVIEW_TEXT = "Пиздатая рецензия, охуенная, жидкое золото";

    public static final String AUTHOR_NAME = "Lin_106_Xan";
    public static final List<Book> BOOK_LIST = new ArrayList<>(List.of(
            Book.builder()
                    .article("article_1")
                    .genre("romance")
                    .rating(4.1)
                    .tags("epic, dota")
                    .reviews(new ArrayList<>())
                    .quotes(new ArrayList<>())
                    .build(),

            Book.builder()
                    .article("article_2")
                    .genre("RoFl")
                    .rating(4.1)
                    .tags("dota, anime")
                    .reviews(new ArrayList<>())
                    .quotes(new ArrayList<>())
                    .build()
    ));


}
