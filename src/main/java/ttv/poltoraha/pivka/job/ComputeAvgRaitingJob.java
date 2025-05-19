package ttv.poltoraha.pivka.job;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.entity.Review;
import ttv.poltoraha.pivka.repository.AuthorRepository;

import java.util.Arrays;

// Джобы - это штука, которая выполняет раз в энное время. Удобно, чтобы наше приложение долго не висело при
// каких-то долгих/трудоёмких задачах. Крутится себе на фоне и пускай
// todo раз в энное время берёт авторов, их книги и считает средний рейтинг автора. По идее условного readCommited будет достаточно
// todo подумать является ли это удобным применением readUncommited.
@Service
@RequiredArgsConstructor
public class ComputeAvgRaitingJob {
    private final AuthorRepository authorRepository;

    // В данной хуйне посмотрите что такое cron-выражение
    // Тут ещё забавный момент с аннотацией транзакционал. В данном примере она НЕ будет работать. Для адекватной работы нужно внедрить зависимость,
    // где будет метод computeAvgRaiting с этой аннотацией.
    @Scheduled(cron = "0 0/20 * * * ?")
    @Transactional
    public void computeAvgRaiting() {
        val authors = authorRepository.findAll();

        authors.forEach(author -> {
            author.setAvgRating(
                    author.getBooks().stream()
                            // флатмапа штука для того, чтобы внутри стрима вложенные массивы перевести в стрим, с которым можно работать дальше
                            .flatMap(book -> book.getReviews().stream())
                            .mapToInt(Review::getRating)
                            .average()
                            .orElse(5.0)
            );
        });

        authorRepository.saveAll(authors);
    }
}
