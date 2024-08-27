package uk.gov.companieshouse.acspprofile.api.mapper;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.acspprofile.api.mapper.get.AcspGetMapper;
import uk.gov.companieshouse.acspprofile.api.model.AcspProfileDocument;

class AcspGetMapperTest {

    private final AcspGetMapper mapper = new AcspGetMapper();

    @Test
    void shouldNotMapProfileWhenNotImplemented() {
        // given

        // when
        Object actual = mapper.mapProfile(new AcspProfileDocument());

        // then
        assertNull(actual);
    }

    @Test
    void shouldNotMapFullProfileWhenNotImplemented() {
        // given

        // when
        Object actual = mapper.mapFullProfile(new AcspProfileDocument());

        // then
        assertNull(actual);
    }
}