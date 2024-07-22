package uk.gov.companieshouse.acspprofile.api.model.statusrules;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record StatusRuleProperties(@JsonProperty("acsp-profile") Map<String, PrefixProperties> filingHistory) {

}
