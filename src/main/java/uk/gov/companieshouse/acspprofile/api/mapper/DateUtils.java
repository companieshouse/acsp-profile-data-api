package uk.gov.companieshouse.acspprofile.api.mapper;

import static java.time.ZoneOffset.UTC;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;

public final class DateUtils {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS")
            .withZone(UTC);

    private DateUtils() {
    }

    public static Instant localDateToInstant(final LocalDate localDate) {
        return Optional.ofNullable(localDate)
                .map(date -> Instant.from(date.atStartOfDay(UTC)))
                .orElse(null);
    }

    public static LocalDate instantToLocalDate(final Instant inputDate) {
        return Optional.ofNullable(inputDate)
                .map(date -> LocalDate.ofInstant(date, ZoneOffset.UTC))
                .orElse(null);
    }

    public static boolean isDeltaStale(final String requestDeltaAt, final String existingDeltaAt) {
        return StringUtils.isNotBlank(requestDeltaAt) &&
                StringUtils.isNotBlank(existingDeltaAt) &&
                OffsetDateTime.parse(requestDeltaAt, FORMATTER)
                        .isBefore(OffsetDateTime.parse(existingDeltaAt, FORMATTER));
    }
}