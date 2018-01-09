package com.pobla.baking;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.espresso.matcher.ViewMatchers.Visibility;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import com.pobla.baking.ui.main.MainActivity;
import com.pobla.baking.ui.recipe.RecipeStepsListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.instanceOf;

@RunWith(AndroidJUnit4.class)
public class RecipeStepsListActivityInstrumentedTests {

  @Rule
  public ActivityTestRule<RecipeStepsListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeStepsListActivity.class, true, false);


  @Test
  public void showOpenDetailViewForSpecificStep() throws Exception {

    Context targetContext = InstrumentationRegistry.getInstrumentation()
                              .getTargetContext();
    Intent intent = new Intent(targetContext, RecipeStepsListActivity.class);
    intent.putExtra("RECIPE_ID", 1);
    mActivityTestRule.launchActivity(intent);

    onView(ViewMatchers.withId(R.id.recipe_list))
      .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

    onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
      .check(matches(withText("Recipe Introduction")));

    onView(ViewMatchers.withId(R.id.recipe_detail_video_player)).check(matches(isDisplayed()));
  }

}
