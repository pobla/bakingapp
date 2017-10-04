package com.pobla.baking.data.model;

import android.content.ContentValues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pobla.baking.data.storage.schematic.values.IngredientsValuesBuilder;

public class Ingredient {

  @SerializedName("quantity")
  @Expose
  public float quantity;
  @SerializedName("measure")
  @Expose
  public String measure;
  @SerializedName("ingredient")
  @Expose
  public String ingredient;

  public float getQuantity() {
    return quantity;
  }

  public void setQuantity(float quantity) {
    this.quantity = quantity;
  }

  public String getMeasure() {
    return measure;
  }

  public void setMeasure(String measure) {
    this.measure = measure;
  }

  public String getIngredient() {
    return ingredient;
  }

  public void setIngredient(String ingredient) {
    this.ingredient = ingredient;
  }

  public ContentValues toContentValue(int recipeId) {
    IngredientsValuesBuilder builder = new IngredientsValuesBuilder();
    builder.recipeId(recipeId);
    builder.quantity(getQuantity());
    builder.measure(getMeasure());
    builder.ingredient(getIngredient());
    return builder.values();
  }
}