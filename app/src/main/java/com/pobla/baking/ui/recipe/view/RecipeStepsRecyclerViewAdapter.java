package com.pobla.baking.ui.recipe.view;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pobla.baking.R;
import com.pobla.baking.data.storage.db.StepColumns;
import com.pobla.baking.ui.recipe.RecipeStepsListActivity;
import com.pobla.baking.ui.recipe.StepDetailActivity;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsRecyclerViewAdapter
  extends RecyclerView.Adapter<RecipeStepsRecyclerViewAdapter.ViewHolder> {

  private final RecipeStepsListActivity mParentActivity;
  private Cursor cursor;
  private final boolean mTwoPane;
  private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      String stepId = (String) view.getTag();
      if (mTwoPane) {
        StepDetailFragment fragment = StepDetailFragment.newInstance(mParentActivity.getRecipeId(), Integer.valueOf(stepId));
        mParentActivity.getSupportFragmentManager().beginTransaction()
          .replace(R.id.recipe_detail_container, fragment)
          .commit();
      } else {
        StepDetailActivity.startActivity(view.getContext(), mParentActivity.getRecipeId(), Integer.valueOf(stepId));
      }
    }
  };

  public RecipeStepsRecyclerViewAdapter(RecipeStepsListActivity parent, boolean twoPane) {
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

  static class ViewHolder extends RecyclerView.ViewHolder {
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
