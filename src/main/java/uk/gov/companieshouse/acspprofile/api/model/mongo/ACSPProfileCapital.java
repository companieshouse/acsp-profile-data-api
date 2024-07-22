package uk.gov.companieshouse.acspprofile.api.model.mongo;

import java.time.Instant;
import java.util.Objects;

public class ACSPProfileCapital {

    private String currency;
    private String figure;
    private Instant date;

    public String getCurrency() {
        return currency;
    }

    public ACSPProfileCapital currency(String currency) {
        this.currency = currency;
        return this;
    }

    public String getFigure() {
        return figure;
    }

    public ACSPProfileCapital figure(String figure) {
        this.figure = figure;
        return this;
    }

    public Instant getDate() {
        return date;
    }

    public ACSPProfileCapital date(Instant date) {
        this.date = date;
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
        ACSPProfileCapital that = (ACSPProfileCapital) o;
        return Objects.equals(currency, that.currency) && Objects.equals(figure,
                that.figure) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, figure, date);
    }

    @Override
    public String toString() {
        return "FilingHistoryCapital{" +
                "currency='" + currency + '\'' +
                ", figure='" + figure + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
