package uk.gov.companieshouse.acspprofile.api.repository;

import static uk.gov.companieshouse.acspprofile.api.AcspProfileApplication.NAMESPACE;

import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.exception.BadGatewayException;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class AcspRepository implements Repository {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    private static final String RECOVERABLE_ERROR_MESSAGE = "Recoverable MongoDB error during %s";
    private static final String ERROR_MESSAGE = "MongoDB error during %s";
    private static final String FIND = "find";
    private static final String INSERT = "insert";
    private static final String UPDATE = "update";

    private final AcspMongoRepository mongoRepository;

    public AcspRepository(AcspMongoRepository mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Optional<AcspProfileDocument> findAcsp(String acspNumber) {
        try {
            return mongoRepository.findById(acspNumber);
        } catch (TransientDataAccessException ex) {
            throw badGatewayAndLogInfo(FIND, ex);
        } catch (DataAccessException ex) {
            throw badGatewayAndLogError(FIND, ex);
        }
    }

    @Override
    public void insertAcsp(AcspProfileDocument document) {
        try {
            mongoRepository.insert(document);
        } catch (TransientDataAccessException ex) {
            throw badGatewayAndLogInfo(INSERT, ex);
        } catch (DataAccessException ex) {
            throw badGatewayAndLogError(INSERT, ex);
        }
    }

    @Override
    public void updateAcsp(AcspProfileDocument document) {
        try {
            mongoRepository.save(document);
        } catch (TransientDataAccessException ex) {
            throw badGatewayAndLogInfo(UPDATE, ex);
        } catch (DataAccessException ex) {
            throw badGatewayAndLogError(UPDATE, ex);
        }
    }

    private static BadGatewayException badGatewayAndLogInfo(String operation, TransientDataAccessException ex) {
        String errorMsg = RECOVERABLE_ERROR_MESSAGE.formatted(operation);
        LOGGER.info(errorMsg, DataMapHolder.getLogMap());
        return new BadGatewayException(errorMsg, ex);
    }

    private static BadGatewayException badGatewayAndLogError(String operation, DataAccessException ex) {
        String errorMsg = ERROR_MESSAGE.formatted(operation);
        LOGGER.error(errorMsg, ex, DataMapHolder.getLogMap());
        return new BadGatewayException(errorMsg, ex);
    }
}
