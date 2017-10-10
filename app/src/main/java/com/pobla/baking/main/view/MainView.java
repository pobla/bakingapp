package com.pobla.baking.main.view;


import android.database.Cursor;

public interface MainView {

  void showLoadingDialog();

  void cancelLoadingDialog();

  void bindData(Cursor data);

  void showNoRecipes();
}
