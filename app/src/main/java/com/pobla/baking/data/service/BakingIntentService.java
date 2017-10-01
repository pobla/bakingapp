package com.pobla.baking.data.service;


import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.pobla.baking.data.model.Recipe;

import java.io.IOException;
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
    Call<List<Recipe>> recipes = bakingService.getRecipes();
    try {
      Response<List<Recipe>> execute = recipes.execute();
      List<Recipe> body = execute.body();
      Log.d(getClass().toString(), "List of Recipes! " + body);
    } catch (Exception e) {
      e.printStackTrace();
    }

  }
}
