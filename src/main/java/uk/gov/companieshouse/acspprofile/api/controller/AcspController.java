package uk.gov.companieshouse.acspprofile.api.controller;

import static uk.gov.companieshouse.acspprofile.api.AcspProfileApplication.NAMESPACE;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.service.GetProcessor;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@RestController
public class AcspController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    private final GetProcessor getProcessor;

    public AcspController(GetProcessor getProcessor) {
        this.getProcessor = getProcessor;
    }

    @GetMapping("/authorised-corporate-service-providers/{acsp_number}/full-profile")
    public ResponseEntity<Object> getFullProfile(@PathVariable("acsp_number") final String acspNumber) {

        DataMapHolder.get().companyNumber(acspNumber);
        LOGGER.info("Processing GET ACSP full profile", DataMapHolder.getLogMap());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(getProcessor.getFullProfile(acspNumber));
    }
}