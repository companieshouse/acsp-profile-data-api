package uk.gov.companieshouse.acspprofile.api.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum SupervisoryBody {

    @JsonProperty("financial-conduct-authority-fca")
	FINANCIAL_CONDUCT_AUTHORITY_FCA("financial-conduct-authority-fca"),
    @JsonProperty("gambling-commission")
	GAMBLING_COMMISSION("gambling-commission"),
    @JsonProperty("hm-revenue-customs-hmrc")
	HM_REVENUE_CUSTOMS_HMRC("hm-revenue-customs-hmrc"),
    @JsonProperty("association-of-accounting-technicians-aat")
	ASSOCIATION_OF_ACCOUNTING_TECHNICIANS_AAT("association-of-accounting-technicians-aat"),
    @JsonProperty("association-of-chartered-certified-accountants-acca")
	ASSOCIATION_OF_CHARTERED_CERTIFIED_ACCOUNTANTS_ACCA("association-of-chartered-certified-accountants-acca"),
    @JsonProperty("association-of-international-accountants-aia")
	ASSOCIATION_OF_INTERNATIONAL_ACCOUNTANTS_AIA("association-of-international-accountants-aia"),
    @JsonProperty("association-of-taxation-technicians-att")
	ASSOCIATION_OF_TAXATION_TECHNICIANS_ATT("association-of-taxation-technicians-att"),
    @JsonProperty("chartered-institute-of-legal-executives-cilex")
	CHARTERED_INSTITUTE_OF_LEGAL_EXECUTIVES_CILEX("chartered-institute-of-legal-executives-cilex"),
    @JsonProperty("chartered-institute-of-management-accountants-cima")
	CHARTERED_INSTITUTE_OF_MANAGEMENT_ACCOUNTANTS_CIMA("chartered-institute-of-management-accountants-cima"),
    @JsonProperty("chartered-institute-of-taxation-ciot")
	CHARTERED_INSTITUTE_OF_TAXATION_CIOT("chartered-institute-of-taxation-ciot"),
    @JsonProperty("council-for-licensed-conveyancers-clc")
	COUNCIL_FOR_LICENSED_CONVEYANCERS_CLC("council-for-licensed-conveyancers-clc"),
    @JsonProperty("faculty-office-of-the-archbishop-of-canterbury")
	FACULTY_OFFICE_OF_THE_ARCHBISHOP_OF_CANTERBURY("faculty-office-of-the-archbishop-of-canterbury"),
    @JsonProperty("faculty-of-advocates")
	FACULTY_OF_ADVOCATES("faculty-of-advocates"),
    @JsonProperty("general-council-of-the-bar-bar-standards-board")
	GENERAL_COUNCIL_OF_THE_BAR_BAR_STANDARDS_BOARD("general-council-of-the-bar-bar-standards-board"),
    @JsonProperty("general-council-of-the-bar-of-northern-ireland")
	GENERAL_COUNCIL_OF_THE_BAR_OF_NORTHERN_IRELAND("general-council-of-the-bar-of-northern-ireland"),
    @JsonProperty("insolvency-practitioners-association-ipa")
	INSOLVENCY_PRACTITIONERS_ASSOCIATION_IPA("insolvency-practitioners-association-ipa"),
    @JsonProperty("institute-of-certified-bookkeepers-icb")
	INSTITUTE_OF_CERTIFIED_BOOKKEEPERS_ICB("institute-of-certified-bookkeepers-icb"),
    @JsonProperty("institute-of-chartered-accountants-in-england-and-wales-icaew")
	INSTITUTE_OF_CHARTERED_ACCOUNTANTS_IN_ENGLAND_AND_WALES_ICAEW("institute-of-chartered-accountants-in-england-and-wales-icaew"),
    @JsonProperty("institute-of-chartered-accountants-in-ireland-icai")
	INSTITUTE_OF_CHARTERED_ACCOUNTANTS_IN_IRELAND_ICAI("institute-of-chartered-accountants-in-ireland-icai"),
    @JsonProperty("institute-of-chartered-accountants-of-scotland-icas")
	INSTITUTE_OF_CHARTERED_ACCOUNTANTS_OF_SCOTLAND_ICAS("institute-of-chartered-accountants-of-scotland-icas"),
    @JsonProperty("institute-of-financial-accountants-ifa")
	INSTITUTE_OF_FINANCIAL_ACCOUNTANTS_IFA("institute-of-financial-accountants-ifa"),
    @JsonProperty("international-association-of-bookkeepers-iab")
	INTERNATIONAL_ASSOCIATION_OF_BOOKKEEPERS_IAB("international-association-of-bookkeepers-iab"),
    @JsonProperty("law-society-solicitors-regulation-authority-sra")
	LAW_SOCIETY_SOLICITORS_REGULATION_AUTHORITY_SRA("law-society-solicitors-regulation-authority-sra"),
    @JsonProperty("law-society-of-northern-ireland")
	LAW_SOCIETY_OF_NORTHERN_IRELAND("law-society-of-northern-ireland"),
    @JsonProperty("law-society-of-scotland")
	LAW_SOCIETY_OF_SCOTLAND("law-society-of-scotland"),
    @JsonProperty("chartered-institute-of-public-finance-and-accountancy-cipfa")
	CHARTERED_INSTITUTE_OF_PUBLIC_FINANCE_AND_ACCOUNTANCY_CIPFA("chartered-institute-of-public-finance-and-accountancy-cipfa"),
    @JsonProperty("royal-institution-of-chartered-surveyors-rics")
	ROYAL_INSTITUTION_OF_CHARTERED_SURVEYORS_RICS("royal-institution-of-chartered-surveyors-rics"),
    @JsonProperty("uk-association-of-online-gambling-operators-ukao")
	UK_ASSOCIATION_OF_ONLINE_GAMBLING_OPERATORS_UKAO("uk-association-of-online-gambling-operators-ukao");

    private final String value;

    SupervisoryBody(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
