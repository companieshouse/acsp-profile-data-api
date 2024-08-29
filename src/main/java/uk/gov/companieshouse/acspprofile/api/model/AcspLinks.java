package uk.gov.companieshouse.acspprofile.api.model;

import java.util.Objects;

public class AcspLinks {

    private String self;

    public String getSelf() {
        return self;
    }

    public AcspLinks self(String self) {
        this.self = self;
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
        AcspLinks acspLinks = (AcspLinks) o;
        return Objects.equals(self, acspLinks.self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(self);
    }

    @Override
    public String toString() {
        return "AcspLinks{" +
                "self='" + self + '\'' +
                '}';
    }
}
