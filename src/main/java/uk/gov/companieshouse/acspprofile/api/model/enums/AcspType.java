package uk.gov.companieshouse.acspprofile.api.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AcspType {

    @JsonProperty("limited-company")
    LIMITED_COMPANY("limited-company"),
    @JsonProperty("limited-partnership")
    LIMITED_PARTNERSHIP("limited-partnership"),
    @JsonProperty("limited-liability-partnership")
    LIMITED_LIABILITY_PARTNERSHIP("limited-liability-partnership"),
    @JsonProperty("non-registered-partnership")
    NON_REGISTERED_PARTNERSHIP("non-registered-partnership"),
    @JsonProperty("sole-trader")
    SOLE_TRADER("sole-trader"),
    @JsonProperty("unincorporated-entity")
    UNINCORPORATED_ENTITY("unincorporated-entity"),
    @JsonProperty("corporate-body")
    CORPORATE_BODY("corporate-body");

    private final String value;

    AcspType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
