package uk.gov.companieshouse.acspprofile.api.service;

import static uk.gov.companieshouse.acspprofile.api.AcspProfileApplication.NAMESPACE;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.exception.NotFoundException;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.mapper.ResponseMapper;
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

    private final Repository repository;
    private final ResponseMapper responseMapper;

    public AcspService(Repository repository, ResponseMapper responseMapper) {
        this.repository = repository;
        this.responseMapper = responseMapper;
    }

    @Override
    public AcspProfile getProfile(String acspNumber) {
        return responseMapper.mapProfile(
                repository.findAcsp(acspNumber)
                        .orElseGet(() -> {
                            LOGGER.info(NOT_FOUND_MESSAGE, DataMapHolder.getLogMap());
                            throw new NotFoundException(NOT_FOUND_MESSAGE);
                        }));
    }

    @Override
    public AcspFullProfile getFullProfile(String acspNumber) {
        return responseMapper.mapFullProfile(
                repository.findAcsp(acspNumber)
                        .orElseGet(() -> {
                            LOGGER.info(NOT_FOUND_MESSAGE, DataMapHolder.getLogMap());
                            throw new NotFoundException(NOT_FOUND_MESSAGE);
                        }));
    }

    @Override
    public void upsertAcsp(String acspNumber, InternalAcspApi internalAcspApi) {

    }
}
