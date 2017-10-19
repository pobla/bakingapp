package com.pobla.baking.ui.recipe.presenter;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.pobla.baking.data.storage.RecipeProvider.Ingredients;
import com.pobla.baking.data.storage.RecipeProvider.Recipes;
import com.pobla.baking.data.storage.RecipeProvider.Steps;
import com.pobla.baking.data.storage.db.RecipeColumns;
import com.pobla.baking.ui.recipe.view.RecipeListView;

import net.simonvt.schematic.Cursors;

public class DefaultRecipeStepsPresenter implements RecipeStepsPresenter {

  private final Context context;
  private final LoaderManager loaderManager;

  private final int recipeId;
  private final RecipeListView view;

  public DefaultRecipeStepsPresenter(Context context, RecipeListView view, LoaderManager loaderManager, int recipeId) {
    this.context = context;
    this.loaderManager = loaderManager;
    this.view = view;
    this.recipeId = recipeId;
  }

  @Override
  public void queryTitle() {
    loaderManager.restartLoader(RecipeStepsPresenter.RECIPE_TITLE_LOADER, null, this);
  }

  @Override
  public void retrieveSteps() {
    loaderManager.restartLoader(RecipeStepsPresenter.RECIPE_STEPS_LOADER, null, this);
    loaderManager.restartLoader(RecipeStepsPresenter.RECIPE_INGREDIENTS_LOADER, null, this);
  }


  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    switch (id){
      case RECIPE_STEPS_LOADER:
        return new CursorLoader(context, Steps.fromRecipe(recipeId), null, null, null, null);
      case RECIPE_TITLE_LOADER:
        return new CursorLoader(context, Recipes.withId(recipeId), new String[]{RecipeColumns.NAME}, null, null, null);
      case RECIPE_INGREDIENTS_LOADER:
        return new CursorLoader(context, Ingredients.fromRecipe(recipeId), null, null, null, null);
      default:
        return null;
    }
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    switch (loader.getId()){
      case RECIPE_STEPS_LOADER:
        view.bindSteps(data);
        break;
      case RECIPE_INGREDIENTS_LOADER:
        view.bindIngredients(data);
        break;
      case RECIPE_TITLE_LOADER:
        view.setTitle(getTitleFromCursor(data));
        break;
    }

  }

  private String getTitleFromCursor(Cursor data) {
    if(data != null){
      data.moveToNext();
      return Cursors.getString(data, RecipeColumns.NAME);
    }
    return null;
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
//    mainView.bindSteps(null);
  }

  public int getRecipeId() {
    return recipeId;
  }

}
