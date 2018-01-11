package com.pobla.baking.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsListActivity extends AppCompatActivity implements RecipeListView {

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
  }


  @Override
  public void setTitle(String titleFromCursor) {
    getSupportActionBar().setTitle(titleFromCursor);
  }

  @Override
  public void bindSteps(Cursor data) {
    ((RecipeStepsRecyclerViewAdapter) stepsList.getAdapter()).setCursor(data);
  }

  @Override
  public void bindIngredients(Cursor data) {
    ((IngredientsRecyclerViewAdapter) ingredientList.getAdapter()).setCursor(data);
  }

  public int getRecipeId(){
    return presenter.getRecipeId();
  }

}
