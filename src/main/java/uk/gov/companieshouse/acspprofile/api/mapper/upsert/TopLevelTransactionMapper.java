package uk.gov.companieshouse.acspprofile.api.mapper.upsert;

import static uk.gov.companieshouse.acspprofile.api.ACSPProfileApplication.NAMESPACE;
import static uk.gov.companieshouse.acspprofile.api.mapper.DateUtils.isDeltaStale;

import java.time.Instant;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.filinghistory.ExternalData;
import uk.gov.companieshouse.api.filinghistory.InternalData;
import uk.gov.companieshouse.api.filinghistory.InternalFilingHistoryApi;
import uk.gov.companieshouse.acspprofile.api.exception.ConflictException;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAssociatedFiling;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileData;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDeltaTimestamp;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileDocument;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class TopLevelTransactionMapper extends AbstractTransactionMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    private final DataMapper dataMapper;
    private final ChildListMapper<ACSPProfileAssociatedFiling> childListMapper;
    private final OriginalValuesMapper originalValuesMapper;

    public TopLevelTransactionMapper(DataMapper dataMapper,
            OriginalValuesMapper originalValuesMapper, LinksMapper linksMapper,
            ChildListMapper<ACSPProfileAssociatedFiling> childListMapper) {
        super(linksMapper);
        this.dataMapper = dataMapper;
        this.originalValuesMapper = originalValuesMapper;
        this.childListMapper = childListMapper;
    }

    @Override
    protected ACSPProfileData mapFilingHistoryData(InternalFilingHistoryApi request, ACSPProfileData data) {
        ExternalData externalData = request.getExternalData();
        final ACSPProfileData mappedData = dataMapper.map(externalData, data);

        if (externalData.getAssociatedFilings() != null && !externalData.getAssociatedFilings().isEmpty()) {
            childListMapper.mapChildList(request, mappedData.getAssociatedFilings(), mappedData::associatedFilings);

            data.getAssociatedFilings().stream()
                    .filter(af -> request.getInternalData().getEntityId().equals(af.getEntityId()))
                    .findFirst()
                    .ifPresent(af -> af.originalDescription(
                            request.getExternalData().getAssociatedFilings().getFirst().getOriginalDescription()));
        }

        return mappedData;
    }

    @Override
    protected ACSPProfileDocument mapTopLevelFields(InternalFilingHistoryApi request, ACSPProfileDocument document,
                                                    Instant instant) {
        if (isDeltaStale(request.getInternalData().getDeltaAt(), document.getDeltaAt())) {
            LOGGER.error("Stale delta received; request delta_at: [%s] is not after existing delta_at: [%s]".formatted(
                    request.getInternalData().getDeltaAt(), document.getDeltaAt()), DataMapHolder.getLogMap());
            throw new ConflictException("Stale delta for upsert");
        }

        final InternalData internalData = request.getInternalData();
        final ExternalData externalData = request.getExternalData();
        return document
                .entityId(internalData.getEntityId())
                .companyNumber(internalData.getCompanyNumber())
                .documentId(internalData.getDocumentId())
                .barcode(externalData.getBarcode())
                .originalDescription(internalData.getOriginalDescription())
                .originalValues(originalValuesMapper.map(internalData.getOriginalValues()))
                .deltaAt(internalData.getDeltaAt())
                .updated(new ACSPProfileDeltaTimestamp()
                        .at(instant)
                        .by(internalData.getUpdatedBy()));
    }
}
