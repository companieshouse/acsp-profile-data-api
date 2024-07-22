package uk.gov.companieshouse.acspprofile.api.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;

public class ACSPProfileAssociatedFiling extends ACSPProfileChild {

    private String category;
    private String subcategory;
    private String description;
    private String type;
    private Instant date;
    @Field("description_values")
    @JsonProperty("description_values")
    private ACSPProfileDescriptionValues descriptionValues;
    @Field("original_description")
    @JsonProperty("original_description")
    private String originalDescription;
    @Field("action_date")
    @JsonProperty("action_date")
    private Instant actionDate;

    @Override
    public ACSPProfileAssociatedFiling entityId(String entityId) {
        this.entityId = entityId;
        return this;
    }

    @Override
    public ACSPProfileAssociatedFiling deltaAt(String deltaAt) {
        this.deltaAt = deltaAt;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ACSPProfileAssociatedFiling category(String category) {
        this.category = category;
        return this;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public ACSPProfileAssociatedFiling subcategory(String subcategory) {
        this.subcategory = subcategory;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ACSPProfileAssociatedFiling description(String description) {
        this.description = description;
        return this;
    }

    public String getType() {
        return type;
    }

    public ACSPProfileAssociatedFiling type(String type) {
        this.type = type;
        return this;
    }

    public Instant getDate() {
        return date;
    }

    public ACSPProfileAssociatedFiling date(Instant date) {
        this.date = date;
        return this;
    }

    public ACSPProfileDescriptionValues getDescriptionValues() {
        return descriptionValues;
    }

    public ACSPProfileAssociatedFiling descriptionValues(ACSPProfileDescriptionValues descriptionValues) {
        this.descriptionValues = descriptionValues;
        return this;
    }

    public String getOriginalDescription() {
        return originalDescription;
    }

    public ACSPProfileAssociatedFiling originalDescription(String originalDescription) {
        this.originalDescription = originalDescription;
        return this;
    }

    public Instant getActionDate() {
        return actionDate;
    }

    public ACSPProfileAssociatedFiling actionDate(Instant actionDate) {
        this.actionDate = actionDate;
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
        if (!super.equals(o)) {
            return false;
        }
        ACSPProfileAssociatedFiling that = (ACSPProfileAssociatedFiling) o;
        return Objects.equals(category, that.category) && Objects.equals(subcategory, that.subcategory)
                && Objects.equals(description, that.description) && Objects.equals(type, that.type)
                && Objects.equals(date, that.date) && Objects.equals(descriptionValues,
                that.descriptionValues) && Objects.equals(originalDescription, that.originalDescription)
                && Objects.equals(actionDate, that.actionDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), category, subcategory, description, type, date, descriptionValues,
                originalDescription, actionDate);
    }

    @Override
    public String toString() {
        return "FilingHistoryAssociatedFiling{" +
                "category='" + category + '\'' +
                ", subcategory='" + subcategory + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", date=" + date +
                ", descriptionValues=" + descriptionValues +
                ", originalDescription='" + originalDescription + '\'' +
                ", actionDate=" + actionDate +
                "} " + super.toString();
    }
}
