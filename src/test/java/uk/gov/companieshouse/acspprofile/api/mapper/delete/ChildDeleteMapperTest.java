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
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAnnotation;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAssociatedFiling;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeltaTimestamp;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileResolution;

@ExtendWith(MockitoExtension.class)
class ChildDeleteMapperTest {

    private static final Instant INSTANT = Instant.now();
    private static final String ENTITY_ID = "entity ID";
    private static final String PARENT_TYPE = "parent type";

    @InjectMocks
    private ChildDeleteMapper deleteMapper;
    @Mock
    private Supplier<Instant> instantSupplier;

    @BeforeEach
    void setUp() {
        DataMapHolder.clear();
    }

    @Test
    void shouldReturnEmptyWhenEdgeCaseTopLevelTransaction() {
        // given
        ACSPProfileData data = new ACSPProfileData()
                .resolutions(new ArrayList<>(List.of(new ACSPProfileResolution()
                        .entityId(ENTITY_ID))));

        ACSPProfileDocument documentCopy = new ACSPProfileDocument()
                .entityId(ENTITY_ID)
                .data(data);
        // when
        Optional<ACSPProfileDocument> actual = deleteMapper.removeTransaction(ENTITY_ID, 0, documentCopy,
                data::getResolutions, data::resolutions);

        // then
        assertTrue(actual.isEmpty());
        verifyNoInteractions(instantSupplier);
    }

    @Test
    void shouldReturnEmptyWhenChildTransactionWithNoParentType() {
        // given
        ACSPProfileData data = new ACSPProfileData()
                .resolutions(new ArrayList<>(List.of(new ACSPProfileResolution()
                        .entityId(ENTITY_ID))));

        ACSPProfileDocument documentCopy = new ACSPProfileDocument()
                .data(data);
        // when
        Optional<ACSPProfileDocument> actual = deleteMapper.removeTransaction(ENTITY_ID, 0, documentCopy,
                data::getResolutions, data::resolutions);

        // then
        assertTrue(actual.isEmpty());
        verifyNoInteractions(instantSupplier);
    }

    @Test
    void shouldReturnUpdatedDocumentWithResolutionsArrayNullWhenLastChild() {
        // given
        ACSPProfileData data = new ACSPProfileData()
                .type(PARENT_TYPE)
                .resolutions(new ArrayList<>(List.of(new ACSPProfileResolution()
                        .entityId(ENTITY_ID))));

        ACSPProfileDocument documentCopy = new ACSPProfileDocument()
                .data(data);

        ACSPProfileDocument expected = new ACSPProfileDocument()
                .data(new ACSPProfileData()
                        .type(PARENT_TYPE))
                .updated(new ACSPProfileDeltaTimestamp()
                        .at(INSTANT)
                        .by("uninitialised"));

        when(instantSupplier.get()).thenReturn(INSTANT);

        // when
        Optional<ACSPProfileDocument> actual = deleteMapper.removeTransaction(ENTITY_ID, 0, documentCopy,
                data::getResolutions, data::resolutions);

        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
        verify(instantSupplier).get();
    }

    @Test
    void shouldReturnUpdatedDocumentWithAnnotationsArrayNullWhenLastChild() {
        // given
        ACSPProfileData data = new ACSPProfileData()
                .type(PARENT_TYPE)
                .annotations(new ArrayList<>(List.of(new ACSPProfileAnnotation()
                        .entityId(ENTITY_ID))));

        ACSPProfileDocument documentCopy = new ACSPProfileDocument()
                .data(data);

        ACSPProfileDocument expected = new ACSPProfileDocument()
                .data(new ACSPProfileData()
                        .type(PARENT_TYPE))
                .updated(new ACSPProfileDeltaTimestamp()
                        .at(INSTANT)
                        .by("uninitialised"));

        when(instantSupplier.get()).thenReturn(INSTANT);

        // when
        Optional<ACSPProfileDocument> actual = deleteMapper.removeTransaction(ENTITY_ID, 0, documentCopy,
                data::getAnnotations, data::annotations);

        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
        verify(instantSupplier).get();
    }

    @Test
    void shouldReturnUpdatedDocumentWithAssociatedFilingsArrayNullWhenLastChild() {
        // given
        ACSPProfileData data = new ACSPProfileData()
                .type(PARENT_TYPE)
                .associatedFilings(new ArrayList<>(List.of(new ACSPProfileAssociatedFiling()
                        .entityId(ENTITY_ID))));

        ACSPProfileDocument documentCopy = new ACSPProfileDocument()
                .data(data);

        ACSPProfileDocument expected = new ACSPProfileDocument()
                .data(new ACSPProfileData()
                        .type(PARENT_TYPE))
                .updated(new ACSPProfileDeltaTimestamp()
                        .at(INSTANT)
                        .by("uninitialised"));

        when(instantSupplier.get()).thenReturn(INSTANT);

        // when
        Optional<ACSPProfileDocument> actual = deleteMapper.removeTransaction(ENTITY_ID, 0, documentCopy,
                data::getAssociatedFilings, data::associatedFilings);

        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
        verify(instantSupplier).get();
    }

    @Test
    void shouldRemoveOneResolutionAtIndexAndReturnUpdatedDocument() {
        // given
        ACSPProfileData data = new ACSPProfileData()
                .resolutions(new ArrayList<>(
                        List.of(
                                new ACSPProfileResolution()
                                        .entityId(ENTITY_ID),
                                new ACSPProfileResolution())));

        ACSPProfileDocument documentCopy = new ACSPProfileDocument()
                .data(data);

        ACSPProfileDocument expected = new ACSPProfileDocument()
                .data(new ACSPProfileData()
                        .resolutions(List.of(new ACSPProfileResolution())))
                .updated(new ACSPProfileDeltaTimestamp()
                        .at(INSTANT)
                        .by("uninitialised"));

        when(instantSupplier.get()).thenReturn(INSTANT);

        // when
        Optional<ACSPProfileDocument> actual = deleteMapper.removeTransaction(ENTITY_ID, 0, documentCopy,
                data::getResolutions, data::resolutions);

        // then
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
        verify(instantSupplier).get();
    }
}