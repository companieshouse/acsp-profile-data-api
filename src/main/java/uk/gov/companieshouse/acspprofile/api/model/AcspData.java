package uk.gov.companieshouse.acspprofile.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.acspprofile.api.model.enums.AcspType;
import uk.gov.companieshouse.acspprofile.api.model.enums.BusinessSector;
import uk.gov.companieshouse.acspprofile.api.model.enums.TradingStatus;

public class AcspData {

    @Field("acsp_number")
    @JsonProperty("acsp_number")
    private String number;
    @Field("acsp_name")
    @JsonProperty("acsp_name")
    private String name;
    @Field("acsp_created_date")
    @JsonProperty("acsp_created_date")
    private Instant createdDate;
    @Field("acsp_trading_status")
    @JsonProperty("acsp_trading_status")
    private TradingStatus tradingStatus;
    @Field("acsp_email")
    @JsonProperty("acsp_email")
    private String email;
    @Field("acsp_type")
    @JsonProperty("acsp_type")
    private AcspType type;
    @Field("acsp_business_sector")
    @JsonProperty("acsp_business_sector")
    private BusinessSector businessSector;
    @Field("acsp_registered_office_address")
    @JsonProperty("acsp_registered_office_address")
    private AcspAddress registeredOfficeAddress;
    @Field("acsp_service_address")
    @JsonProperty("acsp_service_address")
    private AcspAddress serviceAddress;
    @Field("acsp_sole_trader_details")
    @JsonProperty("acsp_sole_trader_details")
    private SoleTraderDetails soleTraderDetails;
    @Field("acsp_aml_details")
    @JsonProperty("acsp_aml_details")
    private AmlDetails amlDetails;
    @Field("acsp_end_date")
    @JsonProperty("acsp_end_date")
    private Instant endDate;

    public String getNumber() {
        return number;
    }

    public AcspData number(String number) {
        this.number = number;
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

    public AmlDetails getAmlDetails() {
        return amlDetails;
    }

    public AcspData amlDetails(AmlDetails amlDetails) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AcspData acspData = (AcspData) o;
        return Objects.equals(number, acspData.number) && Objects.equals(name, acspData.name)
                && Objects.equals(createdDate, acspData.createdDate) && tradingStatus == acspData.tradingStatus
                && Objects.equals(email, acspData.email) && type == acspData.type
                && businessSector == acspData.businessSector && Objects.equals(registeredOfficeAddress,
                acspData.registeredOfficeAddress) && Objects.equals(serviceAddress, acspData.serviceAddress)
                && Objects.equals(soleTraderDetails, acspData.soleTraderDetails) && Objects.equals(
                amlDetails, acspData.amlDetails) && Objects.equals(endDate, acspData.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, name, createdDate, tradingStatus, email, type, businessSector,
                registeredOfficeAddress,
                serviceAddress, soleTraderDetails, amlDetails, endDate);
    }

    @Override
    public String toString() {
        return "AcspData{" +
                "number='" + number + '\'' +
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
                '}';
    }
}
