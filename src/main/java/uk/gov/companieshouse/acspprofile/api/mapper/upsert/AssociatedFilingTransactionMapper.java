package uk.gov.companieshouse.acspprofile.api.mapper.upsert;

import java.time.Instant;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.filinghistory.InternalData;
import uk.gov.companieshouse.api.filinghistory.InternalFilingHistoryApi;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAssociatedFiling;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeltaTimestamp;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;

@Component
public class AssociatedFilingTransactionMapper extends AbstractTransactionMapper {

    private final ChildListMapper<ACSPProfileAssociatedFiling> childListMapper;

    public AssociatedFilingTransactionMapper(LinksMapper linksMapper,
            ChildListMapper<ACSPProfileAssociatedFiling> childListMapper) {
        super(linksMapper);
        this.childListMapper = childListMapper;
    }

    @Override
    protected ACSPProfileData mapFilingHistoryData(InternalFilingHistoryApi request, ACSPProfileData data) {
        childListMapper.mapChildList(request, data.getAssociatedFilings(), data::associatedFilings);
        return data;
    }

    @Override
    protected ACSPProfileDocument mapTopLevelFields(InternalFilingHistoryApi request, ACSPProfileDocument document,
                                                    Instant instant) {
        final InternalData internalData = request.getInternalData();

        return document
                .entityId(internalData.getParentEntityId())
                .companyNumber(internalData.getCompanyNumber())
                .updated(new ACSPProfileDeltaTimestamp()
                        .at(instant)
                        .by(internalData.getUpdatedBy()));
    }
}
