package uk.gov.companieshouse.acspprofile.api.service;

import java.util.Map;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.statusrules.PrefixProperties;
import uk.gov.companieshouse.acspprofile.api.model.statusrules.StatusRuleProperties;
import uk.gov.companieshouse.acspprofile.api.service.CompanyNumberStatusProcessor.CompanyNumberAffixes;

@Component
public class ACSPProfileStatusService implements StatusService {

    private final StatusRuleProperties statusRuleProperties;
    private final CompanyNumberStatusProcessor companyNumberStatusProcessor;

    public ACSPProfileStatusService(StatusRuleProperties statusRuleProperties,
                                    CompanyNumberStatusProcessor companyNumberStatusProcessor) {
        this.statusRuleProperties = statusRuleProperties;
        this.companyNumberStatusProcessor = companyNumberStatusProcessor;
    }

    @Override
    public String processStatus(String companyNumber) {
        CompanyNumberAffixes companyNumberAffixes = companyNumberStatusProcessor
                .splitCompanyNumberAffixes(companyNumber);

        Map<String, PrefixProperties> filingHistory = statusRuleProperties.filingHistory();
        PrefixProperties prefixProperties = filingHistory
                .getOrDefault(companyNumberAffixes.prefix(), filingHistory.get("UNKNOWN_PREFIX"));

        return companyNumberStatusProcessor
                .getStatusFromPrefixProperties(prefixProperties, companyNumberAffixes.suffix());
    }
}

