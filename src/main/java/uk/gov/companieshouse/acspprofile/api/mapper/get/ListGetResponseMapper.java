package uk.gov.companieshouse.acspprofile.api.mapper.get;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.filinghistory.FilingHistoryList;
import uk.gov.companieshouse.api.filinghistory.FilingHistoryList.FilingHistoryStatusEnum;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileListAggregate;

@Component
public class ListGetResponseMapper {

    private final ItemGetResponseMapper itemGetResponseMapper;
    private final ACSPProfileItemCleanser ACSPProfileItemCleanser;

    public ListGetResponseMapper(ItemGetResponseMapper itemGetResponseMapper,
            ACSPProfileItemCleanser ACSPProfileItemCleanser) {
        this.itemGetResponseMapper = itemGetResponseMapper;
        this.ACSPProfileItemCleanser = ACSPProfileItemCleanser;
    }

    public FilingHistoryList mapBaseFilingHistoryList(int startIndex, int itemsPerPage, String status) {
        return new FilingHistoryList()
                .startIndex(startIndex)
                .itemsPerPage(itemsPerPage)
                .totalCount(0)
                .filingHistoryStatus(FilingHistoryStatusEnum.fromValue(status));
    }

    public FilingHistoryList mapFilingHistoryList(int startIndex, int itemsPerPage, String status,
            ACSPProfileListAggregate listAggregate) {
        return new FilingHistoryList()
                .startIndex(startIndex)
                .itemsPerPage(itemsPerPage)
                .filingHistoryStatus(FilingHistoryStatusEnum.fromValue(status))
                .totalCount(listAggregate.getTotalCount())
                .items(listAggregate.getDocumentList().stream()
                        .map(itemGetResponseMapper::mapFilingHistoryItem)
                        .map(ACSPProfileItemCleanser::cleanseFilingHistoryItem)
                        .toList());
    }
}
