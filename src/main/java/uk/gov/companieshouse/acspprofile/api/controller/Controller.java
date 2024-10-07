package uk.gov.companieshouse.acspprofile.api.controller;

import static org.springframework.http.HttpHeaders.LOCATION;
import static uk.gov.companieshouse.acspprofile.api.AcspProfileApplication.NAMESPACE;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.companieshouse.acspprofile.api.logging.DataMapHolder;
import uk.gov.companieshouse.acspprofile.api.service.Service;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@RestController
public class Controller {

    private static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    private final Service service;

    public Controller(Service service) {
        this.service = service;
    }

    @GetMapping("/authorised-corporate-service-providers/{acsp_number}")
    public ResponseEntity<AcspProfile> getProfile(@PathVariable("acsp_number") final String acspNumber) {

        DataMapHolder.get().companyNumber(acspNumber);
        LOGGER.info("Processing GET ACSP profile", DataMapHolder.getLogMap());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getProfile(acspNumber));
    }

    @GetMapping("/authorised-corporate-service-providers/{acsp_number}/full-profile")
    public ResponseEntity<AcspFullProfile> getFullProfile(@PathVariable("acsp_number") final String acspNumber) {

        DataMapHolder.get().companyNumber(acspNumber);
        LOGGER.info("Processing GET ACSP full profile", DataMapHolder.getLogMap());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(service.getFullProfile(acspNumber));
    }

    @PutMapping("/authorised-corporate-service-providers/{acsp_number}/internal")
    public ResponseEntity<Void> upsertAcsp(@PathVariable("acsp_number") final String acspNumber,
            @RequestBody InternalAcspApi internalAcspApi) {

        DataMapHolder.get().companyNumber(acspNumber);
        LOGGER.info("Processing PUT ACSP request", DataMapHolder.getLogMap());

        service.upsertAcsp(acspNumber, internalAcspApi);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(LOCATION, "/authorised-corporate-service-providers/%s".formatted(acspNumber))
                .build();
    }
}
