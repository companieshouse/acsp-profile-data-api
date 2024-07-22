package uk.gov.companieshouse.acspprofile.api.mapper.get;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.filinghistory.ExternalData;
import uk.gov.companieshouse.api.filinghistory.FilingHistoryList;
import uk.gov.companieshouse.api.filinghistory.FilingHistoryList.FilingHistoryStatusEnum;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileListAggregate;

@ExtendWith(MockitoExtension.class)
class ListGetResponseMapperTest {

    private static final String STATUS = "acsp-profile-available";
    private static final int ITEMS_PER_PAGE = 25;
    private static final int START_INDEX = 0;
    @InjectMocks
    private ListGetResponseMapper listGetResponseMapper;
    @Mock
    private ItemGetResponseMapper itemGetResponseMapper;
    @Mock
    private ACSPProfileItemCleanser ACSPProfileItemCleanser;
    @Mock
    private ExternalData dirtyExternalData;
    @Mock
    private ExternalData cleansedExternalData;
    @Mock
    private ACSPProfileDocument ACSPProfileDocument;

    @Test
    void shouldMapBaseFilingHistoryList() {
        // given
        FilingHistoryList expected = new FilingHistoryList()
                .startIndex(START_INDEX)
                .itemsPerPage(ITEMS_PER_PAGE)
                .filingHistoryStatus(FilingHistoryStatusEnum.AVAILABLE)
                .totalCount(0)
                .items(List.of());

        // when
        FilingHistoryList actual = listGetResponseMapper.mapBaseFilingHistoryList(START_INDEX, ITEMS_PER_PAGE, STATUS);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldMapFullFilingHistoryList() {
        // given
        when(itemGetResponseMapper.mapFilingHistoryItem(any())).thenReturn(dirtyExternalData);
        when(ACSPProfileItemCleanser.cleanseFilingHistoryItem(any())).thenReturn(cleansedExternalData);

        ACSPProfileListAggregate listAggregate = new ACSPProfileListAggregate()
                .documentList(List.of(ACSPProfileDocument))
                .totalCount(1);

        FilingHistoryList expected = new FilingHistoryList()
                .startIndex(START_INDEX)
                .itemsPerPage(ITEMS_PER_PAGE)
                .filingHistoryStatus(FilingHistoryStatusEnum.AVAILABLE)
                .totalCount(1)
                .items(List.of(cleansedExternalData));

        // when
        FilingHistoryList actual = listGetResponseMapper.mapFilingHistoryList(START_INDEX, ITEMS_PER_PAGE, STATUS,
                listAggregate);

        // then
        assertEquals(expected, actual);
        verify(itemGetResponseMapper).mapFilingHistoryItem(ACSPProfileDocument);
        verify(ACSPProfileItemCleanser).cleanseFilingHistoryItem(dirtyExternalData);
    }
}