package uk.gov.companieshouse.acspprofile.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;

public class SoleTraderDetails {

    @Field("acsp_sole_trader_forename")
    @JsonProperty("acsp_sole_trader_forename")
    private String soleTraderForename;
    @Field("acsp_sole_trader_forename_2")
    @JsonProperty("acsp_sole_trader_forename_2")
    private String soleTraderForename2;
    @Field("acsp_sole_trader_surname")
    @JsonProperty("acsp_sole_trader_surname")
    private String soleTraderSurname;
    @Field("acsp_sole_trader_date_of_birth")
    @JsonProperty("acsp_sole_trader_date_of_birth")
    private Instant soleTraderDateOfBirth;
    @Field("acsp_sole_trader_nationality")
    @JsonProperty("acsp_sole_trader_nationality")
    private String soleTraderNationality;
    @Field("acsp_sole_trader_usual_residential_country")
    @JsonProperty("acsp_sole_trader_usual_residential_country")
    private String soleTraderUsualResidentialCountry;

    public String getSoleTraderForename() {
        return soleTraderForename;
    }

    public SoleTraderDetails soleTraderForename(String soleTraderForename) {
        this.soleTraderForename = soleTraderForename;
        return this;
    }

    public String getSoleTraderForename2() {
        return soleTraderForename2;
    }

    public SoleTraderDetails soleTraderForename2(String soleTraderForename2) {
        this.soleTraderForename2 = soleTraderForename2;
        return this;
    }

    public String getSoleTraderSurname() {
        return soleTraderSurname;
    }

    public SoleTraderDetails soleTraderSurname(String soleTraderSurname) {
        this.soleTraderSurname = soleTraderSurname;
        return this;
    }

    public Instant getSoleTraderDateOfBirth() {
        return soleTraderDateOfBirth;
    }

    public SoleTraderDetails soleTraderDateOfBirth(Instant soleTraderDateOfBirth) {
        this.soleTraderDateOfBirth = soleTraderDateOfBirth;
        return this;
    }

    public String getSoleTraderNationality() {
        return soleTraderNationality;
    }

    public SoleTraderDetails soleTraderNationality(String soleTraderNationality) {
        this.soleTraderNationality = soleTraderNationality;
        return this;
    }

    public String getSoleTraderUsualResidentialCountry() {
        return soleTraderUsualResidentialCountry;
    }

    public SoleTraderDetails soleTraderUsualResidentialCountry(String soleTraderUsualResidentialCountry) {
        this.soleTraderUsualResidentialCountry = soleTraderUsualResidentialCountry;
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
        return Objects.equals(soleTraderForename, that.soleTraderForename) && Objects.equals(
                soleTraderForename2, that.soleTraderForename2) && Objects.equals(soleTraderSurname,
                that.soleTraderSurname) && Objects.equals(soleTraderDateOfBirth, that.soleTraderDateOfBirth)
                && Objects.equals(soleTraderNationality, that.soleTraderNationality) && Objects.equals(
                soleTraderUsualResidentialCountry, that.soleTraderUsualResidentialCountry);
    }

    @Override
    public int hashCode() {
        return Objects.hash(soleTraderForename, soleTraderForename2, soleTraderSurname, soleTraderDateOfBirth,
                soleTraderNationality, soleTraderUsualResidentialCountry);
    }

    @Override
    public String toString() {
        return "SoleTraderDetails{" +
                "soleTraderForename='" + soleTraderForename + '\'' +
                ", soleTraderForename2='" + soleTraderForename2 + '\'' +
                ", soleTraderSurname='" + soleTraderSurname + '\'' +
                ", soleTraderDateOfBirth=" + soleTraderDateOfBirth +
                ", soleTraderNationality='" + soleTraderNationality + '\'' +
                ", soleTraderUsualResidentialCountry='" + soleTraderUsualResidentialCountry + '\'' +
                '}';
    }
}
