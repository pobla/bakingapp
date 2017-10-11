package com.pobla.baking.ui.recipe.view;


import android.database.Cursor;

public interface RecipeListView {

  void bindSteps(Cursor data);

  void setTitle(String titleFromCursor);

  void bindIngredients(Cursor data);

}
