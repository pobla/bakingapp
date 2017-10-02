package com.pobla.baking.data.model;

import android.content.ContentValues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pobla.baking.data.storage.StepColumns;

public class Step {

  @SerializedName("id")
  @Expose
  public int id;
  @SerializedName("shortDescription")
  @Expose
  public String shortDescription;
  @SerializedName("description")
  @Expose
  public String description;
  @SerializedName("videoURL")
  @Expose
  public String videoURL;
  @SerializedName("thumbnailURL")
  @Expose
  public String thumbnailURL;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getShortDescription() {
    return shortDescription;
  }

  public void setShortDescription(String shortDescription) {
    this.shortDescription = shortDescription;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getVideoURL() {
    return videoURL;
  }

  public void setVideoURL(String videoURL) {
    this.videoURL = videoURL;
  }

  public String getThumbnailURL() {
    return thumbnailURL;
  }

  public void setThumbnailURL(String thumbnailURL) {
    this.thumbnailURL = thumbnailURL;
  }

  public ContentValues toContentValue(int recipeId) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(StepColumns._ID, getId());
    contentValues.put(StepColumns.SHORT_DESCRIPTION, getShortDescription());
    contentValues.put(StepColumns.DESCRIPTION, getDescription());
    contentValues.put(StepColumns.VIDEO_URL, getVideoURL());
    contentValues.put(StepColumns.THUMBNAIL_URL, getThumbnailURL());
    contentValues.put(StepColumns.RECIPE_ID, recipeId);
    return contentValues;
  }
}