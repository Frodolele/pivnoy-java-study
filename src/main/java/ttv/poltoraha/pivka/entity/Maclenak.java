package ttv.poltoraha.pivka.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="maclenak")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Maclenak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String test1;
    private String test2;
    private String test3;
    private String test4;
}
