package com.pobla.baking;


import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;

import com.pobla.baking.data.storage.RecipeProvider.Recipes;
import com.pobla.baking.data.storage.db.RecipeColumns;
import com.pobla.baking.ui.main.MainActivity;
import com.pobla.baking.widget.ListWidgetService;

import net.simonvt.schematic.Cursors;

public class IngredientWidgetProvider extends AppWidgetProvider {

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int recipeId, int appWidgetId) {
    RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.ingredient_list_widget);
    rv.setTextViewText(R.id.tv_widget_name, getRecipeName(context, recipeId));
    rv.setRemoteAdapter(R.id.lv_widget_ingredients, ListWidgetService.getIntent(context, recipeId));
    rv.setOnClickPendingIntent(R.id.tv_widget_name, getMainAppIntent(context));
    appWidgetManager.updateAppWidget(appWidgetId, rv);
  }


  private static PendingIntent getMainAppIntent(Context context) {
    Intent intent = new Intent(context, MainActivity.class);
    return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
  }


  private static String getRecipeName(Context context, int recipeId) {
    Cursor query = context.getContentResolver().query(Recipes.withId(recipeId), null, null, null, null);
    if (query == null) return null;
    query.moveToFirst();
    String recipeName = Cursors.getString(query, RecipeColumns.NAME);
    query.close();
    return recipeName;
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    super.onUpdate(context, appWidgetManager, appWidgetIds);
  }


}
