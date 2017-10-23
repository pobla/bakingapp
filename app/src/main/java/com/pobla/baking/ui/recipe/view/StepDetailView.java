package com.pobla.baking.ui.recipe.view;


import android.database.Cursor;

public interface StepDetailView {

  void bindView(Cursor cursor);

  void showNext(boolean show);

  void showBack(boolean show);
}
