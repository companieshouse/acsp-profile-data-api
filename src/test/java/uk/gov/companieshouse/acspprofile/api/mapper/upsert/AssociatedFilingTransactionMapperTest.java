package uk.gov.companieshouse.acspprofile.api.mapper.upsert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.filinghistory.ExternalData;
import uk.gov.companieshouse.api.filinghistory.InternalData;
import uk.gov.companieshouse.api.filinghistory.InternalFilingHistoryApi;
import uk.gov.companieshouse.api.filinghistory.Links;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAssociatedFiling;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeltaTimestamp;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileLinks;

@ExtendWith(MockitoExtension.class)
class AssociatedFilingTransactionMapperTest {

    private static final String TRANSACTION_ID = "transaction ID";
    private static final String PARENT_ENTITY_ID = "0987654321";
    private static final String COMPANY_NUMBER = "12345678";
    private static final Instant UPDATED_AT = Instant.parse("2024-05-02T00:00:00Z");
    private static final String UPDATED_BY = "updated by";
    private static final Instant CREATED_AT = Instant.parse("2020-05-02T00:00:00Z");
    private static final String CREATED_BY = "created by";


    @InjectMocks
    private AssociatedFilingTransactionMapper associatedFilingTransactionMapper;
    @Mock
    private LinksMapper linksMapper;
    @Mock
    private ChildListMapper<ACSPProfileAssociatedFiling> childListMapper;

    @Mock
    private List<ACSPProfileAssociatedFiling> associatedFilingList;

    @Test
    void shouldMapAssociatedFilingToNewDocument() {
        // given
        Links requestLinks = new Links()
                .self("self link");
        InternalFilingHistoryApi request = new InternalFilingHistoryApi()
                .internalData(new InternalData()
                        .parentEntityId(PARENT_ENTITY_ID)
                        .companyNumber(COMPANY_NUMBER)
                        .updatedBy(UPDATED_BY))
                .externalData(new ExternalData()
                        .links(requestLinks)
                        .paperFiled(true));

        ACSPProfileLinks expectedLinks = new ACSPProfileLinks()
                .self("self link");
        ACSPProfileDeltaTimestamp expectedTimestamp = new ACSPProfileDeltaTimestamp()
                .at(UPDATED_AT)
                .by(UPDATED_BY);
        ACSPProfileDocument expected = new ACSPProfileDocument()
                .transactionId(TRANSACTION_ID)
                .data(new ACSPProfileData()
                        .links(expectedLinks)
                        .paperFiled(true))
                .entityId(PARENT_ENTITY_ID)
                .companyNumber(COMPANY_NUMBER)
                .updated(expectedTimestamp)
                .created(expectedTimestamp);

        when(linksMapper.map(any())).thenReturn(expectedLinks);

        // when
        ACSPProfileDocument actual = associatedFilingTransactionMapper.mapNewFilingHistory(TRANSACTION_ID, request,
                UPDATED_AT);

        // then
        assertEquals(expected, actual);
        verify(linksMapper).map(requestLinks);
        verify(childListMapper).mapChildList(eq(request), isNull(), any());
    }

    @Test
    void shouldMapAssociatedFilingToExistingDocument() {
        // given
        InternalFilingHistoryApi request = new InternalFilingHistoryApi()
                .internalData(new InternalData()
                        .parentEntityId(PARENT_ENTITY_ID)
                        .companyNumber(COMPANY_NUMBER)
                        .updatedBy(UPDATED_BY))
                .externalData(new ExternalData()
                        .paperFiled(true));

        ACSPProfileDeltaTimestamp existingTimestamp = new ACSPProfileDeltaTimestamp()
                .at(CREATED_AT)
                .by(CREATED_BY);
        ACSPProfileLinks existingLinks = new ACSPProfileLinks()
                .self("self link")
                .documentMetadata("metadata");
        ACSPProfileDocument existingDocument = new ACSPProfileDocument()
                .data(new ACSPProfileData()
                        .links(existingLinks)
                        .associatedFilings(associatedFilingList))
                .created(existingTimestamp)
                .updated(existingTimestamp);

        ACSPProfileDocument expected = new ACSPProfileDocument()
                .data(new ACSPProfileData()
                        .links(existingLinks)
                        .paperFiled(true)
                        .associatedFilings(associatedFilingList))
                .entityId(PARENT_ENTITY_ID)
                .companyNumber(COMPANY_NUMBER)
                .updated(new ACSPProfileDeltaTimestamp()
                        .at(UPDATED_AT)
                        .by(UPDATED_BY))
                .created(existingTimestamp);

        // when
        ACSPProfileDocument actual =
                associatedFilingTransactionMapper.mapExistingFilingHistory(request, existingDocument, UPDATED_AT);

        // then
        assertEquals(expected, actual);
        verifyNoInteractions(linksMapper);
        verify(childListMapper).mapChildList(eq(request), eq(associatedFilingList), any());
    }
}
