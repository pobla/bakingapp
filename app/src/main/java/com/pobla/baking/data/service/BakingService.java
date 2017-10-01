package com.pobla.baking.data.service;


import com.pobla.baking.data.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingService {

  @GET("baking.json")
  Call<List<Recipe>> getRecipes();
}
