package uk.gov.companieshouse.acspprofile.api.model.enums;

public enum AcspType {

    LIMITED_COMPANY("limited-company"),
    LIMITED_PARTNERSHIP("limited-partnership"),
    LIMITED_LIABILITY_PARTNERSHIP("limited-liability-partnership"),
    NON_REGISTERED_PARTNERSHIP("non-registered-partnership"),
    SOLE_TRADER("sole-trader"),
    UNINCORPORATED_ENTITY("unincorporated-entity"),
    CORPORATE_BODY("corporate-body");

    private final String value;

    AcspType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
