package uk.gov.companieshouse.acspprofile.api.mapper.upsert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.filinghistory.AssociatedFiling;
import uk.gov.companieshouse.api.filinghistory.ExternalData;
import uk.gov.companieshouse.api.filinghistory.InternalData;
import uk.gov.companieshouse.api.filinghistory.InternalDataOriginalValues;
import uk.gov.companieshouse.api.filinghistory.InternalFilingHistoryApi;
import uk.gov.companieshouse.api.filinghistory.Links;
import uk.gov.companieshouse.acspprofile.api.exception.ConflictException;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAssociatedFiling;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeltaTimestamp;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileLinks;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileOriginalValues;

@ExtendWith(MockitoExtension.class)
class TopLevelTransactionMapperTest {

    private static final String TRANSACTION_ID = "transactionId";
    private static final String ENTITY_ID = "1234567890";
    private static final String COMPANY_NUMBER = "123456789";
    private static final String DOCUMENT_ID = "documentId";
    private static final String BARCODE = "barcode";
    private static final String ORIGINAL_DESCRIPTION = "original description";
    private static final String EXISTING_DOCUMENT_DELTA_AT = "20140916230459600643";
    private static final String NEWEST_REQUEST_DELTA_AT = "20151025185208001000";
    private static final String STALE_REQUEST_DELTA_AT = "20130615185208001000";
    private static final Instant INSTANT = Instant.now();
    private static final String UPDATED_BY = "84746291";
    private static final String EXPECTED_DELTA_AT = NEWEST_REQUEST_DELTA_AT;

    @InjectMocks
    private TopLevelTransactionMapper topLevelMapper;
    @Mock
    private DataMapper dataMapper;
    @Mock
    private OriginalValuesMapper originalValuesMapper;
    @Mock
    private LinksMapper linksMapper;
    @Mock
    private ChildListMapper<ACSPProfileAssociatedFiling> childListMapper;

    @Mock
    private ACSPProfileData expectedACSPProfileData;
    @Mock
    private ACSPProfileData existingACSPProfileData;
    @Mock
    private ACSPProfileOriginalValues expectedACSPProfileOriginalValues;
    @Mock
    private ACSPProfileOriginalValues existingACSPProfileOriginalValues;
    @Mock
    private ACSPProfileLinks expectedACSPProfileLinks;

    @Mock
    private InternalDataOriginalValues requestOriginalValues;
    @Mock
    private ExternalData requestExternalData;
    @Mock
    private Links requestLinks;


    @Test
    void mapNewFilingHistoryShouldReturnNewFilingHistoryDocument() {
        // given
        when(dataMapper.map((any()), any())).thenReturn(expectedACSPProfileData);
        when(requestExternalData.getLinks()).thenReturn(requestLinks);
        when(linksMapper.map(any())).thenReturn(expectedACSPProfileLinks);
        when(expectedACSPProfileData.links(any())).thenReturn(expectedACSPProfileData);
        when(requestExternalData.getPaperFiled()).thenReturn(true);
        when(expectedACSPProfileData.paperFiled(any())).thenReturn(expectedACSPProfileData);
        when(originalValuesMapper.map(any())).thenReturn(expectedACSPProfileOriginalValues);
        when(requestExternalData.getBarcode()).thenReturn(BARCODE);

        final InternalFilingHistoryApi request = buildPutRequestBody();
        final ACSPProfileDocument expectedDocument = getFilingHistoryDocument(
                expectedACSPProfileData,
                expectedACSPProfileOriginalValues,
                EXPECTED_DELTA_AT);

        // when
        final ACSPProfileDocument actualDocument = topLevelMapper.mapNewFilingHistory(TRANSACTION_ID, request,
                INSTANT);

        // then
        assertEquals(expectedDocument, actualDocument);
        verifyNoInteractions(childListMapper);
        verify(expectedACSPProfileData).paperFiled(true);
        verify(dataMapper).map(requestExternalData, new ACSPProfileData());
        verify(originalValuesMapper).map(requestOriginalValues);
        verify(linksMapper).map(requestLinks);
    }

    @Test
    void mapExistingFilingHistoryShouldReturnUpdatedFilingHistoryDocument() {
        // given
        when(dataMapper.map(any(), any())).thenReturn(expectedACSPProfileData);
        when(requestExternalData.getPaperFiled()).thenReturn(true);
        when(expectedACSPProfileData.paperFiled(any())).thenReturn(expectedACSPProfileData);
        when(originalValuesMapper.map(any())).thenReturn(expectedACSPProfileOriginalValues);
        when(requestExternalData.getBarcode()).thenReturn(BARCODE);
        when(requestExternalData.getAssociatedFilings()).thenReturn(null);

        final InternalFilingHistoryApi request = buildPutRequestBody();
        final ACSPProfileDocument expectedDocument = getFilingHistoryDocument(
                expectedACSPProfileData,
                expectedACSPProfileOriginalValues,
                EXPECTED_DELTA_AT);

        final ACSPProfileDocument existingDocument = getFilingHistoryDocument(
                existingACSPProfileData,
                existingACSPProfileOriginalValues,
                EXISTING_DOCUMENT_DELTA_AT);

        // when
        ACSPProfileDocument actualDocument = topLevelMapper.mapExistingFilingHistory(request, existingDocument,
                INSTANT);

        // then
        assertEquals(expectedDocument, actualDocument);
        verifyNoInteractions(childListMapper);
        verify(expectedACSPProfileData).paperFiled(true);
        verify(dataMapper).map(requestExternalData, existingACSPProfileData);
        verifyNoInteractions(childListMapper);
        verify(originalValuesMapper).map(requestOriginalValues);
    }

