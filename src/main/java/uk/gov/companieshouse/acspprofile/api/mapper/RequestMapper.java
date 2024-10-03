package uk.gov.companieshouse.acspprofile.api.mapper;

import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;

public interface RequestMapper {

    AcspProfileDocument mapNewAcsp(InternalAcspApi internalAcsp);
    AcspProfileDocument mapExistingAcsp(InternalAcspApi internalAcspApi, AcspProfileDocument existingDocument);
}
