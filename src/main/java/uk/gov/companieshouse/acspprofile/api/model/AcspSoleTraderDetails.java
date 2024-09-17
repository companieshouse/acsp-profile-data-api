package uk.gov.companieshouse.acspprofile.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;

public class AcspSoleTraderDetails {

    private String forename;
    @Field("other_forenames")
    @JsonProperty("other_forenames")
    private String otherForenames;
    private String surname;
    private String nationality;
    @Field("usual_residential_country")
    @JsonProperty("usual_residential_country")
    private String usualResidentialCountry;

    public String getForename() {
        return forename;
    }

    public AcspSoleTraderDetails forename(String forename) {
        this.forename = forename;
        return this;
    }

    public String getOtherForenames() {
        return otherForenames;
    }

    public AcspSoleTraderDetails otherForenames(String otherForenames) {
        this.otherForenames = otherForenames;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public AcspSoleTraderDetails surname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getNationality() {
        return nationality;
    }

    public AcspSoleTraderDetails nationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public String getUsualResidentialCountry() {
        return usualResidentialCountry;
    }

    public AcspSoleTraderDetails usualResidentialCountry(String usualResidentialCountry) {
        this.usualResidentialCountry = usualResidentialCountry;
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
        AcspSoleTraderDetails that = (AcspSoleTraderDetails) o;
        return Objects.equals(forename, that.forename) && Objects.equals(otherForenames,
                that.otherForenames) && Objects.equals(surname, that.surname) && Objects.equals(
                nationality, that.nationality) && Objects.equals(usualResidentialCountry,
                that.usualResidentialCountry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forename, otherForenames, surname, nationality, usualResidentialCountry);
    }

    @Override
    public String toString() {
        return "SoleTraderDetails{" +
                "forename='" + forename + '\'' +
                ", otherForenames='" + otherForenames + '\'' +
                ", surname='" + surname + '\'' +
                ", nationality='" + nationality + '\'' +
                ", usualResidentialCountry='" + usualResidentialCountry + '\'' +
                '}';
    }
}
