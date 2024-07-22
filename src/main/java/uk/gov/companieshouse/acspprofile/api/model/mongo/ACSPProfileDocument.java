package uk.gov.companieshouse.acspprofile.api.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "company_filing_history")
public class ACSPProfileDocument {

    @Id
    @JsonProperty("_id")
    private String transactionId;
    @Field("_entity_id")
    @JsonProperty("_entity_id")
    private String entityId;
    @Field("company_number")
    @JsonProperty("company_number")
    private String companyNumber;
    @Field("_document_id")
    @JsonProperty("_document_id")
    private String documentId;
    @Field("_barcode")
    @JsonProperty("_barcode")
    private String barcode;
    private ACSPProfileData data;
    @Field("original_description")
    @JsonProperty("original_description")
    private String originalDescription;
    @Field("original_values")
    @JsonProperty("original_values")
    private ACSPProfileOriginalValues originalValues;
    @Field("delta_at")
    @JsonProperty("delta_at")
    private String deltaAt;
    private ACSPProfileDeltaTimestamp updated;
    private ACSPProfileDeltaTimestamp created;
    @Field("matched_default")
    @JsonProperty("matched_default")
    private Integer matchedDefault;

    public String getTransactionId() {
        return transactionId;
    }

    public ACSPProfileDocument transactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getEntityId() {
        return entityId;
    }

    public ACSPProfileDocument entityId(String entityId) {
        this.entityId = entityId;
        return this;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public ACSPProfileDocument companyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
        return this;
    }

    public String getDocumentId() {
        return documentId;
    }

    public ACSPProfileDocument documentId(String documentId) {
        this.documentId = documentId;
        return this;
    }

    public String getBarcode() {
        return barcode;
    }

    public ACSPProfileDocument barcode(String barcode) {
        this.barcode = barcode;
        return this;
    }

    public ACSPProfileData getData() {
        return data;
    }

    public ACSPProfileDocument data(ACSPProfileData data) {
        this.data = data;
        return this;
    }

    public String getOriginalDescription() {
        return originalDescription;
    }

    public ACSPProfileDocument originalDescription(String originalDescription) {
        this.originalDescription = originalDescription;
        return this;
    }

    public ACSPProfileOriginalValues getOriginalValues() {
        return originalValues;
    }

    public ACSPProfileDocument originalValues(ACSPProfileOriginalValues ACSPProfileOriginalValues) {
        this.originalValues = ACSPProfileOriginalValues;
        return this;
    }

    public String getDeltaAt() {
        return deltaAt;
    }

    public ACSPProfileDocument deltaAt(String deltaAt) {
        this.deltaAt = deltaAt;
        return this;
    }

    public ACSPProfileDeltaTimestamp getUpdated() {
        return updated;
    }

    public ACSPProfileDocument updated(ACSPProfileDeltaTimestamp updated) {
        this.updated = updated;
        return this;
    }

    public ACSPProfileDeltaTimestamp getCreated() {
        return created;
    }

    public ACSPProfileDocument created(ACSPProfileDeltaTimestamp created) {
        this.created = created;
        return this;
    }

    public Integer getMatchedDefault() {
        return matchedDefault;
    }

    public ACSPProfileDocument matchedDefault(Integer matchedDefault) {
        this.matchedDefault = matchedDefault;
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
        ACSPProfileDocument that = (ACSPProfileDocument) o;
        return Objects.equals(transactionId, that.transactionId) && Objects.equals(entityId,
                that.entityId) && Objects.equals(companyNumber, that.companyNumber) && Objects.equals(
                documentId, that.documentId) && Objects.equals(barcode, that.barcode) && Objects.equals(
                data, that.data) && Objects.equals(originalDescription, that.originalDescription)
                && Objects.equals(originalValues, that.originalValues) && Objects.equals(deltaAt,
                that.deltaAt) && Objects.equals(updated, that.updated) && Objects.equals(created,
                that.created) && Objects.equals(matchedDefault, that.matchedDefault);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, entityId, companyNumber, documentId, barcode, data, originalDescription,
                originalValues, deltaAt, updated, created, matchedDefault);
    }

    @Override
    public String toString() {
        return "FilingHistoryDocument{" +
                "transactionId='" + transactionId + '\'' +
                ", entityId='" + entityId + '\'' +
                ", companyNumber='" + companyNumber + '\'' +
                ", documentId='" + documentId + '\'' +
                ", barcode='" + barcode + '\'' +
                ", data=" + data +
                ", originalDescription='" + originalDescription + '\'' +
                ", originalValues=" + originalValues +
                ", deltaAt='" + deltaAt + '\'' +
                ", updated=" + updated +
                ", created=" + created +
                ", matchedDefault=" + matchedDefault +
                '}';
    }
}
