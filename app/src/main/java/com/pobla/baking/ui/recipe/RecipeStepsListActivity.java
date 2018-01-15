package com.pobla.baking.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.pobla.baking.R;
import com.pobla.baking.ui.recipe.presenter.DefaultRecipeStepsPresenter;
import com.pobla.baking.ui.recipe.view.IngredientsRecyclerViewAdapter;
import com.pobla.baking.ui.recipe.view.RecipeListView;
import com.pobla.baking.ui.recipe.view.RecipeStepsRecyclerViewAdapter;
import com.pobla.baking.ui.recipe.view.StepDetailFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsListActivity extends AppCompatActivity implements RecipeListView {

  private static final String STEP_LIST_STATE = "STEP_LIST_STATE";
  private static final String INGREDIENTS_LIST_STATE = "INGREDIENTS_LIST_STATE";
  private Bundle savedInstanceState;

  public static void startActivity(Context context, int recipeId) {
    context.startActivity(getIntent(context, recipeId));
  }

  public static Intent getIntent(Context context, int recipeId) {
    Intent intent = new Intent(context, RecipeStepsListActivity.class);
    intent.putExtra(RECIPE_ID, recipeId);
    return intent;
  }

  private static final String RECIPE_ID = "RECIPE_ID";

  @BindView(R.id.recipe_list)
  RecyclerView stepsList;
  @BindView(R.id.ingredient_list)
  RecyclerView ingredientList;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  private DefaultRecipeStepsPresenter presenter;
  private boolean mTwoPane;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_list);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    this.savedInstanceState = savedInstanceState;
    presenter = new DefaultRecipeStepsPresenter(this, this, getSupportLoaderManager(), getIntent().getIntExtra(RECIPE_ID, -1));
    presenter.queryTitle();
    presenter.retrieveSteps();

    mTwoPane = findViewById(R.id.recipe_detail_container) != null;
    stepsList.setAdapter(new RecipeStepsRecyclerViewAdapter(this, mTwoPane));
    DividerItemDecoration decor = new DividerItemDecoration(stepsList.getContext(), DividerItemDecoration.VERTICAL);
    decor.setDrawable(ContextCompat.getDrawable(this, R.color.colorPrimaryDark));
    stepsList.addItemDecoration(decor);
    ingredientList.setAdapter(new IngredientsRecyclerViewAdapter());
    ingredientList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

    if(mTwoPane && getSupportFragmentManager().findFragmentById(R.id.recipe_detail_container) == null){
      new Handler().post(new Runnable() {
        public void run() {
          StepDetailFragment fragment = StepDetailFragment.newInstance(getIntent().getIntExtra(StepDetailFragment.RECIPE_ID, -1), getIntent().getIntExtra(StepDetailFragment.STEP_ID, 0));
            getSupportFragmentManager().beginTransaction()
              .replace(R.id.recipe_detail_container, fragment)
              .commit();
          }
      });

    }
  }


  @Override
  public void setTitle(String titleFromCursor) {
    getSupportActionBar().setTitle(titleFromCursor);
  }

  @Override
  public void bindSteps(Cursor data) {
    ((RecipeStepsRecyclerViewAdapter) stepsList.getAdapter()).setCursor(data);
    if (savedInstanceState != null && savedInstanceState.containsKey(STEP_LIST_STATE)) {
      stepsList.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(STEP_LIST_STATE));
    }
  }

  @Override
  public void bindIngredients(Cursor data) {
    ((IngredientsRecyclerViewAdapter) ingredientList.getAdapter()).setCursor(data);
    if (savedInstanceState != null && savedInstanceState.containsKey(INGREDIENTS_LIST_STATE)) {
      ingredientList.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(INGREDIENTS_LIST_STATE));
    }
  }

  public int getRecipeId() {
    return presenter.getRecipeId();
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
    super.onRestoreInstanceState(savedInstanceState);
    this.savedInstanceState = savedInstanceState;
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putParcelable(INGREDIENTS_LIST_STATE, ingredientList.getLayoutManager().onSaveInstanceState());
    outState.putParcelable(STEP_LIST_STATE, stepsList.getLayoutManager().onSaveInstanceState());
  }
}
