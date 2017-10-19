package com.pobla.baking.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.pobla.baking.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity {

  public static void startActivity(Context context, int recipeId, int stepId) {
    Intent intent = new Intent(context, RecipeDetailActivity.class);
    intent.putExtra(RecipeDetailFragment.RECIPE_ID, recipeId);
    intent.putExtra(RecipeDetailFragment.STEP_ID, stepId);
    context.startActivity(intent);
  }

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_detail);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    if (savedInstanceState == null) {
      RecipeDetailFragment fragment = new RecipeDetailFragment();
      fragment.setArguments(getIntent().getExtras());
      getSupportFragmentManager().beginTransaction()
        .add(R.id.recipe_detail_container, fragment)
        .commit();
    }
  }

}
