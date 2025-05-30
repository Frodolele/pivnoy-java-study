package ttv.poltoraha.pivka.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chapter")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private Integer pageNumber;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
