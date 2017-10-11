package com.pobla.baking.ui.recipe.presenter;


import android.database.Cursor;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

public interface RecipeStepsPresenter extends LoaderCallbacks<Cursor> {

  int RECIPE_TITLE_LOADER = 6794094;
  int RECIPE_STEPS_LOADER = 6794093;
  int RECIPE_INGREDIENTS_LOADER = 6794095;

  void queryTitle();
  void retrieveSteps();
}
