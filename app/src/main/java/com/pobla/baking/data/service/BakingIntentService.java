package com.pobla.baking.data.service;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.pobla.baking.data.model.Recipe;
import com.pobla.baking.data.storage.RecipeProvider.Ingredients;
import com.pobla.baking.data.storage.RecipeProvider.Recipes;
import com.pobla.baking.data.storage.RecipeProvider.Steps;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BakingIntentService extends IntentService {

  private static final String BAKING_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
  private final BakingService bakingService;

  public BakingIntentService() {
    super("BakingIntentService");
    bakingService = new Retrofit.Builder()
                      .baseUrl(BAKING_BASE_URL)
                      .addConverterFactory(GsonConverterFactory.create(new Gson()))
                      .build()
                      .create(BakingService.class);
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    List<Recipe> recipes = retrieveRecipesFromService();
    ContentValues[] contentValues = convertToContentValues(recipes);
    getContentResolver().delete(Recipes.RECIPES, null, null);
    getContentResolver().bulkInsert(Recipes.RECIPES, contentValues);
    insertStepsAndIngredients(recipes);

  }

  private void insertStepsAndIngredients(List<Recipe> recipes) {
    for (int i = 0; i < recipes.size(); i++) {
      Recipe recipe = recipes.get(i);
      getContentResolver().delete(Steps.fromRecipe(recipe.getId()), null, null);
      getContentResolver().delete(Ingredients.fromRecipe(recipe.getId()), null, null);
      insertStepsForRecipe(recipe);
      insertIngredientsForRecipe(recipe);
    }

  }

  private void insertStepsForRecipe(Recipe recipe) {
    ContentValues[] contentValues = new ContentValues[recipe.getSteps().size()];
    for (int i = 0; i < recipe.getSteps().size(); i++) {
      contentValues[i] = recipe.getSteps().get(i).toContentValue(recipe.getId());
    }
    getContentResolver().bulkInsert(Steps.fromRecipe(recipe.getId()), contentValues);
  }

  private void insertIngredientsForRecipe(Recipe recipe) {
    ContentValues[] contentValues = new ContentValues[recipe.getIngredients().size()];
    for (int i = 0; i < recipe.getIngredients().size(); i++) {
      contentValues[i] = recipe.getIngredients().get(i).toContentValue(recipe.getId());
    }
    getContentResolver().bulkInsert(Ingredients.fromRecipe(recipe.getId()), contentValues);
  }

  @NonNull
  private ContentValues[] convertToContentValues(List<Recipe> recipes) {
    ContentValues[] contentValues = new ContentValues[recipes.size()];
    for (int i = 0; i < recipes.size(); i++) {
      contentValues[i] = recipes.get(i).toContentValue();
    }
    return contentValues;
  }

  private List<Recipe> retrieveRecipesFromService() {
    try {
      Call<List<Recipe>> recipes = bakingService.getRecipes();
      Response<List<Recipe>> execute = recipes.execute();
      return execute.body();
    } catch (IOException e) {
      Log.e(getClass().toString(), "Error retriving recipes from web services", e);
    }
    return Collections.emptyList();
  }

}
