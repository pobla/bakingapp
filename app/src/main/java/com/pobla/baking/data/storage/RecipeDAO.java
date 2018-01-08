package com.pobla.baking.data.storage;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.pobla.baking.data.model.Ingredient;
import com.pobla.baking.data.model.Recipe;
import com.pobla.baking.data.model.Step;
import com.pobla.baking.data.storage.RecipeProvider.Ingredients;
import com.pobla.baking.data.storage.RecipeProvider.Recipes;
import com.pobla.baking.data.storage.RecipeProvider.Steps;

import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {

  private final ContentResolver resolver;

  public RecipeDAO(ContentResolver resolver) {
    this.resolver = resolver;
  }

  public void storeRecipes(List<Recipe> recipes) {
    if(recipes != null && recipes.size() >0 ) {
      ContentValues[] contentValues = convertToContentValues(recipes);
      resolver.delete(Recipes.RECIPES, null, null);
      resolver.bulkInsert(Recipes.RECIPES, contentValues);
      insertStepsAndIngredients(recipes);
    }
  }

  private void insertStepsAndIngredients(List<Recipe> recipes) {
    for (Recipe recipe : recipes) {
      storeSteps(recipe);
      storeIngredients(recipe);
    }
  }

  private void storeIngredients(Recipe recipe) {
    resolver.delete(Ingredients.fromRecipe(recipe.getId()), null, null);
    insertIngredientsForRecipe(recipe);
  }

  private void storeSteps(Recipe recipe) {
    resolver.delete(Steps.fromRecipe(recipe.getId()), null, null);
    insertStepsForRecipe(recipe);
  }

  private void insertStepsForRecipe(Recipe recipe) {
    List<ContentValues> contentValues = new ArrayList<>(recipe.getSteps().size());
    for (Step step : recipe.getSteps()) {
      contentValues.add(step.toContentValue(recipe.getId()));
    }
    resolver.bulkInsert(Steps.fromRecipe(recipe.getId()), contentValues.toArray(new ContentValues[0]));
  }

  private void insertIngredientsForRecipe(Recipe recipe) {
    List<ContentValues> contentValues = new ArrayList<>(recipe.getIngredients().size());
    for (Ingredient ingredient : recipe.getIngredients()) {
      contentValues.add(ingredient.toContentValue(recipe.getId()));
    }
    resolver.bulkInsert(Ingredients.fromRecipe(recipe.getId()), contentValues.toArray(new ContentValues[0]));
  }

  @NonNull
  private ContentValues[] convertToContentValues(List<Recipe> recipes) {
    List<ContentValues> contentValues = new ArrayList<>(recipes.size());
    for (Recipe recipe : recipes) {
      contentValues.add(recipe.toContentValue());
    }
    return contentValues.toArray(new ContentValues[0]);
  }
}
