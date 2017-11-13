package com.pobla.baking.ui.recipe.presenter;


public class StepDetail {
  private final int stepId;
  private final int recipeId;

  public StepDetail(int stepId, int recipeId) {
    this.stepId = stepId;
    this.recipeId = recipeId;
  }

  public int getStepId() {
    return stepId;
  }

  public int getRecipeId() {
    return recipeId;
  }

  public StepDetail nextStep() {
    return new StepDetail(stepId + 1, recipeId);
  }

  public StepDetail previousStep() {
    return new StepDetail(stepId - 1, recipeId);
  }
}