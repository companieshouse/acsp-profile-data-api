package uk.gov.companieshouse.acspprofile.api.service;

import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;

public interface Service {

    AcspProfile getProfile(String acspNumber);
    AcspFullProfile getFullProfile(String acspNumber);
    void upsertAcsp(String acspNumber, InternalAcspApi internalAcspApi);
}
