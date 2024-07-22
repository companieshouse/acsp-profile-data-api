package uk.gov.companieshouse.acspprofile.api.mapper.get;

import static uk.gov.companieshouse.acspprofile.api.mapper.DateUtils.instantToString;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.filinghistory.Annotation;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAnnotation;

@Component
public class AnnotationsGetResponseMapper {

    private final DescriptionValuesGetResponseMapper mapper;

    public AnnotationsGetResponseMapper(DescriptionValuesGetResponseMapper mapper) {
        this.mapper = mapper;
    }

    public List<Annotation> map(List<ACSPProfileAnnotation> documentAnnotations) {
        return Optional.ofNullable(documentAnnotations)
                .map(inputAnnotations -> inputAnnotations
                        .stream()
                        .map(annotation ->
                                new Annotation()
                                        .annotation(annotation.getAnnotation())
                                        .category(annotation.getCategory())
                                        .description(annotation.getDescription())
                                        .type(annotation.getType())
                                        .date(instantToString(annotation.getDate()))
                                        .descriptionValues(mapper.map(annotation.getDescriptionValues())))
                        .toList())
                .orElse(null);
    }
}
