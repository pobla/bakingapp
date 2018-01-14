package com.pobla.baking.ui.recipe.presenter;


import android.os.Parcel;
import android.os.Parcelable;

public class StepDetail implements Parcelable{
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


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.stepId);
    dest.writeInt(this.recipeId);
  }

  protected StepDetail(Parcel in) {
    this.stepId = in.readInt();
    this.recipeId = in.readInt();
  }

  public static final Creator<StepDetail> CREATOR = new Creator<StepDetail>() {
    @Override
    public StepDetail createFromParcel(Parcel source) {
      return new StepDetail(source);
    }

    @Override
    public StepDetail[] newArray(int size) {
      return new StepDetail[size];
    }
  };
}
