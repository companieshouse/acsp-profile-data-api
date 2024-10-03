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
    void mapAcspAmlDetails() {
        // given
        List<AcspAmlDetails> acspAmlDetails = List.of(new AcspAmlDetails()
                .membershipDetails(MEMBERSHIP_DETAILS)
                .supervisoryBody(SUPERVISORY_BODY));

        List<AmlDetailsItem> expected = List.of(new AmlDetailsItem()
                .membershipDetails(MEMBERSHIP_DETAILS)
                .supervisoryBody(SupervisoryBody.FACULTY_OF_ADVOCATES));

        // when
        List<AmlDetailsItem> actual = amlDetailsMapper.mapAmlDetails(acspAmlDetails);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void mapBareAcspAmlDetails() {
        // given
        List<AcspAmlDetails> acspAmlDetails = List.of(new AcspAmlDetails());

        List<AmlDetailsItem> expected = List.of(new AmlDetailsItem());

        // when
        List<AmlDetailsItem> actual = amlDetailsMapper.mapAmlDetails(acspAmlDetails);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void mapEmptyAcspAmlDetails() {
        // given
        List<AcspAmlDetails> acspAmlDetails = List.of();

        // when
        List<AmlDetailsItem> actual = amlDetailsMapper.mapAmlDetails(acspAmlDetails);

        // then
        assertNull(actual);
    }

    @Test
    void mapNullAcspAmlDetails() {
        // given

        // when
        List<AmlDetailsItem> actual = amlDetailsMapper.mapAmlDetails(null);

        // then
        assertNull(actual);
    }
}