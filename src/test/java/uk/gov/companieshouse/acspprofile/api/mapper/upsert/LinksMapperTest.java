package uk.gov.companieshouse.acspprofile.api.mapper.upsert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.filinghistory.Links;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileLinks;

class LinksMapperTest {

    private static final String SELF_LINK = "/company/12345678/acsp-profile/abc123def456ghi789";
    private final LinksMapper mapper = new LinksMapper();

    @Test
    void shouldMapRequestLinksToPersistenceModelLinks() {
        // given
        Links requestLinks = new Links()
                .self(SELF_LINK);

        ACSPProfileLinks expected = new ACSPProfileLinks()
                .self(SELF_LINK);
        // when
        ACSPProfileLinks actual = mapper.map(requestLinks);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void shouldMapNullLinksToNull() {
        // given

        // when
        ACSPProfileLinks actual = mapper.map(null);

        // then
        assertNull(actual);
    }
}