    @Test
    void shouldUpdateExistingDocumentWhenExistingDocumentHasAssociatedFilings() {
        // given
        List<AssociatedFiling> requestAssociatedFilingList = List.of(new AssociatedFiling());
        ACSPProfileData expectedACSPProfileData = new ACSPProfileData()
                .associatedFilings(List.of(new ACSPProfileAssociatedFiling()));

        final InternalFilingHistoryApi request = buildPutRequestBody();
        final ACSPProfileDocument expectedDocument = getFilingHistoryDocument(
                expectedACSPProfileData,
                expectedACSPProfileOriginalValues,
                EXPECTED_DELTA_AT);

        final ACSPProfileDocument existingDocument = getFilingHistoryDocument(
                existingACSPProfileData,
                existingACSPProfileOriginalValues,
                EXISTING_DOCUMENT_DELTA_AT);

        when(dataMapper.map(any(), any())).thenReturn(expectedACSPProfileData);
        when(originalValuesMapper.map(any())).thenReturn(expectedACSPProfileOriginalValues);
        when(requestExternalData.getBarcode()).thenReturn(BARCODE);
        when(requestExternalData.getAssociatedFilings()).thenReturn(requestAssociatedFilingList);

        // when
        ACSPProfileDocument actualDocument = topLevelMapper.mapExistingFilingHistory(request, existingDocument,
                INSTANT);

        // then
        assertEquals(expectedDocument, actualDocument);
        verify(dataMapper).map(requestExternalData, existingACSPProfileData);
        verify(childListMapper).mapChildList(eq(request), eq(expectedACSPProfileData.getAssociatedFilings()), any());
        verify(originalValuesMapper).map(requestOriginalValues);
    }

    @Test
    void shouldUpdateExistingDocumentWhenExistingDocumentHasEmptyAssociatedFilings() {
        // given
        List<AssociatedFiling> requestAssociatedFilingList = Collections.emptyList();
        ACSPProfileData expectedACSPProfileData = new ACSPProfileData()
                .associatedFilings(List.of(new ACSPProfileAssociatedFiling()));

        final InternalFilingHistoryApi request = buildPutRequestBody();
        final ACSPProfileDocument expectedDocument = getFilingHistoryDocument(
                expectedACSPProfileData,
                expectedACSPProfileOriginalValues,
                EXPECTED_DELTA_AT);

        final ACSPProfileDocument existingDocument = getFilingHistoryDocument(
                existingACSPProfileData,
                existingACSPProfileOriginalValues,
                EXISTING_DOCUMENT_DELTA_AT);

        when(dataMapper.map(any(), any())).thenReturn(expectedACSPProfileData);
        when(originalValuesMapper.map(any())).thenReturn(expectedACSPProfileOriginalValues);
        when(requestExternalData.getBarcode()).thenReturn(BARCODE);
        when(requestExternalData.getAssociatedFilings()).thenReturn(requestAssociatedFilingList);

        // when
        ACSPProfileDocument actualDocument = topLevelMapper.mapExistingFilingHistory(request, existingDocument,
                INSTANT);

        // then
        assertEquals(expectedDocument, actualDocument);
        verify(dataMapper).map(requestExternalData, existingACSPProfileData);
        verifyNoInteractions(childListMapper);
        verify(originalValuesMapper).map(requestOriginalValues);
    }

    @Test
    void shouldThrowConflictExceptionWhenRequestDeltaAtIsStale() {
        // given
        final InternalFilingHistoryApi request = buildPutRequestBody();
        request.getInternalData().deltaAt(STALE_REQUEST_DELTA_AT);

        final ACSPProfileDocument existingDocument = getFilingHistoryDocument(
                existingACSPProfileData,
                existingACSPProfileOriginalValues,
                EXISTING_DOCUMENT_DELTA_AT);

        // when
        Executable executable = () -> topLevelMapper.mapExistingFilingHistory(request, existingDocument,
                INSTANT);

        // then
        assertThrows(ConflictException.class, executable);
        verifyNoInteractions(dataMapper);
        verifyNoInteractions(originalValuesMapper);
    }

    private InternalFilingHistoryApi buildPutRequestBody() {
        return new InternalFilingHistoryApi()
                .externalData(requestExternalData)
                .internalData(buildInternalData());
    }

    private InternalData buildInternalData() {
        return new InternalData()
                .entityId(ENTITY_ID)
                .companyNumber(COMPANY_NUMBER)
                .documentId(DOCUMENT_ID)
                .deltaAt(NEWEST_REQUEST_DELTA_AT)
                .originalDescription("original description")
                .originalValues(requestOriginalValues)
                .parentEntityId("parent_entity_id")
                .updatedBy(UPDATED_BY);
    }

    private ACSPProfileDocument getFilingHistoryDocument(ACSPProfileData data,
                                                         ACSPProfileOriginalValues originalValues, String deltaAt) {
        ACSPProfileDeltaTimestamp timestamp = new ACSPProfileDeltaTimestamp()
                .at(INSTANT)
                .by(UPDATED_BY);
        return new ACSPProfileDocument()
                .transactionId(TRANSACTION_ID)
                .entityId(ENTITY_ID)
                .companyNumber(COMPANY_NUMBER)
                .documentId(DOCUMENT_ID)
                .barcode(BARCODE)
                .data(data)
                .originalDescription(ORIGINAL_DESCRIPTION)
                .originalValues(originalValues)
                .deltaAt(deltaAt)
                .updated(timestamp)
                .created(timestamp);
    }
}
