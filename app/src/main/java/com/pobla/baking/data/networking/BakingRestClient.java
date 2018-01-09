package com.pobla.baking.data.networking;


import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.pobla.baking.BuildConfig;
import com.pobla.baking.data.model.Recipe;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BakingRestClient {

  private final BakingService bakingService;

  public BakingRestClient() {
    bakingService = new Retrofit.Builder()
                      .baseUrl(BuildConfig.BAKING_BASE_URL)
                      .addConverterFactory(GsonConverterFactory.create(new Gson()))
                      .build()
                      .create(BakingService.class);
  }

  @NonNull
  public List<Recipe> retrieveRecipes() {
    try {
      Call<List<Recipe>> recipes = bakingService.getRecipes();
      Response<List<Recipe>> execute = recipes.execute();
      List<Recipe> body = execute.body();
      return body != null ? body : Collections.<Recipe>emptyList();
    } catch (IOException e) {
      Log.e(getClass().toString(), "Error retrieving recipes from web services", e);
    }
    return Collections.emptyList();
  }
}
