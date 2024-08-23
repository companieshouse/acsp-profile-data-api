package uk.gov.companieshouse.acspprofile.api.model.enums;

public enum SupervisoryBody {

    FINANCIAL_CONDUCT_AUTHORITY_FCA("financial-conduct-authority-fca"),
    GAMBLING_COMMISSION("gambling-commission"),
    HM_REVENUE_CUSTOMS_HMRC("hm-revenue-customs-hmrc"),
    ASSOCIATION_OF_ACCOUNTING_TECHNICIANS_AAT("association-of-accounting-technicians-aat"),
    ASSOCIATION_OF_CHARTERED_CERTIFIED_ACCOUNTANTS_ACCA("association-of-chartered-certified-accountants-acca"),
    ASSOCIATION_OF_INTERNATIONAL_ACCOUNTANTS_AIA("association-of-international-accountants-aia"),
    ASSOCIATION_OF_TAXATION_TECHNICIANS_ATT("association-of-taxation-technicians-att"),
    CHARTERED_INSTITUTE_OF_LEGAL_EXECUTIVES_CILEX("chartered-institute-of-legal-executives-cilex"),
    CHARTERED_INSTITUTE_OF_MANAGEMENT_ACCOUNTANTS_CIMA("chartered-institute-of-management-accountants-cima"),
    CHARTERED_INSTITUTE_OF_TAXATION_CIOT("chartered-institute-of-taxation-ciot"),
    COUNCIL_FOR_LICENSED_CONVEYANCERS_CLC("council-for-licensed-conveyancers-clc"),
    FACULTY_OFFICE_OF_THE_ARCHBISHOP_OF_CANTERBURY("faculty-office-of-the-archbishop-of-canterbury"),
    FACULTY_OF_ADVOCATES("faculty-of-advocates"),
    GENERAL_COUNCIL_OF_THE_BAR_BAR_STANDARDS_BOARD("general-council-of-the-bar-bar-standards-board"),
    GENERAL_COUNCIL_OF_THE_BAR_OF_NORTHERN_IRELAND("general-council-of-the-bar-of-northern-ireland"),
    INSOLVENCY_PRACTITIONERS_ASSOCIATION_IPA("insolvency-practitioners-association-ipa"),
    INSTITUTE_OF_CERTIFIED_BOOKKEEPERS_ICB("institute-of-certified-bookkeepers-icb"),
    INSTITUTE_OF_CHARTERED_ACCOUNTANTS_IN_ENGLAND_AND_WALES_ICAEW("institute-of-chartered-accountants-in-england-and-wales-icaew"),
    INSTITUTE_OF_CHARTERED_ACCOUNTANTS_IN_IRELAND_ICAI("institute-of-chartered-accountants-in-ireland-icai"),
    INSTITUTE_OF_CHARTERED_ACCOUNTANTS_OF_SCOTLAND_ICAS("institute-of-chartered-accountants-of-scotland-icas"),
    INSTITUTE_OF_FINANCIAL_ACCOUNTANTS_IFA("institute-of-financial-accountants-ifa"),
    INTERNATIONAL_ASSOCIATION_OF_BOOKKEEPERS_IAB("international-association-of-bookkeepers-iab"),
    LAW_SOCIETY_SOLICITORS_REGULATION_AUTHORITY_SRA("law-society-solicitors-regulation-authority-sra"),
    LAW_SOCIETY_OF_NORTHERN_IRELAND("law-society-of-northern-ireland"),
    LAW_SOCIETY_OF_SCOTLAND("law-society-of-scotland"),
    CHARTERED_INSTITUTE_OF_PUBLIC_FINANCE_AND_ACCOUNTANCY_CIPFA("chartered-institute-of-public-finance-and-accountancy-cipfa"),
    ROYAL_INSTITUTION_OF_CHARTERED_SURVEYORS_RICS("royal-institution-of-chartered-surveyors-rics"),
    UK_ASSOCIATION_OF_ONLINE_GAMBLING_OPERATORS_UKAO("uk-association-of-online-gambling-operators-ukao");

    private final String value;

    SupervisoryBody(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
