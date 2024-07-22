package uk.gov.companieshouse.acspprofile.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FilingHistoryStatusServiceIT {

    @Autowired
    private FilingHistoryStatusService filingHistoryStatusService;

    @ParameterizedTest
    @CsvSource({
            "AC999999 , acsp-profile-not-available-assurance-company-from-2004",
            "AC001838 , acsp-profile-available-assurance-company-before-2004",
            "BR999999 , acsp-profile-available",
            "ES999999 , acsp-profile-available",
            "FC999999 , acsp-profile-available",
            "FE999999 , acsp-profile-available",
            "GE999999 , acsp-profile-available",
            "GN999999 , acsp-profile-available",
            "GS999999 , acsp-profile-available",
            "IC999999 , acsp-profile-not-available-investment-company-with-variable-capital",
            "IP999999 , acsp-profile-not-available-industrial-and-provident-society",
            "LP999999 , acsp-profile-available-limited-partnership-from-2014",
            "LP003369 , acsp-profile-not-available-limited-partnership-before-1988",
            "LP015996 , acsp-profile-available-no-images-limited-partnership-from-1988",
            "NA999999 , acsp-profile-not-available-assurance-company-from-2004",
            "NA000001 , acsp-profile-not-available-assurance-company-from-2004",
            "NC999999 , acsp-profile-available",
            "NF999999 , acsp-profile-available",
            "NI999999 , acsp-profile-available",
            "NL999999 , acsp-profile-available-limited-partnership-from-2014",
            "NL000010 , acsp-profile-not-available-limited-partnership-before-1988",
            "NL000075 , acsp-profile-available-no-images-limited-partnership-from-1988",
            "NO999999 , acsp-profile-not-available-northern-ireland-other-industrial-and-provident-society",
            "NP999999 , acsp-profile-not-available-northern-ireland-industrial-and-provident-society",
            "NR999999 , acsp-profile-not-available-royal-charter",
            "NV999999 , acsp-profile-not-available-investment-company-with-variable-capital",
            "NZ999999 , acsp-profile-available",
            "OC999999 , acsp-profile-available",
            "OE999999 , acsp-profile-available",
            "PC999999 , acsp-profile-not-available-protected-cell-company",
            "R0999999 , acsp-profile-available",
            "RC999999 , acsp-profile-not-available-royal-charter",
            "SA999999 , acsp-profile-not-available-assurance-company-from-2004",
            "SA144517 , acsp-profile-available-assurance-company-before-2004",
            "SC999999 , acsp-profile-available",
            "SE999999 , acsp-profile-available",
            "SF999999 , acsp-profile-available",
            "SG999999 , acsp-profile-available",
            "SI999999 , acsp-profile-not-available-investment-company-with-variable-capital",
            "SL999999 , acsp-profile-available-limited-partnership-from-2014",
            "SL001082 , acsp-profile-not-available-limited-partnership-before-1988",
            "SL016383 , acsp-profile-available-no-images-limited-partnership-from-1988",
            "SO999999 , acsp-profile-available",
            "SP999999 , acsp-profile-not-available-scottish-industrial-and-provident-society",
            "SR999999 , acsp-profile-not-available-royal-charter",
            "SZ999999 , acsp-profile-available",
            "ZC999999 , acsp-profile-available",
            "12345678 , acsp-profile-available",
            "AA999999 , acsp-profile-not-available-unknown-prefix",
            "ABC12345 , acsp-profile-not-available-invalid-format"
    })
    void shouldCorrectlyReturnStatusFromGivenCompanyNumber(final String companyNumber, final String expected) {
        // given

        // when
        final String actual = filingHistoryStatusService.processStatus(companyNumber);

        // then
        assertEquals(expected, actual);
    }
}
