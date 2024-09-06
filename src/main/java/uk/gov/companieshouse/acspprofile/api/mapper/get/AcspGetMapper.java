package uk.gov.companieshouse.acspprofile.api.mapper.get;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspData;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.AcspProfile;
import uk.gov.companieshouse.api.acspprofile.Links;

@Component
public class AcspGetMapper implements GetMapper {

    private static final String KIND = "authorised-corporate-service-provider-info";

    @Override
    public AcspProfile mapProfile(AcspProfileDocument document) {
        AcspData data = document.getData();
        return new AcspProfile()
                .number(data.getAcspNumber())
                .name(data.getName())
                .type(AcspProfile.TypeEnum.fromValue(data.getType().getValue()))
                .kind(KIND)
                .status(AcspProfile.StatusEnum.fromValue(data.getStatus().getValue()))
                .links(new Links()
                        .self(data.getLinks().getSelf()));
    }

    @Override
    public AcspFullProfile mapFullProfile(AcspProfileDocument document) {
        // not implemented
        return null;
    }
}
