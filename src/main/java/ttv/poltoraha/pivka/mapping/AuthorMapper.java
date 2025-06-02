package ttv.poltoraha.pivka.mapping;

import ttv.poltoraha.pivka.dao.dto.AuthorDto;
import ttv.poltoraha.pivka.entity.Author;

public class AuthorMapper {

    public Author mapFromDtoToEntity(AuthorDto dto) {
        return Author.builder()
                .avgRating(dto.getAvgRating())
                .fullName(dto.getFullName())
                .build();
    }

    public AuthorDto mapFromEntityToDto(Author entity) {
        return AuthorDto.builder()
                .avgRating(entity.getAvgRating())
                .fullName(entity.getFullName())
                .build();
    }
}
