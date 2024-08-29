package uk.gov.companieshouse.acspprofile.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;

public class SoleTraderDetails {

    private String forename;
    @Field("forename_2")
    @JsonProperty("forename_2")
    private String forename2;
    private String surname;
    private String nationality;
    @Field("usual_residential_country")
    @JsonProperty("usual_residential_country")
    private String usualResidentialCountry;

    public String getForename() {
        return forename;
    }

    public SoleTraderDetails forename(String forename) {
        this.forename = forename;
        return this;
    }

    public String getForename2() {
        return forename2;
    }

    public SoleTraderDetails forename2(String forename2) {
        this.forename2 = forename2;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public SoleTraderDetails surname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getNationality() {
        return nationality;
    }

    public SoleTraderDetails nationality(String nationality) {
        this.nationality = nationality;
        return this;
    }

    public String getUsualResidentialCountry() {
        return usualResidentialCountry;
    }

    public SoleTraderDetails usualResidentialCountry(String usualResidentialCountry) {
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
        SoleTraderDetails that = (SoleTraderDetails) o;
        return Objects.equals(forename, that.forename) && Objects.equals(forename2, that.forename2)
                && Objects.equals(surname, that.surname) && Objects.equals(nationality,
                that.nationality) && Objects.equals(usualResidentialCountry, that.usualResidentialCountry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(forename, forename2, surname, nationality, usualResidentialCountry);
    }

    @Override
    public String toString() {
        return "SoleTraderDetails{" +
                "forename='" + forename + '\'' +
                ", forename2='" + forename2 + '\'' +
                ", surname='" + surname + '\'' +
                ", nationality='" + nationality + '\'' +
                ", usualResidentialCountry='" + usualResidentialCountry + '\'' +
                '}';
    }
}
