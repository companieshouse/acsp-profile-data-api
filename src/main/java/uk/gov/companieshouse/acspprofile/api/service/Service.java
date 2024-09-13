package uk.gov.companieshouse.acspprofile.api.service;

import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;

public interface Service {

    AcspProfile getProfile(String acspNumber);
    AcspFullProfile getFullProfile(String acspNumber);
}
