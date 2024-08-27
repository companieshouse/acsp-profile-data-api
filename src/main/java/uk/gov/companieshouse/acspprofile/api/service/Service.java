package uk.gov.companieshouse.acspprofile.api.service;

import java.util.Optional;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;

public interface Service {

    Optional<AcspProfileDocument> findAcsp(String acspNumber);
}
