package com.pobla.baking.ui.recipe.view;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pobla.baking.R;
import com.pobla.baking.data.storage.db.IngredientColumns;
import com.pobla.baking.ui.recipe.RecipeDetailActivity;
import com.pobla.baking.ui.recipe.RecipeDetailFragment;
import com.pobla.baking.ui.recipe.RecipeListActivity;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsRecyclerViewAdapter
  extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.ViewHolder> {

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

  public IngredientsRecyclerViewAdapter(RecipeListActivity parent, boolean twoPane) {
    mParentActivity = parent;
    mTwoPane = twoPane;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
                  .inflate(R.layout.ingredient_list_content, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    cursor.moveToPosition(position);
    holder.tvQuantity.setText(Cursors.getString(cursor, IngredientColumns.QUANTITY));
    holder.txIngredientName.setText(Cursors.getString(cursor, IngredientColumns.INGREDIENT));
    holder.tvMeasure.setText(Cursors.getString(cursor, IngredientColumns.MEASURE).toLowerCase());
    holder.itemView.setTag(Cursors.getString(cursor, IngredientColumns._ID));
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
    @BindView(R.id.tv_ingredient_list_quantity)
    TextView tvQuantity;
    @BindView(R.id.tv_ingredient_list_name)
    TextView txIngredientName;
  @BindView(R.id.tv_ingredient_list_measure)
    TextView tvMeasure;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, itemView);
    }
  }
}
