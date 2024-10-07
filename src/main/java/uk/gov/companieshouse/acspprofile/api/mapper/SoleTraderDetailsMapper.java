package uk.gov.companieshouse.acspprofile.api.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.acspprofile.api.model.AcspSoleTraderDetails;
import uk.gov.companieshouse.api.acspprofile.SoleTraderDetails;

@Component
public class SoleTraderDetailsMapper {

    public SoleTraderDetails mapSoleTraderDetailsResponse(AcspSoleTraderDetails acspSoleTraderDetails) {
        return Optional.ofNullable(acspSoleTraderDetails)
                .map(details -> new SoleTraderDetails()
                        .forename(details.getForename())
                        .otherForenames(details.getOtherForenames())
                        .surname(details.getSurname())
                        .nationality(details.getNationality())
                        .usualResidentialCountry(details.getUsualResidentialCountry()))
                .orElse(null);
    }

    public AcspSoleTraderDetails mapSoleTraderDetailsRequest(SoleTraderDetails soleTraderDetailsResponse) {
        return Optional.ofNullable(soleTraderDetailsResponse)
                .map(details -> new AcspSoleTraderDetails()
                        .forename(details.getForename())
                        .otherForenames(details.getOtherForenames())
                        .surname(details.getSurname())
                        .nationality(details.getNationality())
                        .usualResidentialCountry(details.getUsualResidentialCountry()))
                .orElse(null);
    }
}