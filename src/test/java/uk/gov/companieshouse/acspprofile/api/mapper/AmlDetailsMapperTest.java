package uk.gov.companieshouse.acspprofile.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.acspprofile.api.model.AcspAmlDetails;
import uk.gov.companieshouse.api.acspprofile.AmlDetailsItem;
import uk.gov.companieshouse.api.acspprofile.SupervisoryBody;

class AmlDetailsMapperTest {

    private static final String MEMBERSHIP_DETAILS = "membership details";
    private static final String SUPERVISORY_BODY = "faculty-of-advocates";

    private final AmlDetailsMapper amlDetailsMapper = new AmlDetailsMapper();

    @Test
    void mapAcspAmlDetailsToResponse() {
        // given
        List<AcspAmlDetails> acspAmlDetails = List.of(new AcspAmlDetails()
                .membershipDetails(MEMBERSHIP_DETAILS)
                .supervisoryBody(SUPERVISORY_BODY));

        List<AmlDetailsItem> expected = List.of(new AmlDetailsItem()
                .membershipDetails(MEMBERSHIP_DETAILS)
                .supervisoryBody(SupervisoryBody.FACULTY_OF_ADVOCATES));

        // when
        List<AmlDetailsItem> actual = amlDetailsMapper.mapAmlDetailsResponse(acspAmlDetails);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void mapBareAcspAmlDetailsToResponse() {
        // given
        List<AcspAmlDetails> acspAmlDetails = List.of(new AcspAmlDetails());

        List<AmlDetailsItem> expected = List.of(new AmlDetailsItem());

        // when
        List<AmlDetailsItem> actual = amlDetailsMapper.mapAmlDetailsResponse(acspAmlDetails);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void mapEmptyAcspAmlDetailsToResponse() {
        // given
        List<AcspAmlDetails> acspAmlDetails = List.of();

        // when
        List<AmlDetailsItem> actual = amlDetailsMapper.mapAmlDetailsResponse(acspAmlDetails);

        // then
        assertNull(actual);
    }

    @Test
    void mapNullAcspAmlDetailsToResponse() {
        // given

        // when
        List<AmlDetailsItem> actual = amlDetailsMapper.mapAmlDetailsResponse(null);

        // then
        assertNull(actual);
    }

    @Test
    void mapAmlDetailsRequestToAcsp() {
        // given
        List<AmlDetailsItem> amlDetailsItems = List.of(new AmlDetailsItem()
                .membershipDetails(MEMBERSHIP_DETAILS)
                .supervisoryBody(SupervisoryBody.FACULTY_OF_ADVOCATES));

        List<AcspAmlDetails> expected = List.of(new AcspAmlDetails()
                .membershipDetails(MEMBERSHIP_DETAILS)
                .supervisoryBody(SUPERVISORY_BODY));

        // when
        List<AcspAmlDetails> actual = amlDetailsMapper.mapAmlDetailsRequest(amlDetailsItems);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void mapBareAmlDetailsRequestToAcspAmlDetails() {
        // given
        List<AmlDetailsItem> amlDetailsItems = List.of(new AmlDetailsItem());

        List<AcspAmlDetails> expected = List.of(new AcspAmlDetails());

        // when
        List<AcspAmlDetails> actual = amlDetailsMapper.mapAmlDetailsRequest(amlDetailsItems);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void mapEmptyAmlDetailsRequestToAcspAmlDetails() {
        // given
        List<AmlDetailsItem> amlDetailsItems = List.of();

        // when
        List<AcspAmlDetails> actual = amlDetailsMapper.mapAmlDetailsRequest(amlDetailsItems);

        // then
        assertNull(actual);
    }

    @Test
    void mapNullAmlDetailsRequestToAcspAmlDetails() {
        // given

        // when
        List<AcspAmlDetails> actual = amlDetailsMapper.mapAmlDetailsRequest(null);

        // then
        assertNull(actual);
    }
}