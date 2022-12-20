package com.cointizen.paysdk.entity;

public class PrivacyInfoEntity {

    private int status;
    private String agreement_name;
    private String agreement_link;
    private String privacy_name;
    private String privacy_link;
    private String close_name;
    private String close_link;

    public boolean isShowDialog;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAgreement_name() {
        return agreement_name;
    }

    public void setAgreement_name(String agreement_name) {
        this.agreement_name = agreement_name;
    }

    public String getAgreement_link() {
        return agreement_link;
    }

    public void setAgreement_link(String agreement_link) {
        this.agreement_link = agreement_link;
    }

    public String getPrivacy_name() {
        return privacy_name;
    }

    public void setPrivacy_name(String privacy_name) {
        this.privacy_name = privacy_name;
    }

    public String getPrivacy_link() {
        return privacy_link;
    }

    public void setPrivacy_link(String privacy_link) {
        this.privacy_link = privacy_link;
    }

    public String getClose_name() {
        return close_name;
    }

    public void setClose_name(String close_name) {
        this.close_name = close_name;
    }

    public String getClose_link() {
        return close_link;
    }

    public void setClose_link(String close_link) {
        this.close_link = close_link;
    }
}
