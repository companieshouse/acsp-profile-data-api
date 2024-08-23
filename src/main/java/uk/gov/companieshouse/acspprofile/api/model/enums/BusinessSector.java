package uk.gov.companieshouse.acspprofile.api.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum BusinessSector {

    @JsonProperty("casinos")
	CASINOS("casinos"),
    @JsonProperty("high-value-dealers")
	HIGH_VALUE_DEALERS("high-value-dealers"),
    @JsonProperty("estate-agents")
	ESTATE_AGENTS("estate-agents"),
    @JsonProperty("financial-institutions")
	FINANCIAL_INSTITUTIONS("financial-institutions"),
    @JsonProperty("credit-institutions")
	CREDIT_INSTITUTIONS("credit-institutions"),
    @JsonProperty("trust-or-company-service-providers")
	TRUST_OR_COMPANY_SERVICE_PROVIDERS("trust-or-company-service-providers"),
    @JsonProperty("independent-legal-professionals")
	INDEPENDENT_LEGAL_PROFESSIONALS("independent-legal-professionals"),
    @JsonProperty("auditors-insolvency-practitioners-external-accountants-and-tax-advisers")
	AUDITORS_INSOLVENCY_PRACTITIONERS_EXTERNAL_ACCOUNTANTS_AND_TAX_ADVISERS("auditors-insolvency-practitioners-external-accountants-and-tax-advisers");

    private final String value;
    BusinessSector(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
