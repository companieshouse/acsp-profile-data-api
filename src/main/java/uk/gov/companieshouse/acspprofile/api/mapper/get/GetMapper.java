package uk.gov.companieshouse.acspprofile.api.mapper.get;

import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;

public interface GetMapper {

    Object mapProfile(AcspProfileDocument document);
    Object mapFullProfile(AcspProfileDocument document);
}
