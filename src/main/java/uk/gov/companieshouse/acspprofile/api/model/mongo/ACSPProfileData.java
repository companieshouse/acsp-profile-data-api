package uk.gov.companieshouse.acspprofile.api.model.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import org.springframework.data.mongodb.core.mapping.Field;

public class ACSPProfileData {

    private String type;
    private Instant date;
    private String category;
    private Object subcategory;
    private String description;
    @Field("description_values")
    @JsonProperty("description_values")
    private ACSPProfileDescriptionValues descriptionValues;
    private List<ACSPProfileAnnotation> annotations;
    private List<ACSPProfileResolution> resolutions;
    @Field("associated_filings")
    @JsonProperty("associated_filings")
    private List<ACSPProfileAssociatedFiling> associatedFilings;
    @Field("action_date")
    @JsonProperty("action_date")
    private Instant actionDate;
    private Integer pages;
    @Field("paper_filed")
    @JsonProperty("paper_filed")
    private Boolean paperFiled;
    private ACSPProfileLinks links;

    public String getType() {
        return type;
    }

    public ACSPProfileData type(String type) {
        this.type = type;
        return this;
    }

    public Instant getDate() {
        return date;
    }

    public ACSPProfileData date(Instant date) {
        this.date = date;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public ACSPProfileData category(String category) {
        this.category = category;
        return this;
    }

    public Object getSubcategory() {
        return subcategory;
    }

    public ACSPProfileData subcategory(String subcategory) {
        this.subcategory = subcategory;
        return this;
    }

    public ACSPProfileData subcategory(List<String> subcategories) {
        this.subcategory = subcategories;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ACSPProfileData description(String description) {
        this.description = description;
        return this;
    }

    public ACSPProfileDescriptionValues getDescriptionValues() {
        return descriptionValues;
    }

    public ACSPProfileData descriptionValues(ACSPProfileDescriptionValues descriptionValues) {
        this.descriptionValues = descriptionValues;
        return this;
    }

    public List<ACSPProfileAnnotation> getAnnotations() {
        return annotations;
    }

    public ACSPProfileData annotations(List<ACSPProfileAnnotation> annotations) {
        this.annotations = annotations;
        return this;
    }

    public List<ACSPProfileResolution> getResolutions() {
        return resolutions;
    }

    public ACSPProfileData resolutions(List<ACSPProfileResolution> resolutions) {
        this.resolutions = resolutions;
        return this;
    }

    public List<ACSPProfileAssociatedFiling> getAssociatedFilings() {
        return associatedFilings;
    }

    public ACSPProfileData associatedFilings(List<ACSPProfileAssociatedFiling> associatedFilings) {
        this.associatedFilings = associatedFilings;
        return this;
    }

    public Instant getActionDate() {
        return actionDate;
    }

    public ACSPProfileData actionDate(Instant actionDate) {
        this.actionDate = actionDate;
        return this;
    }

    public Integer getPages() {
        return pages;
    }

    public ACSPProfileData pages(Integer pages) {
        this.pages = pages;
        return this;
    }

    public Boolean getPaperFiled() {
        return paperFiled;
    }

    public ACSPProfileData paperFiled(Boolean paperFiled) {
        this.paperFiled = paperFiled;
        return this;
    }

    public ACSPProfileLinks getLinks() {
        return links;
    }

    public ACSPProfileData links(ACSPProfileLinks links) {
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
        ACSPProfileData that = (ACSPProfileData) o;
        return Objects.equals(type, that.type) && Objects.equals(date, that.date)
                && Objects.equals(category, that.category) && Objects.equals(subcategory,
                that.subcategory) && Objects.equals(description, that.description) && Objects.equals(
                descriptionValues, that.descriptionValues) && Objects.equals(annotations, that.annotations)
                && Objects.equals(resolutions, that.resolutions) && Objects.equals(associatedFilings,
                that.associatedFilings) && Objects.equals(actionDate, that.actionDate)
                && Objects.equals(pages, that.pages) && Objects.equals(paperFiled, that.paperFiled)
                && Objects.equals(links, that.links);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, date, category, subcategory, description, descriptionValues, annotations, resolutions,
                associatedFilings, actionDate, pages, paperFiled, links);
    }

    @Override
    public String toString() {
        return "FilingHistoryData{" +
                "type='" + type + '\'' +
                ", date=" + date +
                ", category='" + category + '\'' +
                ", subcategory=" + subcategory +
                ", description='" + description + '\'' +
                ", descriptionValues=" + descriptionValues +
                ", annotations=" + annotations +
                ", resolutions=" + resolutions +
                ", associatedFilings=" + associatedFilings +
                ", actionDate=" + actionDate +
                ", pages=" + pages +
                ", paperFiled=" + paperFiled +
                ", links=" + links +
                '}';
    }
}