package com.pobla.baking.data.model;


import android.content.ContentValues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pobla.baking.data.storage.RecipeColumns;

import java.util.List;

public class Recipe {

  @SerializedName("id")
  @Expose
  public int id;

  @SerializedName("name")
  @Expose
  public String name;

  @SerializedName("ingredients")
  @Expose
  public List<Ingredient> ingredients = null;

  @SerializedName("steps")
  @Expose
  public List<Step> steps = null;

  @SerializedName("servings")
  @Expose
  public int servings;

  @SerializedName("image")
  @Expose
  public String image;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<Ingredient> ingredients) {
    this.ingredients = ingredients;
  }

  public List<Step> getSteps() {
    return steps;
  }

  public void setSteps(List<Step> steps) {
    this.steps = steps;
  }

  public int getServings() {
    return servings;
  }

  public void setServings(int servings) {
    this.servings = servings;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public ContentValues toContentValue() {
    ContentValues contentValues = new ContentValues();
    contentValues.put(RecipeColumns._ID, getId());
    contentValues.put(RecipeColumns.TITLE, getName());
    return contentValues;
  }


}
