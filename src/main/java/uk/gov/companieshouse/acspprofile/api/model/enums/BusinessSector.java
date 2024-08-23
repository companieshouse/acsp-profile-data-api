package uk.gov.companieshouse.acspprofile.api.model.enums;

public enum BusinessSector {

    CASINOS("casinos"),
    HIGH_VALUE_DEALERS("high-value-dealers"),
    ESTATE_AGENTS("estate-agents"),
    FINANCIAL_INSTITUTIONS("financial-institutions"),
    CREDIT_INSTITUTIONS("credit-institutions"),
    TRUST_OR_COMPANY_SERVICE_PROVIDERS("trust-or-company-service-providers"),
    INDEPENDENT_LEGAL_PROFESSIONALS("independent-legal-professionals"),
    AUDITORS_INSOLVENCY_PRACTITIONERS_EXTERNAL_ACCOUNTANTS_AND_TAX_ADVISERS("auditors-insolvency-practitioners-external-accountants-and-tax-advisers");

    private final String value;
    BusinessSector(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
