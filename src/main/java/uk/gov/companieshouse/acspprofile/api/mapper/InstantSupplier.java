package uk.gov.companieshouse.acspprofile.api.mapper;

import java.time.Instant;
import java.util.function.Supplier;
import org.springframework.stereotype.Component;

@Component
public class InstantSupplier implements Supplier<Instant> {

    @Override
    public Instant get() {
        return Instant.now();
    }
}
