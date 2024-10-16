package uk.gov.companieshouse.acspprofile.api.mapper;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.acspprofile.api.model.AcspSoleTraderDetails;
import uk.gov.companieshouse.api.acspprofile.SoleTraderDetails;

class SoleTraderDetailsMapperTest {

    private static final String FORENAME = "forename";
    private static final String OTHER_FORENAMES = "middle name";
    private static final String SURNAME = "surname";
    private static final String BRITISH = "british";
    private static final String USUAL_RESIDENTIAL_COUNTRY = "united kingdom";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(2000, 8, 27);
    private static final Instant DATE_OF_BIRTH_INSTANT = Instant.from(DATE_OF_BIRTH.atStartOfDay(UTC));

    private final SoleTraderDetailsMapper soleTraderDetailsMapper = new SoleTraderDetailsMapper();

    @Test
    void mapAcspSoleTraderDetailsToResponse() {
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
                .usualResidentialCountry(USUAL_RESIDENTIAL_COUNTRY)
                .dateOfBirth(DATE_OF_BIRTH);

        // when
        SoleTraderDetails actual = soleTraderDetailsMapper.mapSoleTraderDetailsResponse(acspSoleTraderDetails,
                DATE_OF_BIRTH_INSTANT);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void mapNullAcspSoleTraderDetailsToResponse() {
        // given

        // when
        SoleTraderDetails actual = soleTraderDetailsMapper.mapSoleTraderDetailsResponse(null, null);

        // then
        assertNull(actual);
    }

    @Test
    void mapSoleTraderDetailsRequestToAcspSoleTraderDetails() {
        // given
        SoleTraderDetails soleTraderDetails = new SoleTraderDetails()
                .forename(FORENAME)
                .otherForenames(OTHER_FORENAMES)
                .surname(SURNAME)
                .nationality(BRITISH)
                .usualResidentialCountry(USUAL_RESIDENTIAL_COUNTRY);

        AcspSoleTraderDetails expected = new AcspSoleTraderDetails()
                .forename(FORENAME)
                .otherForenames(OTHER_FORENAMES)
                .surname(SURNAME)
                .nationality(BRITISH)
                .usualResidentialCountry(USUAL_RESIDENTIAL_COUNTRY);

        // when
        AcspSoleTraderDetails actual = soleTraderDetailsMapper.mapSoleTraderDetailsRequest(soleTraderDetails);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void mapNullSoleTraderDetailsRequestToAcspSoleTraderDetails() {
        // given

        // when
        AcspSoleTraderDetails actual = soleTraderDetailsMapper.mapSoleTraderDetailsRequest(null);

        // then
        assertNull(actual);
    }
}