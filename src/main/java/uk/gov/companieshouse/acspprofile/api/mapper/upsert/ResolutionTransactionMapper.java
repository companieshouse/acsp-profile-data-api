package uk.gov.companieshouse.acspprofile.api.mapper.upsert;

import static uk.gov.companieshouse.acspprofile.api.mapper.DateUtils.stringToInstant;

import java.time.Instant;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.filinghistory.InternalData;
import uk.gov.companieshouse.api.filinghistory.InternalFilingHistoryApi;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeltaTimestamp;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileResolution;

@Component
public class ResolutionTransactionMapper extends AbstractTransactionMapper {

    private final DataMapper dataMapper;
    private final ChildListMapper<ACSPProfileResolution> childListMapper;

    public ResolutionTransactionMapper(LinksMapper linksMapper,
            DataMapper dataMapper,
            ChildListMapper<ACSPProfileResolution> childListMapper) {
        super(linksMapper);
        this.dataMapper = dataMapper;
        this.childListMapper = childListMapper;
    }

    @Override
    protected ACSPProfileData mapFilingHistoryData(InternalFilingHistoryApi request, ACSPProfileData data) {
        if (StringUtils.isBlank(request.getInternalData().getParentEntityId())) {
            data = dataMapper.map(request.getExternalData(), data)
                    .date(stringToInstant(request.getExternalData().getDate()));
        }
        childListMapper.mapChildList(request, data.getResolutions(), data::resolutions);
        return data;
    }

    @Override
    protected ACSPProfileDocument mapTopLevelFields(InternalFilingHistoryApi request,
                                                    ACSPProfileDocument document, Instant instant) {
        final InternalData internalData = request.getInternalData();

        if (StringUtils.isBlank(internalData.getParentEntityId())) {
            document
                    .barcode(request.getExternalData().getBarcode())
                    .documentId(internalData.getDocumentId())
                    .deltaAt(internalData.getDeltaAt())
                    .matchedDefault(internalData.getMatchedDefault())
                    .originalDescription(internalData.getOriginalDescription());
        } else {
            document.entityId(internalData.getParentEntityId());
        }
        return document
                .companyNumber(internalData.getCompanyNumber())
                .updated(new ACSPProfileDeltaTimestamp()
                        .at(instant)
                        .by(internalData.getUpdatedBy()));
    }
}
