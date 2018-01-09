package com.pobla.baking;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.pobla.baking.ui.main.MainActivity;

public class IngredientWidgetProvider extends AppWidgetProvider {

  //TODO finisj widget confiugration + layout:
//  https://developer.android.com/guide/topics/appwidgets/index.html
//  https://developer.android.com/guide/topics/appwidgets/index.html#Configuring
  public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int recipeId, int appWidgetId) {
    Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
    int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
    RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.ingredient_list_widget);
    rv.setTextViewText(R.id.tv_sample, "Testing " + recipeId);

    appWidgetManager.updateAppWidget(appWidgetId, rv);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    super.onUpdate(context, appWidgetManager, appWidgetIds);
  }


}
