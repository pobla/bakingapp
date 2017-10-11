package com.pobla.baking.main.view;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pobla.baking.R;
import com.pobla.baking.data.storage.db.RecipeColumns;
import com.pobla.baking.main.view.RecipeListAdapter.RecipeContextHolder;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends Adapter<RecipeContextHolder> {


  private final ItemClickListener itemClickListener;

  private Cursor cursor;

  public RecipeListAdapter(ItemClickListener itemClickListener) {
    this.itemClickListener = itemClickListener;
  }


  @Override
  public RecipeContextHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    View view = LayoutInflater.from(context).inflate(R.layout.listitem_main_row, parent, false);
    return new RecipeContextHolder(view);
  }

  @Override
  public void onBindViewHolder(RecipeContextHolder holder, int position) {
    if (cursor != null && !cursor.isClosed()) {
      cursor.moveToPosition(position);
      holder.recipeName.setText(Cursors.getString(cursor, RecipeColumns.NAME));
      holder.servings.setText(Cursors.getString(cursor, RecipeColumns.SERVINGS));
      holder.steps.setText(Cursors.getStringOrNull(cursor, RecipeColumns.STEPS));
      //TODO bind image view
    }
  }

  @Override
  public int getItemCount() {
    return cursor != null ? cursor.getCount() : 0;
  }

  public void swapCursor(Cursor cursor) {
    if (this.cursor != null) {
      this.cursor.close();
    }
    this.cursor = cursor;
    notifyDataSetChanged();
  }

  class RecipeContextHolder extends RecyclerView.ViewHolder implements OnClickListener {

    @BindView(R.id.tv_main_row_recipeName)
    TextView recipeName;
    @BindView(R.id.tv_main_row_recipeServings)
    TextView servings;
    @BindView(R.id.tv_main_row_recipeSteps)
    TextView steps;


    public RecipeContextHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      int clickedPosition = getAdapterPosition();
      cursor.moveToPosition(clickedPosition);
      itemClickListener.onItemClick(Cursors.getInt(cursor, RecipeColumns._ID));

    }
  }

  public interface ItemClickListener {
    void onItemClick(int recipeId);
  }
}
