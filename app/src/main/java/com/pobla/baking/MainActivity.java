package com.pobla.baking;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pobla.baking.data.BakingIntentService;
import com.pobla.baking.data.storage.RecipeProvider.Recipes;
import com.pobla.baking.ui.RecipeListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

  @BindView(R.id.recycleView_main_recipelist)
  RecyclerView recipeList;

  private RecipeListAdapter recipeListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    recipeList.setLayoutManager(new LinearLayoutManager(this));
    recipeListAdapter = new RecipeListAdapter();
    recipeList.setAdapter(recipeListAdapter);

    Intent intentToSyncImmediately = new Intent(this, BakingIntentService.class);
    startService(intentToSyncImmediately);
    Cursor recipesCursor = getContentResolver().query(Recipes.RECIPES, null, null, null, null);
    recipeListAdapter.swapCursor(recipesCursor);


//    movieGrid.setLayoutManager(new ListAdapter(this));
//    movieGrid.setAdapter(mainViewAdapter);

//    testing.setOnClickListener(new OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        MainActivity context = MainActivity.this;
//        Intent intentToSyncImmediately = new Intent(context, BakingIntentService.class);
//        context.startService(intentToSyncImmediately);
//        Cursor recipesCursor = getContentResolver().query(Recipes.RECIPES, null, null, null, null);
//        textView.setText("We have: " + recipesCursor.getCount() + "\n");
//        while (recipesCursor.moveToNext()) {
//          textView.append(Cursors.getString(recipesCursor, RecipeColumns.NAME) + "Servings: "+ Cursors.getInt(recipesCursor, RecipeColumns.SERVINGS));
//          textView.append("\n");
//          Cursor ingridentsCursors = getContentResolver().query(Ingredients.fromRecipe(recipesCursor.getInt(0)), null, null, null, null);
//          while (ingridentsCursors.moveToNext()) {
//            textView.append("\t" + ingridentsCursors.getString(3) + " + ");
//          }
//          textView.append("\n");
//          ingridentsCursors.close();
//
//          Cursor stepsCursors = getContentResolver().query(Steps.fromRecipe(recipesCursor.getInt(0)), null, null, null, null);
//          while (stepsCursors.moveToNext()) {
//            textView.append("\t" + stepsCursors.getString(1));
//            textView.append("\n");
//          }
//          stepsCursors.close();
//        }
//        recipesCursor.close();
//
//      }
//    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    Cursor recipesCursor = getContentResolver().query(Recipes.RECIPES, null, null, null, null);
    recipeListAdapter.swapCursor(recipesCursor);
  }
}
