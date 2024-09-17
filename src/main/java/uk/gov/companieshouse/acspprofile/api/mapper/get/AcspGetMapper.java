package uk.gov.companieshouse.acspprofile.api.mapper.get;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspData;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;
import uk.gov.companieshouse.api.acspprofile.Links;
import uk.gov.companieshouse.api.acspprofile.Status;
import uk.gov.companieshouse.api.acspprofile.Type;

@Component
public class AcspGetMapper implements GetMapper {

    private static final String KIND = "authorised-corporate-service-provider-info";

    @Override
    public AcspProfile mapProfile(AcspProfileDocument document) {
        AcspData data = document.getData();
        return new AcspProfile()
                .number(data.getAcspNumber())
                .name(data.getName())
                .type(Type.fromValue(data.getType()))
                .kind(KIND)
                .status(Status.fromValue(data.getStatus()))
                .links(new Links()
                        .self(data.getLinks().getSelf()));
    }

    @Override
    public AcspFullProfile mapFullProfile(AcspProfileDocument document) {
        // not implemented
        throw new UnsupportedOperationException("Mapping of full profile not yet implemented");
    }
}
