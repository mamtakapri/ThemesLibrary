package com.example.retrofitexample.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("img_url")
    @Expose
    private Object imgUrl;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("theme_category_id")
    @Expose
    private Integer themeCategoryId;
    @SerializedName("meta_data")
    @Expose
    private Object metaData;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("category")
    @Expose
    private Category category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(Object imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getThemeCategoryId() {
        return themeCategoryId;
    }

    public void setThemeCategoryId(Integer themeCategoryId) {
        this.themeCategoryId = themeCategoryId;
    }

    public Object getMetaData() {
        return metaData;
    }

    public void setMetaData(Object metaData) {
        this.metaData = metaData;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}