package com.pobla.baking.data;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pobla.baking.data.model.Recipe;
import com.pobla.baking.data.networking.BakingRestClient;
import com.pobla.baking.data.storage.RecipeDAO;

import java.net.URL;
import java.util.List;

public class BakingIntentService extends IntentService {

  private final BakingRestClient bakingRestClient = new BakingRestClient();

  public BakingIntentService() {
    super("BakingIntentService");
  }

  @Override
  protected void onHandleIntent(@Nullable Intent intent) {
    List<Recipe> recipes = bakingRestClient.retrieveRecipes();
    RecipeDAO recipeDAO = new RecipeDAO(getContentResolver());
    recipeDAO.storeRecipes(recipes);
  }

  public static void startImmediateSync(@NonNull final Context context) {
    Intent intentToSyncImmediately = new Intent(context, BakingIntentService.class);
    context.startService(intentToSyncImmediately);
  }

}
