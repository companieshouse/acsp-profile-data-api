package uk.gov.companieshouse.acspprofile.api.mapper.upsert;

import static uk.gov.companieshouse.acspprofile.api.mapper.DateUtils.stringToInstant;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.filinghistory.Annotation;
import uk.gov.companieshouse.api.filinghistory.InternalData;
import uk.gov.companieshouse.api.filinghistory.InternalFilingHistoryApi;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAnnotation;

@Component
public class AnnotationChildMapper implements ChildMapper<ACSPProfileAnnotation> {

    private final DescriptionValuesMapper descriptionValuesMapper;

    public AnnotationChildMapper(DescriptionValuesMapper descriptionValuesMapper) {
        this.descriptionValuesMapper = descriptionValuesMapper;
    }

    @Override
    public ACSPProfileAnnotation mapChild(InternalFilingHistoryApi request,
                                          ACSPProfileAnnotation existingAnnotation) {
        InternalData internalData = request.getInternalData();
        Annotation requestAnnotation = request.getExternalData().getAnnotations().getFirst();

        return existingAnnotation
                .entityId(internalData.getEntityId())
                .deltaAt(internalData.getDeltaAt())
                .annotation(requestAnnotation.getAnnotation())
                .category(requestAnnotation.getCategory())
                .date(stringToInstant(requestAnnotation.getDate()))
                .description(requestAnnotation.getDescription())
                .descriptionValues(descriptionValuesMapper.map(requestAnnotation.getDescriptionValues()))
                .type(requestAnnotation.getType());
    }

    @Override
    public ACSPProfileAnnotation newInstance() {
        return new ACSPProfileAnnotation();
    }
}
