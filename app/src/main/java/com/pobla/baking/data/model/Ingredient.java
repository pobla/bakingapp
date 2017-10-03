package com.pobla.baking.data.model;

import android.content.ContentValues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pobla.baking.data.storage.IngredientColumns;

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
    ContentValues contentValues = new ContentValues();
    contentValues.put(IngredientColumns.QUANTITY, getQuantity());
    contentValues.put(IngredientColumns.MEASURE, getMeasure());
    contentValues.put(IngredientColumns.INGREDIENT, getIngredient());
    contentValues.put(IngredientColumns.RECIPE_ID, recipeId);
    return contentValues;
  }
}