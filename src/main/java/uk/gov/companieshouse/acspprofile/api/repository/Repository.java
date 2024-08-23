package uk.gov.companieshouse.acspprofile.api.repository;

import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;

public interface Repository {

    AcspProfileDocument findAscp(String acspNumber);
}
