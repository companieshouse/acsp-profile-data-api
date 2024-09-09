package uk.gov.companieshouse.acspprofile.api.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AcspCountry {

    @JsonProperty("wales")
    WALES("wales"),
    @JsonProperty("england")
    ENGLAND("england"),
    @JsonProperty("scotland")
    SCOTLAND("scotland"),
    @JsonProperty("great-britain")
    GREAT_BRITAIN("great-britain"),
    @JsonProperty("not-specified")
    NOT_SPECIFIED("not-specified"),
    @JsonProperty("united-kingdom")
    UNITED_KINGDOM("united-kingdom"),
    @JsonProperty("northern-ireland")
    NORTHERN_IRELAND("northern-ireland");

    private final String value;

    AcspCountry(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
