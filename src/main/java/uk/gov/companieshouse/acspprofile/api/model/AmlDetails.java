package uk.gov.companieshouse.acspprofile.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.acspprofile.api.model.enums.SupervisoryBody;

public class AmlDetails {

    @Field("aml_supervisory_body")
	@JsonProperty("aml_supervisory_body")
    private SupervisoryBody supervisoryBody;
    @Field("aml_membership_details")
	@JsonProperty("aml_membership_details")
    private String membershipDetails;

    public SupervisoryBody getSupervisoryBody() {
        return supervisoryBody;
    }

    public AmlDetails supervisoryBody(SupervisoryBody supervisoryBody) {
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
        return supervisoryBody == that.supervisoryBody && Objects.equals(membershipDetails,
                that.membershipDetails);
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
