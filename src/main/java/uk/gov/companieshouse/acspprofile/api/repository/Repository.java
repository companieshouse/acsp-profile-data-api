package uk.gov.companieshouse.acspprofile.api.repository;

import java.util.Optional;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;

public interface Repository {

    Optional<AcspProfileDocument> findAscp(String acspNumber);
}
