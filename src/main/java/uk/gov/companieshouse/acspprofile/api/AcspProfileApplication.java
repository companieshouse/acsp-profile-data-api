package uk.gov.companieshouse.acspprofile.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AcspProfileApplication {

    public static final String NAMESPACE = "acsp-profile-data-api";

    public static void main(String[] args) {
        SpringApplication.run(AcspProfileApplication.class, args);
    }
}
