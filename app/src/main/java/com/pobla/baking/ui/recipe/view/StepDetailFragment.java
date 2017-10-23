package com.pobla.baking.ui.recipe.view;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pobla.baking.R;
import com.pobla.baking.data.storage.RecipeProvider.Steps;
import com.pobla.baking.data.storage.db.StepColumns;
import com.pobla.baking.ui.recipe.RecipeStepsListActivity;
import com.pobla.baking.ui.recipe.StepDetailActivity;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeStepsListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment implements StepDetailView {

  public static final String RECIPE_ID = "RECIPE_ID";
  public static final String STEP_ID = "STEP_ID";

  public static StepDetailFragment newInstance(int recipeId, Integer stepId) {
    StepDetailFragment fragment = new StepDetailFragment();
    Bundle bundle = new Bundle();
    bundle.putInt(StepDetailFragment.RECIPE_ID, recipeId);
    bundle.putInt(StepDetailFragment.STEP_ID, stepId);
    fragment.setArguments(bundle);
    return fragment;
  }

  private int recipeId;
  private int stepId;

  @BindView(R.id.recipe_detail_description)
  TextView textViewDescription;
  @BindView(R.id.recipe_detail_button_next)
  FloatingActionButton fabNext;
  @BindView(R.id.recipe_detail_button_back)
  FloatingActionButton fabBack;

  public StepDetailFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(RECIPE_ID)) {
      recipeId = getArguments().getInt(RECIPE_ID);
      stepId = getArguments().getInt(STEP_ID);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    new QueryAsyncTask(this, this.getActivity().getContentResolver()).execute(Steps.fromRecipeWithStep(recipeId, stepId));
  }

  @Override
  public void bindView(Cursor cursor) {
    if (cursor != null && cursor.moveToNext()) {
      getActivity().setTitle(Cursors.getString(cursor, StepColumns.SHORT_DESCRIPTION));
      textViewDescription.setText(Cursors.getString(cursor, StepColumns.DESCRIPTION));
      updateNext();
      updateBack();
    }
  }

  private void updateBack() {
    if (stepId == 0) {
      fabBack.setVisibility(View.GONE);
      return;
    }
    Cursor query = this.getActivity().getContentResolver().query(Steps.fromRecipeWithStep(recipeId, stepId - 1), null, null, null, null);
    if (query != null) {
      fabBack.setVisibility(query.getCount() == 0 ? View.GONE : View.VISIBLE);
      query.close();
    }
  }

  private void updateNext() {
    Cursor query = this.getActivity().getContentResolver().query(Steps.fromRecipeWithStep(recipeId, stepId + 1), null, null, null, null);
    if (query != null) {
      fabNext.setVisibility(query.getCount() == 0 ? View.GONE : View.VISIBLE);
      query.close();
    }
  }

  @OnClick(R.id.recipe_detail_button_back)
  public void showPrevious() {
    stepId--;
    new QueryAsyncTask(this, this.getActivity().getContentResolver()).execute(Steps.fromRecipeWithStep(recipeId, stepId));
  }

  @OnClick(R.id.recipe_detail_button_next)
  public void showNext() {
    stepId++;
    new QueryAsyncTask(this, this.getActivity().getContentResolver()).execute(Steps.fromRecipeWithStep(recipeId, stepId));
  }

  private static class QueryAsyncTask extends AsyncTask<Uri, Void, Cursor> {

    private final StepDetailView view;
    private final ContentResolver contentResolver;

    public QueryAsyncTask(StepDetailView view, ContentResolver contentResolver) {
      this.view = view;
      this.contentResolver = contentResolver;
    }

    @Override
    protected Cursor doInBackground(Uri... uris) {
      return contentResolver.query(uris[0], null, null, null, null);
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
      super.onPostExecute(cursor);
      view.bindView(cursor);
    }
  }
}
