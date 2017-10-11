package com.pobla.baking.ui.main;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.pobla.baking.R;
import com.pobla.baking.data.BakingIntentService;
import com.pobla.baking.ui.main.presenter.DefaultMainViewPresenter;
import com.pobla.baking.ui.main.view.MainView;
import com.pobla.baking.ui.main.presenter.MainViewPresenter;
import com.pobla.baking.ui.main.view.RecipeListAdapter;
import com.pobla.baking.ui.main.view.RecipeListAdapter.ItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView, ItemClickListener{


  @BindView(R.id.constraintLayout_main_loading)
  View loadingView;

  @BindView(R.id.textView_main_noRecipes)
  View noRecipeView;

  @BindView(R.id.recycleView_main_recipelist)
  RecyclerView recipeList;

  private RecipeListAdapter recipeListAdapter;
  private MainViewPresenter presenter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    presenter = new DefaultMainViewPresenter(this, this, getSupportLoaderManager());
    BakingIntentService.startImmediateSync(this);

    recipeList.setLayoutManager(new LinearLayoutManager(this));
    recipeListAdapter = new RecipeListAdapter(this);
    recipeList.setAdapter(recipeListAdapter);



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
    Toast.makeText(this, "Typing:" +recipeId, Toast.LENGTH_SHORT).show();
  }
}
