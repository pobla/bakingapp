package com.pobla.baking.data.service;


import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.pobla.baking.data.model.Recipe;
import com.pobla.baking.data.storage.RecipeProvider.Recipes;

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
