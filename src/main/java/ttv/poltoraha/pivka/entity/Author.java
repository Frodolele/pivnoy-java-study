package ttv.poltoraha.pivka.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

// Энтити - это привязка класса к конкретной табличке в БД
@Entity(name="author")
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullName;
    private Double avgRating;
    @OneToMany(mappedBy="author")
    @ToString.Exclude
    private List<Book> books;
}
