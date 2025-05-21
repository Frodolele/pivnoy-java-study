package ttv.poltoraha.pivka.metrics;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

// Как правило все имеющиеся метрики создаются в отдельном классе.
@Component
public class CustomMetrics {
    private final Counter myCounter;
    private final Timer myTimer;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    public CustomMetrics(MeterRegistry meterRegistry) {
        this.myCounter = Counter
                .builder("author_controller_count")
                .description("total number of incoming requests")
                .register(meterRegistry);


        this.myTimer = meterRegistry.timer("db_call_duration", List.of(Tag.of("purpose", "db_calls")));
    }

    // методы для изменения метрик
    public void recordMyCounter() {
        myCounter.increment();
    }

    public void recordMyTimer(Long durationMs) {
        myTimer.record(durationMs, TimeUnit.MILLISECONDS);
    }
}
