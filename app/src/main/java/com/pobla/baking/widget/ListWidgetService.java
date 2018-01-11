package com.pobla.baking.widget;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.pobla.baking.R;
import com.pobla.baking.data.storage.RecipeProvider.Ingredients;
import com.pobla.baking.data.storage.db.IngredientColumns;

import net.simonvt.schematic.Cursors;

public class ListWidgetService extends RemoteViewsService {

  private static String RECIPE_ID = "RECIPE_ID";

  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    int recipeId = intent.getIntExtra(RECIPE_ID, 0);
    return new ListRemoteViewsFactory(this.getApplicationContext(), recipeId);
  }

  public static Intent getIntent(Context context, int recipeId) {
    Intent intent = new Intent(context, ListWidgetService.class);
    intent.putExtra(RECIPE_ID, recipeId);
    return intent;
  }

  class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final int recipeId;
    private final Context context;
    private Cursor cursor;

    public ListRemoteViewsFactory(Context applicationContext, int recipeId) {
      this.context = applicationContext;
      this.recipeId = recipeId;


    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
      if (cursor != null) {
        cursor.close();
      }
      cursor = context.getContentResolver().query(Ingredients.fromRecipe(recipeId), null, null, null, null);
    }

    @Override
    public void onDestroy() {
      if (cursor != null)
        cursor.close();

    }

    @Override
    public int getCount() {
      return (cursor == null) ? 0 : cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
      if (cursor == null || cursor.getCount() == 0) return null;
      cursor.moveToPosition(position);

      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_list_item_widget);

      views.setTextViewText(R.id.id_text, String.format("%s %s", Cursors.getString(cursor, IngredientColumns.QUANTITY), Cursors.getString(cursor, IngredientColumns.MEASURE).toLowerCase()));
      views.setTextViewText(R.id.content, Cursors.getString(cursor, IngredientColumns.INGREDIENT));
      return views;

    }

    @Override
    public RemoteViews getLoadingView() {
      return null;
    }

    @Override
    public int getViewTypeCount() {
      return 1;
    }

    @Override
    public long getItemId(int i) {
      return i;
    }

    @Override
    public boolean hasStableIds() {
      return true;
    }
  }

}
