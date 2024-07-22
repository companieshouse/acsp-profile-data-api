package uk.gov.companieshouse.acspprofile.api.mapper.delete;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.acspprofile.api.exception.BadRequestException;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAnnotation;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAssociatedFiling;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeleteAggregate;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileResolution;
import uk.gov.companieshouse.acspprofile.api.serdes.ACSPProfileDocumentCopier;

@ExtendWith(MockitoExtension.class)
class DeleteMapperDelegatorTest {

    private static final String ENTITY_ID = "entity ID";
    private static final String COMPOSITE_RES_TYPE = "RESOLUTIONS";
    private static final String CHILD_ENTITY_ID = "child entity ID";
    private static final String PARENT_TYPE = "CERTNM";

    @InjectMocks
    private DeleteMapperDelegator deleteMapperDelegator;
    @Mock
    private ACSPProfileDocumentCopier documentCopier;
    @Mock
    private CompositeResolutionDeleteMapper compositeResolutionDeleteMapper;
    @Mock
    private ChildDeleteMapper childDeleteMapper;

    @Mock
    private ACSPProfileDocument document;

    @Test
    void shouldCallCompositeResolutionMapperWhenCompositeResTypeAndResEntityIdMatches() {
        // given
        ACSPProfileDocument documentCopy = new ACSPProfileDocument()
                .entityId(ENTITY_ID)
                .data(new ACSPProfileData()
                        .type(COMPOSITE_RES_TYPE)
                        .resolutions(List.of(
                                new ACSPProfileResolution()
                                        .entityId("first ID"),
                                new ACSPProfileResolution()
                                        .entityId(ENTITY_ID))));
        ACSPProfileDeleteAggregate aggregate = new ACSPProfileDeleteAggregate()
                .resolutionIndex(1)
                .document(document);

        when(documentCopier.deepCopy(any())).thenReturn(documentCopy);
        when(compositeResolutionDeleteMapper.removeTransaction(anyInt(), any())).thenReturn(
                Optional.of(new ACSPProfileDocument()));

        // when
        Optional<ACSPProfileDocument> actual = deleteMapperDelegator.delegateDelete(ENTITY_ID, aggregate);

        // then
        assertTrue(actual.isPresent());
        verify(documentCopier).deepCopy(document);
        verify(compositeResolutionDeleteMapper).removeTransaction(1, documentCopy);
    }

    @Test
    void shouldCallChildDeleteMapperWhenChildResolutionAndResEntityIdMatches() {
        // given
        List<ACSPProfileResolution> resolutions = List.of(
                new ACSPProfileResolution()
                        .entityId(CHILD_ENTITY_ID));
        ACSPProfileDocument documentCopy = new ACSPProfileDocument()
                .entityId(ENTITY_ID)
                .data(new ACSPProfileData()
                        .type(PARENT_TYPE)
                        .resolutions(resolutions));
        ACSPProfileDeleteAggregate aggregate = new ACSPProfileDeleteAggregate()
                .resolutionIndex(0)
                .document(document);

        when(documentCopier.deepCopy(any())).thenReturn(documentCopy);
        when(childDeleteMapper.removeTransaction(any(), anyInt(), any(), any(), any())).thenReturn(
                Optional.of(new ACSPProfileDocument()));

        // when
        Optional<ACSPProfileDocument> actual = deleteMapperDelegator.delegateDelete(CHILD_ENTITY_ID, aggregate);

        // then
        assertTrue(actual.isPresent());
        verify(documentCopier).deepCopy(document);
        verify(childDeleteMapper).removeTransaction(eq(CHILD_ENTITY_ID), eq(0), eq(documentCopy),
                argThat(supplier -> supplier.get().equals(resolutions)), any());
    }

