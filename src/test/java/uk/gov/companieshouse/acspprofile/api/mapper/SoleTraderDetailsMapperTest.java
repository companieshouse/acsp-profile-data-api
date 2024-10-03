package uk.gov.companieshouse.acspprofile.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.acspprofile.api.model.AcspSoleTraderDetails;
import uk.gov.companieshouse.api.acspprofile.SoleTraderDetails;

class SoleTraderDetailsMapperTest {

    private static final String FORENAME = "forename";
    private static final String OTHER_FORENAMES = "middle name";
    private static final String SURNAME = "surname";
    private static final String BRITISH = "british";
    private static final String USUAL_RESIDENTIAL_COUNTRY = "united kingdom";

    private final SoleTraderDetailsMapper soleTraderDetailsMapper = new SoleTraderDetailsMapper();

    @Test
    void mapAcspSoleTraderDetails() {
        // given
        AcspSoleTraderDetails acspSoleTraderDetails = new AcspSoleTraderDetails()
                .forename(FORENAME)
                .otherForenames(OTHER_FORENAMES)
                .surname(SURNAME)
                .nationality(BRITISH)
                .usualResidentialCountry(USUAL_RESIDENTIAL_COUNTRY);

        SoleTraderDetails expected = new SoleTraderDetails()
                .forename(FORENAME)
                .otherForenames(OTHER_FORENAMES)
                .surname(SURNAME)
                .nationality(BRITISH)
                .usualResidentialCountry(USUAL_RESIDENTIAL_COUNTRY);

        // when
        SoleTraderDetails actual = soleTraderDetailsMapper.mapAcspSoleTraderDetails(acspSoleTraderDetails);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void mapNullAcspSoleTraderDetails() {
        // given

        // when
        SoleTraderDetails actual = soleTraderDetailsMapper.mapAcspSoleTraderDetails(null);

        // then
        assertNull(actual);
    }
}