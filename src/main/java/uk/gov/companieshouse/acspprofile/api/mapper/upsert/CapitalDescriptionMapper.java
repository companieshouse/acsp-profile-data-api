package uk.gov.companieshouse.acspprofile.api.mapper.upsert;


import static uk.gov.companieshouse.acspprofile.api.mapper.DateUtils.stringToInstant;

import java.util.List;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.filinghistory.AltCapitalDescriptionValue;
import uk.gov.companieshouse.api.filinghistory.CapitalDescriptionValue;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAltCapital;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileCapital;

@Component
public class CapitalDescriptionMapper {

    public List<ACSPProfileCapital> mapCapitalDescriptionValueList(List<CapitalDescriptionValue> capital) {
        return capital != null ? capital.stream()
                .map(value -> new ACSPProfileCapital()
                        .currency(value.getCurrency())
                        .date(stringToInstant(value.getDate()))
                        .figure(value.getFigure()))
                .toList() : null;
    }

    public List<ACSPProfileAltCapital> mapAltCapitalDescriptionValueList(List<AltCapitalDescriptionValue> capital) {
        return capital != null ? capital.stream()
                .map(value -> new ACSPProfileAltCapital()
                        .currency(value.getCurrency())
                        .date(stringToInstant(value.getDate()))
                        .figure(value.getFigure())
                        .description(value.getDescription()))
                .toList() : null;
    }
}
