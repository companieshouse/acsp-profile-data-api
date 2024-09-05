package uk.gov.companieshouse.acspprofile.api.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AcspStatus {

    @JsonProperty("active")
    ACTIVE("active"),
    @JsonProperty("suspended")
    SUSPENDED("suspended"),
    @JsonProperty("ceased")
    CEASED("ceased");

    private final String value;

    AcspStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}