    @Test
    void shouldCallChildDeleteMapperWhenChildAnnotationAndAnnotationEntityIdMatches() {
        // given
        List<ACSPProfileAnnotation> annotations = List.of(
                new ACSPProfileAnnotation()
                        .entityId(CHILD_ENTITY_ID));
        ACSPProfileDocument documentCopy = new ACSPProfileDocument()
                .entityId(ENTITY_ID)
                .data(new ACSPProfileData()
                        .annotations(annotations));
        ACSPProfileDeleteAggregate aggregate = new ACSPProfileDeleteAggregate()
                .annotationIndex(0)
                .document(document);

        when(documentCopier.deepCopy(any())).thenReturn(documentCopy);
        when(childDeleteMapper.removeTransaction(any(), anyInt(), any(), any(), any())).thenReturn(
                Optional.of(new ACSPProfileDocument()));

        // when
        Optional<ACSPProfileDocument> actual = deleteMapperDelegator.delegateDelete(CHILD_ENTITY_ID, aggregate);

        // then
        assertTrue(actual.isPresent());
        verify(documentCopier).deepCopy(document);
        verify(childDeleteMapper).removeTransaction(eq(CHILD_ENTITY_ID), eq(0), eq(documentCopy),
                argThat(supplier -> supplier.get().equals(annotations)), any());
    }

    @Test
    void shouldCallChildDeleteMapperWhenChildAssociatedFilingAndAssociatedFilingEntityIdMatches() {
        // given
        List<ACSPProfileAssociatedFiling> associatedFilings = List.of(new ACSPProfileAssociatedFiling()
                .entityId(CHILD_ENTITY_ID));
        ACSPProfileDocument documentCopy = new ACSPProfileDocument()
                .entityId(ENTITY_ID)
                .data(new ACSPProfileData()
                        .associatedFilings(associatedFilings));

        ACSPProfileDeleteAggregate aggregate = new ACSPProfileDeleteAggregate()
                .associatedFilingIndex(0)
                .document(document);

        when(documentCopier.deepCopy(any())).thenReturn(documentCopy);
        when(childDeleteMapper.removeTransaction(any(), anyInt(), any(), any(), any())).thenReturn(
                Optional.of(new ACSPProfileDocument()));

        // when
        Optional<ACSPProfileDocument> actual = deleteMapperDelegator.delegateDelete(CHILD_ENTITY_ID, aggregate);

        // then
        assertTrue(actual.isPresent());
        verify(documentCopier).deepCopy(document);
        verify(childDeleteMapper).removeTransaction(eq(CHILD_ENTITY_ID), eq(0), eq(documentCopy),
                argThat(supplier -> supplier.get().equals(associatedFilings)), any());
    }

    @Test
    void shouldReturnTopLevelMapperWhenTopLevelEntityIdMatches() {
        // given
        ACSPProfileDocument documentCopy = new ACSPProfileDocument()
                .entityId(ENTITY_ID)
                .data(new ACSPProfileData());
        ACSPProfileDeleteAggregate aggregate = new ACSPProfileDeleteAggregate()
                .document(document);

        when(documentCopier.deepCopy(any())).thenReturn(documentCopy);

        // when
        Optional<ACSPProfileDocument> actual = deleteMapperDelegator.delegateDelete(ENTITY_ID, aggregate);

        // then
        assertTrue(actual.isEmpty());
        verify(documentCopier).deepCopy(document);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenNoEntityIdMatchesAndNoChildIndexes() {
        // given
        ACSPProfileDocument documentCopy = new ACSPProfileDocument()
                .entityId(ENTITY_ID);
        ACSPProfileDeleteAggregate aggregate = new ACSPProfileDeleteAggregate()
                .document(document);

        when(documentCopier.deepCopy(any())).thenReturn(documentCopy);

        // when
        Executable actual = () -> deleteMapperDelegator.delegateDelete(CHILD_ENTITY_ID, aggregate);

        // then
        assertThrows(BadRequestException.class, actual);
        verify(documentCopier).deepCopy(document);
    }
}