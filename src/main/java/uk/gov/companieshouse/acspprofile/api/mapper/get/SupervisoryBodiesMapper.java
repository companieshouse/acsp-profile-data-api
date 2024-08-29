package uk.gov.companieshouse.acspprofile.api.mapper.get;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspData;
import uk.gov.companieshouse.api.acspprofile.SupervisoryBody;

@Component
public class SupervisoryBodiesMapper {

    public List<SupervisoryBody> map(AcspData data) {
        return Optional.ofNullable(data.getAmlDetails())
                .map(amlDetails -> amlDetails.stream()
                        .map(amlDetail -> new SupervisoryBody()) // TODO
                        .toList())
                .orElse(null);
    }
}
