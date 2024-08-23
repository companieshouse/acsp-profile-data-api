package uk.gov.companieshouse.acspprofile.api.model;

import java.time.Instant;

public class DeltaTimeStamp {

    private Instant at;
    private String by;
    private String type;

    public Instant getAt() {
        return at;
    }

    public DeltaTimeStamp at(Instant at) {
        this.at = at;
        return this;
    }

    public String getBy() {
        return by;
    }

    public DeltaTimeStamp by(String by) {
        this.by = by;
        return this;
    }

    public String getType() {
        return type;
    }

    public DeltaTimeStamp type(String type) {
        this.type = type;
        return this;
    }
}
