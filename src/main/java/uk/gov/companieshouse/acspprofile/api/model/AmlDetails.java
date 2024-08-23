package uk.gov.companieshouse.acspprofile.api.model;

import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.acspprofile.api.model.enums.SupervisoryBody;

public class AmlDetails {

    @Field("aml_supervisory_body")
    private SupervisoryBody supervisoryBody;
    @Field("aml_membership_details")
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
}
