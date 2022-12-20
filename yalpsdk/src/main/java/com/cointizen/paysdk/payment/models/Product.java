package com.cointizen.paysdk.payment.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("createTime")
    @Expose
    private String createTime;

    @SerializedName("gameDeveloperId")
    @Expose
    private String gameDeveloperId;

    @SerializedName("platformProductId")
    @Expose
    private String platformProductId;

    @SerializedName("stripeProductId")
    @Expose
    private String stripeProductId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("unitAmount")
    @Expose
    private String unitAmount;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("caption")
    @Expose
    private String caption;

    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

    @SerializedName("createBy")
    @Expose
    private String createBy;

    @SerializedName("updateTime")
    @Expose
    private String updateTime;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("unitLabel")
    @Expose
    private String unitLabel;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("ygameDeveloperPlatformCpWalletAddress")
    @Expose
    private String ygameDeveloperPlatformCpWalletAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGameDeveloperId() {
        return gameDeveloperId;
    }

    public void setGameDeveloperId(String gameDeveloperId) {
        this.gameDeveloperId = gameDeveloperId;
    }

    public String getPlatformProductId() {
        return platformProductId;
    }

    public void setPlatformProductId(String platformProductId) {
        this.platformProductId = platformProductId;
    }

    public String getStripeProductId() {
        return stripeProductId;
    }

    public void setStripeProductId(String stripeProductId) {
        this.stripeProductId = stripeProductId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(String unitAmount) {
        this.unitAmount = unitAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitLabel() {
        return unitLabel;
    }

    public void setUnitLabel(String unitLabel) {
        this.unitLabel = unitLabel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getYgameDeveloperPlatformCpWalletAddress() {
        return ygameDeveloperPlatformCpWalletAddress;
    }

    public void setYgameDeveloperPlatformCpWalletAddress(String ygameDeveloperPlatformCpWalletAddress) {
        this.ygameDeveloperPlatformCpWalletAddress = ygameDeveloperPlatformCpWalletAddress;
    }

}
