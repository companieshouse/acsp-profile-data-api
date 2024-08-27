package uk.gov.companieshouse.acspprofile.api.mapper;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;

@Component
public class AcspGetMapper implements GetMapper {

    @Override
    public Object mapProfile(AcspProfileDocument document) {
        // not implemented
        return null;
    }

    @Override
    public Object mapFullProfile(AcspProfileDocument document) {
        // not implemented
        return null;
    }
}
