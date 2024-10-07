package uk.gov.companieshouse.acspprofile.api.mapper;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.time.Instant;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class InstantSupplierTest {

    private final Supplier<Instant> instantSupplier = new InstantSupplier();

    @Test
    void shouldSupplyInstant() {
        assertInstanceOf(Instant.class, instantSupplier.get());
    }
}