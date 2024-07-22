package uk.gov.companieshouse.acspprofile.api.model.mongo;

import java.time.Instant;
import java.util.Objects;

public class ACSPProfileDeltaTimestamp {

    private Instant at;
    private String by;


    public Instant getAt() {
        return at;
    }

    public ACSPProfileDeltaTimestamp at(Instant at) {
        this.at = at;
        return this;
    }

    public String getBy() {
        return by;
    }

    public ACSPProfileDeltaTimestamp by(String by) {
        this.by = by;
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
        ACSPProfileDeltaTimestamp that = (ACSPProfileDeltaTimestamp) o;
        return Objects.equals(at, that.at) && Objects.equals(by, that.by);
    }

    @Override
    public int hashCode() {
        return Objects.hash(at, by);
    }

    @Override
    public String toString() {
        return "FilingHistoryDeltaTimestamp{" +
                "at=" + at +
                ", by='" + by + '\'' +
                '}';
    }
}
