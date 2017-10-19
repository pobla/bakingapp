package com.pobla.baking.ui.recipe;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pobla.baking.R;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {

  public static final String RECIPE_ID = "RECIPE_ID";
  public static final String STEP_ID = "STEP_ID";

  public static Bundle buildArgs(int recipeId, Integer stepId) {
    Bundle bundle = new Bundle();
    bundle.putInt(RecipeDetailFragment.RECIPE_ID, recipeId);
    bundle.putInt(RecipeDetailFragment.STEP_ID, stepId);
    return bundle;
  }

  private int recipeId;
  private int stepId;

  public RecipeDetailFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(RECIPE_ID)) {
      recipeId = getArguments().getInt(RECIPE_ID);
      stepId = getArguments().getInt(STEP_ID);

      Activity activity = this.getActivity();
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

    // Show the dummy content as text in a TextView.
    ((TextView) rootView.findViewById(R.id.recipe_detail)).setText("Step: " + stepId + " Recipe:" + recipeId);

    return rootView;
  }

}
