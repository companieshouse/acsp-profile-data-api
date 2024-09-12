package uk.gov.companieshouse.acspprofile.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;

public class AcspAddress {

    @Field("care_of")
    @JsonProperty("care_of")
    private String careOf;
    @Field("address_line_1")
    @JsonProperty("address_line_1")
    private String addressLine1;
    @Field("address_line_2")
    @JsonProperty("address_line_2")
    private String addressLine2;
    private String country;
    private String locality;
    @Field("po_box")
    @JsonProperty("po_box")
    private String poBox;
    @Field("postal_code")
    @JsonProperty("postal_code")
    private String postalCode;
    private String premises;
    private String region;

    public String getCareOf() {
        return careOf;
    }

    public AcspAddress careOf(String careOf) {
        this.careOf = careOf;
        return this;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public AcspAddress addressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
        return this;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public AcspAddress addressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public AcspAddress country(String country) {
        this.country = country;
        return this;
    }

    public String getLocality() {
        return locality;
    }

    public AcspAddress locality(String locality) {
        this.locality = locality;
        return this;
    }

    public String getPoBox() {
        return poBox;
    }

    public AcspAddress poBox(String poBox) {
        this.poBox = poBox;
        return this;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public AcspAddress postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public String getPremises() {
        return premises;
    }

    public AcspAddress premises(String premises) {
        this.premises = premises;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public AcspAddress region(String region) {
        this.region = region;
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
        AcspAddress that = (AcspAddress) o;
        return Objects.equals(careOf, that.careOf) && Objects.equals(addressLine1, that.addressLine1)
                && Objects.equals(addressLine2, that.addressLine2) && Objects.equals(country,
                that.country) && Objects.equals(locality, that.locality) && Objects.equals(poBox,
                that.poBox) && Objects.equals(postalCode, that.postalCode) && Objects.equals(premises,
                that.premises) && Objects.equals(region, that.region);
    }

    @Override
    public int hashCode() {
        return Objects.hash(careOf, addressLine1, addressLine2, country, locality, poBox, postalCode, premises, region);
    }

    @Override
    public String toString() {
        return "AcspAddress{" +
                "careOf='" + careOf + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", country='" + country + '\'' +
                ", locality='" + locality + '\'' +
                ", poBox='" + poBox + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", premises='" + premises + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
