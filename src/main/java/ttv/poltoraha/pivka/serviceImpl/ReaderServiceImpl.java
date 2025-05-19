package ttv.poltoraha.pivka.serviceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ttv.poltoraha.pivka.entity.Quote;
import ttv.poltoraha.pivka.entity.Reader;
import ttv.poltoraha.pivka.entity.Reading;
import ttv.poltoraha.pivka.repository.BookRepository;
import ttv.poltoraha.pivka.repository.QuoteRepository;
import ttv.poltoraha.pivka.repository.ReaderRepository;
import ttv.poltoraha.pivka.service.ReaderService;
import util.MyUtility;

@Service
@RequiredArgsConstructor
@Transactional
public class ReaderServiceImpl implements ReaderService {
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;
    private final QuoteRepository quoteRepository;
    @Override
    public void createQuote(String username, Integer book_id, String text) {
        val newQuote = new Quote();
        val reader = readerRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException("Entity reader with id = " + username + " was not found"));
        val book = bookRepository.findById(book_id)
                .orElseThrow(() -> new EntityNotFoundException("Entity book with id = " + book_id + " was not found"));
        newQuote.setBook(book);
        newQuote.setText(text);
        newQuote.setReader(reader);

        reader.getQuotes().add(newQuote);

        // todo потенциально лучше сейвить quoteRepository. Чем меньше вложенностей у сохраняемой сущности - тем эффективнее это будет происходить.
        quoteRepository.save(newQuote);
        //я ваще хуй знает че я сделал,я абсолютно не уверен что это правильно, почему оно работает эффективнее я хуй знает
        // мб из-за каскадов в ридеррепо и типо слишком много данных дергается просто так не ебу
    }

    @Override
    public void addFinishedBook(String username, Integer bookId) {
        val reader = MyUtility.findEntityById(readerRepository.findByUsername(username), "reader", username);

        val book = MyUtility.findEntityById(bookRepository.findById(bookId), "book", bookId.toString());

        val reading = new Reading();
        reading.setReader(reader);
        reading.setBook(book);

        reader.getReadings().add(reading);

        readerRepository.save(reader);
    }

    @Override
    public void createReader(String username, String password) {
        val reader = new Reader();
        reader.setUsername(username);
        reader.setPassword(password);

        readerRepository.save(reader);
    }
}
