package com.pobla.baking.ui.recipe.presenter;


public interface StepDetailPresenter {

  void setModel(StepDetail model);

  void retrieveDetails();

  void showNextStep();

  void showPreviousStep();
}
