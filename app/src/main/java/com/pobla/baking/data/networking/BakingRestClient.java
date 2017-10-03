package com.pobla.baking.data.networking;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.pobla.baking.data.model.Recipe;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BakingRestClient {

  private static final String BAKING_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
  private final BakingService bakingService;

  public BakingRestClient() {
    bakingService = new Retrofit.Builder()
                      .baseUrl(BAKING_BASE_URL)
                      .addConverterFactory(GsonConverterFactory.create(new Gson()))
                      .build()
                      .create(BakingService.class);
  }

  @NonNull
  public List<Recipe> retrieveRecipes() {
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
