package com.pobla.baking.ui.main;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.DisplayMetrics;
import android.view.View;

import com.pobla.baking.IngredientWidgetProvider;
import com.pobla.baking.R;
import com.pobla.baking.data.BakingIntentService;
import com.pobla.baking.ui.main.presenter.DefaultMainViewPresenter;
import com.pobla.baking.ui.main.presenter.MainViewPresenter;
import com.pobla.baking.ui.main.view.MainView;
import com.pobla.baking.ui.main.view.RecipeListAdapter;
import com.pobla.baking.ui.main.view.RecipeListAdapter.ItemClickListener;
import com.pobla.baking.ui.recipe.RecipeStepsListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class MainActivity extends AppCompatActivity implements MainView, ItemClickListener {

  private static final int SCALING_FACTOR = 250;
  private static final String LAYOUT_MANAGER_STATE = "LAYOUT_MANAGER_STATE";

  @BindView(R.id.constraintLayout_main_loading)
  View loadingView;

  @BindView(R.id.textView_main_noRecipes)
  View noRecipeView;

  @BindView(R.id.recycleView_main_recipelist)
  RecyclerView recipeList;

  private RecipeListAdapter recipeListAdapter;
  private MainViewPresenter presenter;
  private int appWidgetId;
  private boolean isWidgetConfigurationRequest;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    presenter = new DefaultMainViewPresenter(this, this, getSupportLoaderManager());
    BakingIntentService.startImmediateSync(this);

    recipeList.setLayoutManager(calculateLayoutManager());
    recipeListAdapter = new RecipeListAdapter(this);
    recipeList.setAdapter(recipeListAdapter);

    Bundle extras = getIntent().getExtras();
    if (extras != null) {
      appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, INVALID_APPWIDGET_ID);
      isWidgetConfigurationRequest = appWidgetId != INVALID_APPWIDGET_ID;
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    presenter.retrieveRecipes();
  }

  @Override
  public void showLoadingDialog() {
    loadingView.setVisibility(View.VISIBLE);
  }

  @Override
  public void cancelLoadingDialog() {
    loadingView.setVisibility(View.GONE);
  }

  @Override
  public void bindData(Cursor data) {
    recipeList.setVisibility(View.VISIBLE);
    noRecipeView.setVisibility(View.GONE);

    recipeListAdapter.swapCursor(data);
  }

  @Override
  public void showNoRecipes() {
    recipeList.setVisibility(View.GONE);
    noRecipeView.setVisibility(View.VISIBLE);
  }

  @Override
  public void onItemClick(int recipeId) {
    if (!isWidgetConfigurationRequest) {
      RecipeStepsListActivity.startActivity(this, recipeId);
    } else {
      updateWidget(recipeId);
    }
  }

  private void updateWidget(int recipeId) {
    IngredientWidgetProvider.updateAppWidget(this, AppWidgetManager.getInstance(this), recipeId, appWidgetId);
    Intent resultValue = new Intent();
    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    setResult(RESULT_OK, resultValue);
    finish();
  }

  private LayoutManager calculateLayoutManager() {
    if(getResources().getBoolean(R.bool.isTablet)) {
      DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
      float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
      return new GridLayoutManager(this, (int) (dpWidth / SCALING_FACTOR));
    }
    return new LinearLayoutManager(this);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(LAYOUT_MANAGER_STATE, recipeList.getLayoutManager().onSaveInstanceState());
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    if (savedInstanceState != null) {
      Parcelable parcelable = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
      recipeList.getLayoutManager().onRestoreInstanceState(parcelable);
    }
  }

}
