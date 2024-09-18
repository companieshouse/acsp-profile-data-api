package uk.gov.companieshouse.acspprofile.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;

public class AcspData {

    @Field("acsp_number")
    @JsonProperty("acsp_number")
    private String acspNumber;
    private String name;
    @Field("notified_from")
    @JsonProperty("notified_from")
    private Instant notifiedFrom;
    private String status;
    private String type;
    @Field("business_sector")
    @JsonProperty("business_sector")
    private String businessSector;
    private String etag;
    @Field("registered_office_address")
    @JsonProperty("registered_office_address")
    private AcspAddress registeredOfficeAddress;
    @Field("service_address")
    @JsonProperty("service_address")
    private AcspAddress serviceAddress;
    @Field("sole_trader_details")
    @JsonProperty("sole_trader_details")
    private AcspSoleTraderDetails acspSoleTraderDetails;
    @Field("aml_details")
    @JsonProperty("aml_details")
    private List<AcspAmlDetails> acspAmlDetails;
    @Field("deauthorised_from")
    @JsonProperty("deauthorised_from")
    private Instant deauthorisedFrom;
    private AcspLinks links;

    public String getAcspNumber() {
        return acspNumber;
    }

    public AcspData acspNumber(String acspNumber) {
        this.acspNumber = acspNumber;
        return this;
    }

    public String getName() {
        return name;
    }

    public AcspData name(String name) {
        this.name = name;
        return this;
    }

    public Instant getNotifiedFrom() {
        return notifiedFrom;
    }

    public AcspData notifiedFrom(Instant notifiedFrom) {
        this.notifiedFrom = notifiedFrom;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public AcspData status(String status) {
        this.status = status;
        return this;
    }

    public String getType() {
        return type;
    }

    public AcspData type(String type) {
        this.type = type;
        return this;
    }

    public String getBusinessSector() {
        return businessSector;
    }

    public AcspData businessSector(String businessSector) {
        this.businessSector = businessSector;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public AcspData etag(String etag) {
        this.etag = etag;
        return this;
    }

    public AcspAddress getRegisteredOfficeAddress() {
        return registeredOfficeAddress;
    }

    public AcspData registeredOfficeAddress(
            AcspAddress registeredOfficeAddress) {
        this.registeredOfficeAddress = registeredOfficeAddress;
        return this;
    }

    public AcspAddress getServiceAddress() {
        return serviceAddress;
    }

    public AcspData serviceAddress(AcspAddress serviceAddress) {
        this.serviceAddress = serviceAddress;
        return this;
    }

    public AcspSoleTraderDetails getSoleTraderDetails() {
        return acspSoleTraderDetails;
    }

    public AcspData soleTraderDetails(AcspSoleTraderDetails acspSoleTraderDetails) {
        this.acspSoleTraderDetails = acspSoleTraderDetails;
        return this;
    }

    public List<AcspAmlDetails> getAmlDetails() {
        return acspAmlDetails;
    }

    public AcspData amlDetails(List<AcspAmlDetails> acspAmlDetails) {
        this.acspAmlDetails = acspAmlDetails;
        return this;
    }

    public Instant getDeauthorisedFrom() {
        return deauthorisedFrom;
    }

    public AcspData deauthorisedFrom(Instant deauthorisedFrom) {
        this.deauthorisedFrom = deauthorisedFrom;
        return this;
    }

    public AcspLinks getLinks() {
        return links;
    }

    public AcspData links(AcspLinks links) {
        this.links = links;
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
        AcspData data = (AcspData) o;
        return Objects.equals(acspNumber, data.acspNumber) && Objects.equals(name, data.name)
                && Objects.equals(notifiedFrom, data.notifiedFrom) && Objects.equals(status,
                data.status) && Objects.equals(type, data.type) && Objects.equals(businessSector,
                data.businessSector) && Objects.equals(etag, data.etag) && Objects.equals(
                registeredOfficeAddress, data.registeredOfficeAddress) && Objects.equals(serviceAddress,
                data.serviceAddress) && Objects.equals(acspSoleTraderDetails, data.acspSoleTraderDetails)
                && Objects.equals(acspAmlDetails, data.acspAmlDetails) && Objects.equals(deauthorisedFrom,
                data.deauthorisedFrom) && Objects.equals(links, data.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acspNumber, name, notifiedFrom, status, type, businessSector, etag, registeredOfficeAddress,
                serviceAddress, acspSoleTraderDetails, acspAmlDetails, deauthorisedFrom, links);
    }

    @Override
    public String toString() {
        return "AcspData{" +
                "acspNumber='" + acspNumber + '\'' +
                ", name='" + name + '\'' +
                ", notifiedFrom=" + notifiedFrom +
                ", status=" + status +
                ", type=" + type +
                ", businessSector=" + businessSector +
                ", etag='" + etag + '\'' +
                ", registeredOfficeAddress=" + registeredOfficeAddress +
                ", serviceAddress=" + serviceAddress +
                ", soleTraderDetails=" + acspSoleTraderDetails +
                ", amlDetails=" + acspAmlDetails +
                ", deauthorisedFrom=" + deauthorisedFrom +
                ", links=" + links +
                '}';
    }
}
