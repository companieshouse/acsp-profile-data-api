package uk.gov.companieshouse.acspprofile.api.mapper.delete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeltaTimestamp;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileResolution;

@ExtendWith(MockitoExtension.class)
class CompositeResolutionDeleteMapperTest {

    private static final Instant INSTANT = Instant.now();

    @InjectMocks
    private CompositeResolutionDeleteMapper deleteMapper;
    @Mock
    private Supplier<Instant> instantSupplier;

    @BeforeEach
    void setUp() {
        DataMapHolder.clear();
    }

    @Test
    void shouldRemoveResolutionAtIndexAndReturnUpdatedDocument() {
        // given
        ACSPProfileDocument document = new ACSPProfileDocument()
                .data(new ACSPProfileData()
                        .resolutions(new ArrayList<>(
                                List.of(
                                        new ACSPProfileResolution()
                                                .entityId("entity ID"),
                                        new ACSPProfileResolution()))));

        ACSPProfileDocument expected = new ACSPProfileDocument()
                .data(new ACSPProfileData()
                        .resolutions(List.of(new ACSPProfileResolution())))
                .updated(new ACSPProfileDeltaTimestamp()
                        .at(INSTANT)
                        .by("uninitialised"));

        when(instantSupplier.get()).thenReturn(INSTANT);

        // when
        Optional<ACSPProfileDocument> actual = deleteMapper.removeTransaction(0, document);

        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
        verify(instantSupplier).get();
    }

    @Test
    void shouldReturnEmptyWhenLastCompositeResolution() {
        // given
        ACSPProfileDocument document = new ACSPProfileDocument()
                .data(new ACSPProfileData()
                        .resolutions(new ArrayList<>(List.of(new ACSPProfileResolution()))));

        // when
        Optional<ACSPProfileDocument> actual = deleteMapper.removeTransaction(0, document);

        // then
        assertTrue(actual.isEmpty());
        verifyNoInteractions(instantSupplier);
    }
}