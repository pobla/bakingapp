package com.pobla.baking.ui.recipe.view;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pobla.baking.R;
import com.pobla.baking.data.storage.db.IngredientColumns;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsRecyclerViewAdapter
  extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.ViewHolder> {

  private Cursor cursor;

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
    //Set image source if the service returns it.
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
