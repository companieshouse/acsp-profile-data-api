package uk.gov.companieshouse.acspprofile.api.mapper;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;

@Component
public class AcspRequestMapper implements RequestMapper {

    @Override
    public AcspProfileDocument mapNewAcsp(InternalAcspApi internalAcsp) {
        return null;
    }

    @Override
    public AcspProfileDocument mapExistingAcsp(InternalAcspApi internalAcspApi, AcspProfileDocument existingDocument) {
        return null;
    }
}
