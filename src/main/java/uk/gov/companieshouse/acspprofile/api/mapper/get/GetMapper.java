package uk.gov.companieshouse.acspprofile.api.mapper.get;

import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;

public interface GetMapper {

    AcspProfile mapProfile(AcspProfileDocument document);
    AcspFullProfile mapFullProfile(AcspProfileDocument document);
}
