package uk.gov.companieshouse.acspprofile.api.mapper.get;

import static uk.gov.companieshouse.acspprofile.api.mapper.DateUtils.instantToString;

import java.util.List;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.filinghistory.AltCapitalDescriptionValue;
import uk.gov.companieshouse.api.filinghistory.CapitalDescriptionValue;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAltCapital;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileCapital;

@Component
public class CapitalDescriptionGetResponseMapper {

    public List<CapitalDescriptionValue> mapFilingHistoryCapital(List<ACSPProfileCapital> capital) {
        return capital != null ? capital.stream()
                .map(value -> new CapitalDescriptionValue()
                        .currency(value.getCurrency())
                        .date(instantToString(value.getDate()))
                        .figure(value.getFigure()))
                .toList() : null;
    }

    public List<AltCapitalDescriptionValue> mapFilingHistoryAltCapital(List<ACSPProfileAltCapital> capital) {
        return capital != null ? capital.stream()
                .map(value -> new AltCapitalDescriptionValue()
                        .currency(value.getCurrency())
                        .date(instantToString(value.getDate()))
                        .figure(value.getFigure())
                        .description(value.getDescription()))
                .toList() : null;
    }
}
