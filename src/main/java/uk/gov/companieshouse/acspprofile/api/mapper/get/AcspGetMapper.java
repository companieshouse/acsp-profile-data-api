package uk.gov.companieshouse.acspprofile.api.mapper.get;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;

@Component
public class AcspGetMapper implements GetMapper {

    @Override
    public AcspProfile mapProfile(AcspProfileDocument document) {
        // not implemented
        return null;
    }

    @Override
    public AcspFullProfile mapFullProfile(AcspProfileDocument document) {
        // not implemented
        return null;
    }
}
