package uk.gov.companieshouse.acspprofile.api.model;

import java.time.Instant;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeltaTimeStamp that = (DeltaTimeStamp) o;
        return Objects.equals(at, that.at) && Objects.equals(by, that.by) && Objects.equals(
                type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(at, by, type);
    }

    @Override
    public String toString() {
        return "DeltaTimeStamp{" +
                "at=" + at +
                ", by='" + by + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
