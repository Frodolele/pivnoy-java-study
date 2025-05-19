package ttv.poltoraha.pivka.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity(name="reader")
@Data
@ToString
@DiscriminatorValue("READER")
public class Reader extends MyUser {

    // Тут хороший пример зачем вообще юзать каскад. В текущем виде мы сохраняем quotes через класс ReaderService и вызов
    // репозитория readerRepository. Если не установить каскад тип, то цитата просто не будет создана в бд
    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quote> quotes = new ArrayList<>();
    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reading> readings = new ArrayList<>();

}
