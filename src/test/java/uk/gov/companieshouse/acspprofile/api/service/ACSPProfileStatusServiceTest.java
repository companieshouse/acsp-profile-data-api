package uk.gov.companieshouse.acspprofile.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.acspprofile.api.model.statusrules.PrefixProperties;
import uk.gov.companieshouse.acspprofile.api.model.statusrules.StatusRuleProperties;
import uk.gov.companieshouse.acspprofile.api.service.CompanyNumberStatusProcessor.CompanyNumberAffixes;

@ExtendWith(MockitoExtension.class)
class ACSPProfileStatusServiceTest {

    @InjectMocks
    private ACSPProfileStatusService ACSPProfileStatusService;

    @Mock
    private StatusRuleProperties statusRuleProperties;
    @Mock
    private CompanyNumberStatusProcessor companyNumberStatusProcessor;

    @Test
    void shouldSuccessfullyProcessStatus() {
        // given
        PrefixProperties prefixProperties = new PrefixProperties(
                "type",
                "status",
                null);

        final String prefix = "AB";
        final String suffix = "123456";
        final String companyNumber = prefix + suffix;

        CompanyNumberAffixes companyNumberAffixes = new CompanyNumberAffixes(prefix, suffix);

        when(companyNumberStatusProcessor.splitCompanyNumberAffixes(any())).thenReturn(companyNumberAffixes);
        when(statusRuleProperties.filingHistory()).thenReturn(Map.of(prefix, prefixProperties));
        when(companyNumberStatusProcessor.getStatusFromPrefixProperties(any(), any())).thenReturn(prefixProperties.status());

        final String expected = "status";

        // when
        final String actual = ACSPProfileStatusService.processStatus(companyNumber);

        // then
        assertEquals(expected, actual);
    }
}
