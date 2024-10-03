package uk.gov.companieshouse.acspprofile.api.service;

import static uk.gov.companieshouse.acspprofile.api.AcspProfileApplication.NAMESPACE;
import static uk.gov.companieshouse.acspprofile.api.mapper.DateUtils.isDeltaStale;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.exception.ConflictException;
import uk.gov.companieshouse.acspprofile.api.exception.NotFoundException;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.mapper.RequestMapper;
import uk.gov.companieshouse.acspprofile.api.mapper.ResponseMapper;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.acspprofile.api.repository.Repository;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class AcspService implements Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    private static final String NOT_FOUND_MESSAGE = "ACSP document not found";
    private static final String STALE_DELTA_MESSAGE = "Stale delta received, request delta_at: [%s], existing delta_at: [%s]";

    private final Repository repository;
    private final ResponseMapper responseMapper;
    private final RequestMapper requestMapper;

    public AcspService(Repository repository, ResponseMapper responseMapper, RequestMapper requestMapper) {
        this.repository = repository;
        this.responseMapper = responseMapper;
        this.requestMapper = requestMapper;
    }

    @Override
    public AcspProfile getProfile(String acspNumber) {
        return responseMapper.mapProfile(doFindAcsp(acspNumber));
    }

    @Override
    public AcspFullProfile getFullProfile(String acspNumber) {
        return responseMapper.mapFullProfile(doFindAcsp(acspNumber));
    }

    @Override
    public void upsertAcsp(String acspNumber, InternalAcspApi internalAcspApi) {
        repository.findAcsp(acspNumber)
                .ifPresentOrElse(
                        document -> {
                            if (isDeltaStale(internalAcspApi.getInternalData().getDeltaAt(), document.getDeltaAt())) {
                                String errorMsg = STALE_DELTA_MESSAGE.formatted(
                                        internalAcspApi.getInternalData().getDeltaAt(),
                                        document.getDeltaAt());
                                LOGGER.error(errorMsg, DataMapHolder.getLogMap());
                                throw new ConflictException(errorMsg);
                            }
                            repository.updateAcsp(requestMapper.mapExistingAcsp(internalAcspApi, document));
                            LOGGER.info("Successfully updated ACSP document", DataMapHolder.getLogMap());
                        },
                        () -> {
                            repository.insertAcsp(requestMapper.mapNewAcsp(internalAcspApi));
                            LOGGER.info("Successfully inserted ACSP document", DataMapHolder.getLogMap());
                        });
    }

    private AcspProfileDocument doFindAcsp(String acspNumber) {
        AcspProfileDocument document = repository.findAcsp(acspNumber)
                .orElseGet(() -> {
                    LOGGER.info(NOT_FOUND_MESSAGE, DataMapHolder.getLogMap());
                    throw new NotFoundException(NOT_FOUND_MESSAGE);
                });
        LOGGER.info("Successfully found ACSP document", DataMapHolder.getLogMap());
        return document;
    }
}
