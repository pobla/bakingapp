package com.pobla.baking.ui.recipe.presenter;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;

import com.pobla.baking.data.storage.RecipeProvider.Steps;
import com.pobla.baking.ui.recipe.view.StepDetailView;

import java.util.ArrayList;
import java.util.List;

public class DefaultStepDetailPresenter implements StepDetailPresenter {

  private final StepDetailView view;
  private final ContentResolver contentResolver;
  private StepDetail model;

  public DefaultStepDetailPresenter(StepDetailView view, ContentResolver contentResolver) {
    this.view = view;
    this.contentResolver = contentResolver;
  }

  @Override
  public void setModel(StepDetail model) {
    this.model = model;
  }

  @Override
  public StepDetail getModel() {
    return model;
  }

  @Override
  public void retrieveDetails() {
    new QueryAsyncTask(view, contentResolver).execute(this.model);
  }

  @Override
  public void showNextStep() {
    StepDetail stepDetail = model.nextStep();
    new QueryAsyncTask(view, contentResolver).execute(stepDetail);
    setModel(stepDetail);
  }

  @Override
  public void showPreviousStep() {
    StepDetail stepDetail = model.previousStep();
    new QueryAsyncTask(view, contentResolver).execute(stepDetail);
    setModel(stepDetail);
  }

  private static class QueryAsyncTask extends AsyncTask<StepDetail, Void, List<Cursor>> {

    private final StepDetailView view;
    private final ContentResolver contentResolver;

    public QueryAsyncTask(StepDetailView view, ContentResolver contentResolver) {
      this.view = view;
      this.contentResolver = contentResolver;
    }

    @Override
    protected List<Cursor> doInBackground(StepDetail... models) {
      StepDetail model = models[0];
      List<Cursor> cursorSet = new ArrayList<>(3);
      cursorSet.add(queryStep(model));
      cursorSet.add(queryStep(model.nextStep()));
      cursorSet.add(queryStep(model.previousStep()));
      cursorSet.remove(null);
      return cursorSet;
    }

    private Cursor queryStep(StepDetail detail) {
      if (detail.getStepId() < 0) return null;
      return contentResolver.query(Steps.fromRecipeWithStep(detail.getRecipeId(),detail.getStepId()), null, null, null, null);
    }

    @Override
    protected void onPostExecute(List<Cursor> cursor) {
      super.onPostExecute(cursor);
      view.bindView(cursor.get(0));
      view.showNext(cursor.get(1).getCount() > 0);
      view.showBack(cursor.size() > 2 && cursor.get(2).getCount() > 0);
    }
  }

}
