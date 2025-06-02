package ttv.poltoraha.pivka.app.util;

import ttv.poltoraha.pivka.dao.dto.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;

public class DataAuthorUtils {

    public static Author getLevTolstoyTransient() {
        return Author.builder()
                .fullName("Lev Nilolaevich Tolctoy")
                .avgRating(10.0)
                .build();
    }

    public static AuthorDto getLevTolstoyTransientDto() {
        return AuthorDto.builder()
                .fullName("Lev Nilolaevich Tolctoy")
                .avgRating(10.0)
                .build();
    }


    public static Author getLevTolstoyPersisted() {
        return Author.builder()
                .id(1)
                .fullName("Lev Nilolaevich Tolctoy")
                .avgRating(10.0)
                .build();
    }


}
