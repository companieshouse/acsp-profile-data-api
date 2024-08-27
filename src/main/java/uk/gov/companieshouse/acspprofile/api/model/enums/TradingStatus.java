package uk.gov.companieshouse.acspprofile.api.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TradingStatus {

    @JsonProperty("active")
    ACTIVE("active"),
    @JsonProperty("suspended")
    SUSPENDED("suspended"),
    @JsonProperty("ceased")
    CEASED("ceased");

    private final String value;

    TradingStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
