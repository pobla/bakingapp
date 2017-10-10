package com.pobla.baking.main.presenter;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.pobla.baking.data.storage.RecipeProvider.Recipes;
import com.pobla.baking.data.storage.db.RecipeColumns;
import com.pobla.baking.main.view.MainView;

public class DefaultMainViewPresenter implements MainViewPresenter {

  private final Context context;
  private final MainView mainView;
  private final LoaderManager loaderManager;

  public DefaultMainViewPresenter(Context context, MainView mainView, LoaderManager loaderManager) {
    this.context = context;
    this.mainView = mainView;
    this.loaderManager = loaderManager;
  }

  @Override
  public void retrieveRecipes() {
    loaderManager.restartLoader(MainViewPresenter.MAIN_VIEW_LOADER, null, this);
  }


  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    if (MAIN_VIEW_LOADER != id) {
      return null;
    }
    mainView.showLoadingDialog();
    return new CursorLoader(context, Recipes.RECIPES, RecipeColumns.ALL_COLUMNS, null, null, null);
  }

  @Override
  public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    mainView.cancelLoadingDialog();
    if (data == null || data.getCount() == 0) {
      mainView.showNoRecipes();
    } else {
      mainView.bindData(data);
    }
  }

  @Override
  public void onLoaderReset(Loader<Cursor> loader) {
    mainView.bindData(null);
  }
}
