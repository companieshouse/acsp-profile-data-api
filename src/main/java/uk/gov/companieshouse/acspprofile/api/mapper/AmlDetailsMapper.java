package uk.gov.companieshouse.acspprofile.api.mapper;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspAmlDetails;
import uk.gov.companieshouse.api.acspprofile.AmlDetailsItem;
import uk.gov.companieshouse.api.acspprofile.SupervisoryBody;

@Component
public class AmlDetailsMapper {

    public List<AmlDetailsItem> mapAmlDetailsResponse(List<AcspAmlDetails> acspAmlDetails) {
        return Optional.ofNullable(acspAmlDetails)
                .map(amlDetails -> amlDetails.stream()
                        .map(amlDetail -> new AmlDetailsItem()
                                .supervisoryBody(amlDetail.getSupervisoryBody() != null ?
                                        SupervisoryBody.fromValue(amlDetail.getSupervisoryBody()) : null)
                                .membershipDetails(amlDetail.getMembershipDetails()))
                        .toList())
                .filter(details -> !details.isEmpty())
                .orElse(null);
    }

    public List<AcspAmlDetails> mapAmlDetailsRequest(List<AmlDetailsItem> amlDetailsItems) {
        return Optional.ofNullable(amlDetailsItems)
                .map(amlDetails -> amlDetails.stream()
                        .map(amlDetail -> new AcspAmlDetails()
                                .supervisoryBody(amlDetail.getSupervisoryBody() != null ?
                                        amlDetail.getSupervisoryBody().getValue() : null)
                                .membershipDetails(amlDetail.getMembershipDetails()))
                        .toList())
                .filter(details -> !details.isEmpty())
                .orElse(null);
    }
}
