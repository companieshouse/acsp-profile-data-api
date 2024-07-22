package uk.gov.companieshouse.acspprofile.api.mapper.upsert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.filinghistory.AltCapitalDescriptionValue;
import uk.gov.companieshouse.api.filinghistory.CapitalDescriptionValue;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileAltCapital;
import uk.gov.companieshouse.acspprofile.api.model.mongo.ACSPProfileCapital;

class CapitalDescriptionMapperTest {

    private static final String DATE_STRING = "2022-01-01T00:00:00.00Z";
    private static final Instant DATE_INSTANT = Instant.parse(DATE_STRING);
    private static final String CURRENCY = "GBP";
    private static final String FIGURE = "100.00";
    private static final String ALT_DESCRIPTION = "alt description";
    private final CapitalDescriptionMapper mapper = new CapitalDescriptionMapper();

    @Test
    void mapCapitalDescriptionValueList() {

        CapitalDescriptionValue capitalDescriptionValue = new CapitalDescriptionValue()
                .currency(CURRENCY)
                .figure(FIGURE)
                .date(DATE_STRING);
        ACSPProfileCapital expected = new ACSPProfileCapital()
                .currency(CURRENCY)
                .figure(FIGURE)
                .date(DATE_INSTANT);

        List<ACSPProfileCapital> actual = mapper.mapCapitalDescriptionValueList(List.of(capitalDescriptionValue));
        assertEquals(1, actual.size());
        assertEquals(expected, actual.getFirst());
    }

    @Test
    void mapAltCapitalDescriptionValueList() {

        AltCapitalDescriptionValue altCapitalDescriptionValue = new AltCapitalDescriptionValue()
                .currency(CURRENCY)
                .figure(FIGURE)
                .date(DATE_STRING)
                .description(ALT_DESCRIPTION);

        ACSPProfileAltCapital expected = new ACSPProfileAltCapital()
                .currency(CURRENCY)
                .figure(FIGURE)
                .date(DATE_INSTANT)
                .description(ALT_DESCRIPTION);

        List<ACSPProfileAltCapital> actual = mapper.mapAltCapitalDescriptionValueList(
                List.of(altCapitalDescriptionValue));
        assertEquals(1, actual.size());
        assertEquals(expected, actual.getFirst());
    }


    @Test
    void shouldMapNullCapitalDescriptionValueListToNull() {
        // given

        // when
        List<ACSPProfileCapital> actual = mapper.mapCapitalDescriptionValueList(null);

        // then
        assertNull(actual);
    }

    @Test
    void shouldMapNullAltCapitalDescriptionValueListToNull() {
        // given

        // when
        List<ACSPProfileAltCapital> actual = mapper.mapAltCapitalDescriptionValueList(null);

        // then
        assertNull(actual);
    }
}