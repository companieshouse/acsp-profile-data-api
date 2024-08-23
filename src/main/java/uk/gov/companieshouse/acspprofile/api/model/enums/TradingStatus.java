package uk.gov.companieshouse.acspprofile.api.model.enums;

public enum TradingStatus {

    ACTIVE("active"),
    SUSPENDED("suspended"),
    CEASED("ceased");

    private final String value;

    TradingStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
