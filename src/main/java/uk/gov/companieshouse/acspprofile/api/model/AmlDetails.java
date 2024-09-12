package uk.gov.companieshouse.acspprofile.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;

public class AmlDetails {

    @Field("supervisory_body")
    @JsonProperty("supervisory_body")
    private String supervisoryBody;
    @Field("membership_details")
    @JsonProperty("membership_details")
    private String membershipDetails;

    public String getSupervisoryBody() {
        return supervisoryBody;
    }

    public AmlDetails supervisoryBody(String supervisoryBody) {
        this.supervisoryBody = supervisoryBody;
        return this;
    }

    public String getMembershipDetails() {
        return membershipDetails;
    }

    public AmlDetails membershipDetails(String membershipDetails) {
        this.membershipDetails = membershipDetails;
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
        AmlDetails that = (AmlDetails) o;
        return Objects.equals(supervisoryBody, that.supervisoryBody) && Objects.equals(
                membershipDetails, that.membershipDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supervisoryBody, membershipDetails);
    }

    @Override
    public String toString() {
        return "AmlDetails{" +
                "supervisoryBody=" + supervisoryBody +
                ", membershipDetails='" + membershipDetails + '\'' +
                '}';
    }
}
