package uk.gov.companieshouse.acspprofile.api.mapper.upsert;

import static uk.gov.companieshouse.acspprofile.api.mapper.DateUtils.stringToInstant;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.filinghistory.InternalData;
import uk.gov.companieshouse.api.filinghistory.InternalFilingHistoryApi;
import uk.gov.companieshouse.api.filinghistory.Resolution;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileResolution;

@Component
public class ResolutionChildMapper implements ChildMapper<ACSPProfileResolution> {

    private final DescriptionValuesMapper descriptionValuesMapper;

    public ResolutionChildMapper(DescriptionValuesMapper descriptionValuesMapper) {
        this.descriptionValuesMapper = descriptionValuesMapper;
    }

    @Override
    public ACSPProfileResolution mapChild(InternalFilingHistoryApi request,
                                          ACSPProfileResolution existingResolution) {
        InternalData internalData = request.getInternalData();
        Resolution requestResolution = request.getExternalData().getResolutions().getFirst();

        return existingResolution
                .barcode(requestResolution.getBarcode())
                .category(requestResolution.getCategory().getValue())
                .description(requestResolution.getDescription())
                .type(requestResolution.getType())
                .subcategory(requestResolution.getSubcategory())
                .date(stringToInstant(requestResolution.getDate()))
                .entityId(internalData.getEntityId())
                .descriptionValues(descriptionValuesMapper.map(requestResolution.getDescriptionValues()))
                .originalDescription(requestResolution.getOriginalDescription())
                .deltaAt(internalData.getDeltaAt());
    }

    @Override
    public ACSPProfileResolution newInstance() {
        return new ACSPProfileResolution();
    }
}
