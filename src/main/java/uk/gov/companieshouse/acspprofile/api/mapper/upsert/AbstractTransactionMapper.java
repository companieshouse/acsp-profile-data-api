package uk.gov.companieshouse.acspprofile.api.mapper.upsert;

import static java.lang.Boolean.TRUE;

import java.time.Instant;
import uk.gov.companieshouse.api.filinghistory.InternalFilingHistoryApi;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeltaTimestamp;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;

public abstract class AbstractTransactionMapper {

    private final LinksMapper linksMapper;

    protected AbstractTransactionMapper(LinksMapper linksMapper) {
        this.linksMapper = linksMapper;
    }

    public ACSPProfileDocument mapNewFilingHistory(String id, InternalFilingHistoryApi request, Instant instant) {
        ACSPProfileDocument newDocument = new ACSPProfileDocument()
                .transactionId(id)
                .created(new ACSPProfileDeltaTimestamp()
                        .at(instant)
                        .by(request.getInternalData().getUpdatedBy()));

        return mapTopLevelFields(request, newDocument, instant)
                .data(mapFilingHistoryData(request, new ACSPProfileData())
                        .links(linksMapper.map(request.getExternalData().getLinks()))
                        .paperFiled(request.getExternalData().getPaperFiled()));
    }

    public ACSPProfileDocument mapExistingFilingHistory(InternalFilingHistoryApi request,
                                                        ACSPProfileDocument existingDocument, Instant instant) {
        ACSPProfileData existingData = existingDocument.getData();

        Boolean paperFiled = existingData.getPaperFiled();
        if (!TRUE.equals(paperFiled)) {
            paperFiled = request.getExternalData().getPaperFiled();
        }
        return mapTopLevelFields(request, existingDocument, instant)
                .data(mapFilingHistoryData(request, existingData)
                        .paperFiled(paperFiled));
    }

    protected abstract ACSPProfileData mapFilingHistoryData(InternalFilingHistoryApi request, ACSPProfileData data);

    protected abstract ACSPProfileDocument mapTopLevelFields(InternalFilingHistoryApi request,
                                                             ACSPProfileDocument document, Instant instant);
}
