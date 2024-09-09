package uk.gov.companieshouse.acspprofile.api.service;

import static uk.gov.companieshouse.acspprofile.api.AcspProfileApplication.NAMESPACE;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.exception.NotFoundException;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.mapper.get.GetMapper;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class AcspGetProcessor implements GetProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    private static final String NOT_FOUND_MESSAGE = "ACSP document not found";

    private final Service acspService;
    private final GetMapper acspGetmapper;

    public AcspGetProcessor(Service acspService, GetMapper acspGetmapper) {
        this.acspService = acspService;
        this.acspGetmapper = acspGetmapper;
    }

    @Override
    public AcspProfile getProfile(String acspNumber) {
        return acspGetmapper.mapProfile(
                acspService.findAcsp(acspNumber)
                        .orElseGet(() -> {
                            LOGGER.info(NOT_FOUND_MESSAGE, DataMapHolder.getLogMap());
                            throw new NotFoundException(NOT_FOUND_MESSAGE);
                        }));
    }

    @Override
    public AcspFullProfile getFullProfile(String acspNumber) {
        return acspGetmapper.mapFullProfile(
                acspService.findAcsp(acspNumber)
                        .orElseGet(() -> {
                            LOGGER.info(NOT_FOUND_MESSAGE, DataMapHolder.getLogMap());
                            throw new NotFoundException(NOT_FOUND_MESSAGE);
                        }));
    }
}
