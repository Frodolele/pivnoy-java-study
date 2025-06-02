package ttv.poltoraha.pivka.dao.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class AuthorDto {
    private String fullName;
    private Double avgRating;
}
