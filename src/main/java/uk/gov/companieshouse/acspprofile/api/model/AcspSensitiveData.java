package uk.gov.companieshouse.acspprofile.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;

public class AcspSensitiveData {

    private String email;
    @Field("date_of_birth")
    @JsonProperty("date_of_birth")
    private Instant dateOfBirth;

    public String getEmail() {
        return email;
    }

    public AcspSensitiveData email(String email) {
        this.email = email;
        return this;
    }

    public Instant getDateOfBirth() {
        return dateOfBirth;
    }

    public AcspSensitiveData dateOfBirth(Instant dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
        AcspSensitiveData that = (AcspSensitiveData) o;
        return Objects.equals(email, that.email) && Objects.equals(dateOfBirth, that.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, dateOfBirth);
    }

    @Override
    public String toString() {
        return "AcspSensitiveData{" +
                "email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
