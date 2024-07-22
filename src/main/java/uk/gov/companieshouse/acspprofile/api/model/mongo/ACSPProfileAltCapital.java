package uk.gov.companieshouse.acspprofile.api.model.mongo;

import java.time.Instant;
import java.util.Objects;

public class ACSPProfileAltCapital {

    private String currency;
    private String figure;
    private Instant date;
    private String description;

    public String getCurrency() {
        return currency;
    }

    public ACSPProfileAltCapital currency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getFigure() {
        return figure;
    }

    public ACSPProfileAltCapital figure(String figure) {
        this.figure = figure;
        return this;
    }

    public Instant getDate() {
        return date;
    }

    public ACSPProfileAltCapital date(Instant date) {
        this.date = date;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ACSPProfileAltCapital description(String description) {
        this.description = description;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ACSPProfileAltCapital that = (ACSPProfileAltCapital) o;
        return Objects.equals(currency, that.currency) && Objects.equals(figure,
                that.figure) && Objects.equals(date, that.date) && Objects.equals(
                description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, figure, date, description);
    }

    @Override
    public String toString() {
        return "FilingHistoryAltCapital{" +
                "currency='" + currency + '\'' +
                ", figure='" + figure + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
