package uk.gov.companieshouse.acspprofile.api.mapper.get;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspAmlDetails;
import uk.gov.companieshouse.api.acspprofile.AmlDetailsItem;
import uk.gov.companieshouse.api.acspprofile.SupervisoryBody;

@Component
public class AmlDetailsMapper {

    public List<AmlDetailsItem> mapAmlDetails(List<AcspAmlDetails> acspAmlDetails) {
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
}
