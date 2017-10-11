package com.pobla.baking.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pobla.baking.R;
import com.pobla.baking.data.storage.RecipeProvider.Steps;
import com.pobla.baking.data.storage.db.StepColumns;
import com.pobla.baking.ui.recipe.presenter.DefaultRecipeStepsPresenter;
import com.pobla.baking.ui.recipe.view.RecipeListView;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity implements RecipeListView {

  public static void startActivity(Context context, int recipeId) {
    Intent intent = new Intent(context, RecipeListActivity.class);
    intent.putExtra(RECIPE_ID, recipeId);
    context.startActivity(intent);
  }

  private static final String RECIPE_ID = "RECIPE_ID";

  @BindView(R.id.recipe_list)
  RecyclerView recyclerView;
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
    recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mTwoPane));
  }


  @Override
  public void setTitle(String titleFromCursor) {
    getSupportActionBar().setTitle(titleFromCursor);
  }

  @Override
  public void bindSteps(Cursor data) {
    ((SimpleItemRecyclerViewAdapter) recyclerView.getAdapter()).setCursor(data);
  }

  @Override
  public void bindIngredients(Cursor data) {

  }

  public static class SimpleItemRecyclerViewAdapter
    extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final RecipeListActivity mParentActivity;
    private Cursor cursor;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String stepId = (String) view.getTag();
        if (mTwoPane) {
          Bundle arguments = new Bundle();
          arguments.putString(RecipeDetailFragment.ARG_ITEM_ID, stepId);
          RecipeDetailFragment fragment = new RecipeDetailFragment();
          fragment.setArguments(arguments);
          mParentActivity.getSupportFragmentManager().beginTransaction()
            .replace(R.id.recipe_detail_container, fragment)
            .commit();
        } else {
          Context context = view.getContext();
          Intent intent = new Intent(context, RecipeDetailActivity.class);
          intent.putExtra(RecipeDetailFragment.ARG_ITEM_ID, stepId);
          context.startActivity(intent);
        }
      }
    };

    SimpleItemRecyclerViewAdapter(RecipeListActivity parent, boolean twoPane) {
      mParentActivity = parent;
      mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_content, parent, false);
      return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
      cursor.moveToPosition(position);
      holder.mIdView.setText(Cursors.getString(cursor, StepColumns._ID));
      holder.mContentView.setText(Cursors.getString(cursor, StepColumns.SHORT_DESCRIPTION));
      holder.itemView.setTag(Cursors.getString(cursor, StepColumns._ID));
      holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
      return cursor != null ? cursor.getCount() : 0;
    }

    public void setCursor(Cursor data) {
      if (this.cursor != null) {
        cursor.close();
      }
      this.cursor = data;
      notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
      @BindView(R.id.id_text)
      TextView mIdView;
      @BindView(R.id.content)
      TextView mContentView;

      ViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, itemView);
      }
    }
  }
}
