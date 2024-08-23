package uk.gov.companieshouse.acspprofile.api.model;

import java.time.Instant;
import org.springframework.data.mongodb.core.mapping.Field;
import uk.gov.companieshouse.acspprofile.api.model.enums.AcspType;
import uk.gov.companieshouse.acspprofile.api.model.enums.BusinessSector;
import uk.gov.companieshouse.acspprofile.api.model.enums.TradingStatus;

public class AcspData {

    @Field("acsp_number")
    private String number;
    @Field("acsp_name")
    private String name;
    @Field("acsp_created_date")
    private Instant createdDate;
    @Field("acsp_trading_status")
    private TradingStatus tradingStatus;
    @Field("acsp_email")
    private String email;
    @Field("acsp_type")
    private AcspType type;
    @Field("acsp_business_sector")
    private BusinessSector businessSector;
    @Field("acsp_registered_office_address")
    private AcspAddress registeredOfficeAddress;
    @Field("acsp_service_address")
    private AcspAddress serviceAddress;
    @Field("acsp_sole_trader_details")
    private SoleTraderDetails soleTraderDetails;
    @Field("acsp_aml_details")
    private AmlDetails amlDetails;
    @Field("acsp_end_date")
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
}
