package com.pobla.baking.ui.main.presenter;


import android.database.Cursor;
import android.support.v4.app.LoaderManager.LoaderCallbacks;

public interface MainViewPresenter extends LoaderCallbacks<Cursor> {

  int MAIN_VIEW_LOADER = 123123;

  void retrieveRecipes();
}
