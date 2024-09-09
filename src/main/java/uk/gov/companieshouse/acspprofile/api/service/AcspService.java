package uk.gov.companieshouse.acspprofile.api.service;

import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.acspprofile.api.repository.Repository;

@Component
public class AcspService implements Service {

    private final Repository acspRepository;

    public AcspService(Repository acspRepository) {
        this.acspRepository = acspRepository;
    }

    @Override
    public Optional<AcspProfileDocument> findAcsp(String acspNumber) {
        return acspRepository.findById(acspNumber);
    }
}
