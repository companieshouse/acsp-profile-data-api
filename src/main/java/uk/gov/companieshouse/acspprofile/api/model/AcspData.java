package uk.gov.companieshouse.acspprofile.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.acspprofile.api.model.enums.AcspType;
import uk.gov.companieshouse.acspprofile.api.model.enums.BusinessSector;
import uk.gov.companieshouse.acspprofile.api.model.enums.TradingStatus;

public class AcspData {

    @Field("acsp_number")
    @JsonProperty("acsp_number")
    private String acspNumber;
    @Field("name")
    @JsonProperty("name")
    private String name;
    @Field("created_date")
    @JsonProperty("created_date")
    private Instant createdDate;
    @Field("trading_status")
    @JsonProperty("trading_status")
    private TradingStatus tradingStatus;
    @Field("email")
    @JsonProperty("email")
    private String email;
    @Field("type")
    @JsonProperty("type")
    private AcspType type;
    @Field("business_sector")
    @JsonProperty("business_sector")
    private BusinessSector businessSector;
    @Field("registered_office_address")
    @JsonProperty("registered_office_address")
    private AcspAddress registeredOfficeAddress;
    @Field("service_address")
    @JsonProperty("service_address")
    private AcspAddress serviceAddress;
    @Field("sole_trader_details")
    @JsonProperty("sole_trader_details")
    private SoleTraderDetails soleTraderDetails;
    @Field("aml_details")
    @JsonProperty("aml_details")
    private List<AmlDetails> amlDetails;
    @Field("end_date")
    @JsonProperty("end_date")
    private Instant endDate;
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

    public Instant getCreatedDate() {
        return createdDate;
    }

    public AcspData createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public TradingStatus getTradingStatus() {
        return tradingStatus;
    }

    public AcspData tradingStatus(TradingStatus tradingStatus) {
        this.tradingStatus = tradingStatus;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AcspData email(String email) {
        this.email = email;
        return this;
    }

    public AcspType getType() {
        return type;
    }

    public AcspData type(AcspType type) {
        this.type = type;
        return this;
    }

    public BusinessSector getBusinessSector() {
        return businessSector;
    }

    public AcspData businessSector(BusinessSector businessSector) {
        this.businessSector = businessSector;
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

    public SoleTraderDetails getSoleTraderDetails() {
        return soleTraderDetails;
    }

    public AcspData soleTraderDetails(SoleTraderDetails soleTraderDetails) {
        this.soleTraderDetails = soleTraderDetails;
        return this;
    }

    public List<AmlDetails> getAmlDetails() {
        return amlDetails;
    }

    public AcspData amlDetails(List<AmlDetails> amlDetails) {
        this.amlDetails = amlDetails;
        return this;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public AcspData endDate(Instant endDate) {
        this.endDate = endDate;
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
        AcspData acspData = (AcspData) o;
        return Objects.equals(acspNumber, acspData.acspNumber) && Objects.equals(name, acspData.name)
                && Objects.equals(createdDate, acspData.createdDate) && tradingStatus == acspData.tradingStatus
                && Objects.equals(email, acspData.email) && type == acspData.type
                && businessSector == acspData.businessSector && Objects.equals(registeredOfficeAddress,
                acspData.registeredOfficeAddress) && Objects.equals(serviceAddress, acspData.serviceAddress)
                && Objects.equals(soleTraderDetails, acspData.soleTraderDetails) && Objects.equals(
                amlDetails, acspData.amlDetails) && Objects.equals(endDate, acspData.endDate)
                && Objects.equals(links, acspData.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acspNumber, name, createdDate, tradingStatus, email, type, businessSector,
                registeredOfficeAddress, serviceAddress, soleTraderDetails, amlDetails, endDate, links);
    }

    @Override
    public String toString() {
        return "AcspData{" +
                "acspNumber='" + acspNumber + '\'' +
                ", name='" + name + '\'' +
                ", createdDate=" + createdDate +
                ", tradingStatus=" + tradingStatus +
                ", email='" + email + '\'' +
                ", type=" + type +
                ", businessSector=" + businessSector +
                ", registeredOfficeAddress=" + registeredOfficeAddress +
                ", serviceAddress=" + serviceAddress +
                ", soleTraderDetails=" + soleTraderDetails +
                ", amlDetails=" + amlDetails +
                ", endDate=" + endDate +
                ", links=" + links +
                '}';
    }
}
