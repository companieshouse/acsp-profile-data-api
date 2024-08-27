package uk.gov.companieshouse.acspprofile.api.service;

import static uk.gov.companieshouse.acspprofile.api.AcspProfileApplication.NAMESPACE;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.exception.NotFoundException;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.mapper.get.GetMapper;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Component
public class AcspGetProcessor implements GetProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    private final Service acspService;
    private final GetMapper acspGetmapper;

    public AcspGetProcessor(Service acspService, GetMapper acspGetmapper) {
        this.acspService = acspService;
        this.acspGetmapper = acspGetmapper;
    }

    @Override
    public Object getProfile(String acspNumber) {
        // not implemented
        return null;
    }

    @Override
    public Object getFullProfile(String acspNumber) {
        return acspGetmapper.mapFullProfile(
                acspService.findAcsp(acspNumber)
                        .orElseGet(() -> {
                            LOGGER.info("ACSP profile not found", DataMapHolder.getLogMap());
                            throw new NotFoundException("ACSP profile not found");
                        }));
    }
}
