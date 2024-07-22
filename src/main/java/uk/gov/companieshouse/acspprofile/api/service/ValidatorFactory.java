package uk.gov.companieshouse.acspprofile.api.service;

import static uk.gov.companieshouse.acspprofile.api.ACSPProfileApplication.NAMESPACE;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.filinghistory.InternalData.TransactionKindEnum;
import uk.gov.companieshouse.api.filinghistory.InternalFilingHistoryApi;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class ValidatorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    private final TopLevelPutRequestValidator topLevelPutRequestValidator;
    private final AnnotationPutRequestValidator annotationPutRequestValidator;
    private final AssociatedFilingPutRequestValidator associatedFilingPutRequestValidator;
    private final ResolutionPutRequestValidator resolutionPutRequestValidator;

    public ValidatorFactory(TopLevelPutRequestValidator topLevelPutRequestValidator,
            AnnotationPutRequestValidator annotationPutRequestValidator,
            AssociatedFilingPutRequestValidator associatedFilingPutRequestValidator,
            ResolutionPutRequestValidator resolutionPutRequestValidator) {
        this.topLevelPutRequestValidator = topLevelPutRequestValidator;
        this.annotationPutRequestValidator = annotationPutRequestValidator;
        this.associatedFilingPutRequestValidator = associatedFilingPutRequestValidator;
        this.resolutionPutRequestValidator = resolutionPutRequestValidator;
    }

    public Validator<InternalFilingHistoryApi> getPutRequestValidator(TransactionKindEnum kind) {
        LOGGER.debug("Getting validator for [%s] transaction kind".formatted(kind.getValue()),
                DataMapHolder.getLogMap());
        return switch (kind) {
            case TOP_LEVEL -> topLevelPutRequestValidator;
            case ANNOTATION -> annotationPutRequestValidator;
            case ASSOCIATED_FILING -> associatedFilingPutRequestValidator;
            case RESOLUTION -> resolutionPutRequestValidator;
        };
    }
}
