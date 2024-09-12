package uk.gov.companieshouse.acspprofile.api.service;

import static uk.gov.companieshouse.acspprofile.api.AcspProfileApplication.NAMESPACE;

import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.exception.BadGatewayException;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.acspprofile.api.repository.Repository;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class AcspService implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    private final Repository acspRepository;

    public AcspService(Repository acspRepository) {
        this.acspRepository = acspRepository;
    }

    @Override
    public Optional<AcspProfileDocument> findAcsp(String acspNumber) {
        try {
            return acspRepository.findById(acspNumber);
        } catch (TransientDataAccessException ex) {
            LOGGER.info("Recoverable MongoDB error during find", DataMapHolder.getLogMap());
            throw new BadGatewayException("Recoverable MongoDB error during find", ex);
        } catch (DataAccessException ex) {
            LOGGER.error("MongoDB error during find", ex, DataMapHolder.getLogMap());
            throw new BadGatewayException("MongoDB error during find", ex);
        }
    }
}
