package uk.gov.companieshouse.acspprofile.api.model;

import org.springframework.data.mongodb.core.mapping.Field;

public class SoleTraderDetails {

    @Field("acsp_sole_trader_forename")
    private String soleTraderForename;
    @Field("acsp_sole_trader_forename_2")
    private String soleTraderForename2;
    @Field("acsp_sole_trader_surname")
    private String soleTraderSurname;
    @Field("acsp_sole_trader_date_of_birth")
    private String soleTraderDateOfBirth;
    @Field("acsp_sole_trader_nationality")
    private String soleTraderNationality;
    @Field("acsp_sole_trader_usual_residential_country")
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

    public String getSoleTraderDateOfBirth() {
        return soleTraderDateOfBirth;
    }

    public SoleTraderDetails soleTraderDateOfBirth(String soleTraderDateOfBirth) {
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
}
