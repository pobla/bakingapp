package com.pobla.baking.data.storage;


import android.net.Uri;

import com.pobla.baking.BuildConfig;
import com.pobla.baking.data.storage.db.BakingDatabaseDeclaration;
import com.pobla.baking.data.storage.db.IngredientColumns;
import com.pobla.baking.data.storage.db.RecipeColumns;
import com.pobla.baking.data.storage.db.StepColumns;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.MapColumns;
import net.simonvt.schematic.annotation.TableEndpoint;

import java.util.HashMap;
import java.util.Map;

import static com.pobla.baking.data.storage.RecipeProvider.AUTHORITY;

@ContentProvider(authority = AUTHORITY,
  database = BakingDatabaseDeclaration.class,
  packageName = "com.pobla.baking.data.storage.schematic"
)
public final class RecipeProvider {

  public static final String AUTHORITY = BuildConfig.PROVIDER_AUTHORITY;

  private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

  private interface Path {
    String RECIPE = "recipes";
    String STEPS = "steps";
    String INGREDIENTS = "ingredients";
  }


  @TableEndpoint(table = BakingDatabaseDeclaration.RECIPES)
  public static class Recipes {

    @MapColumns
    public static Map<String, String> mapColumns() {
      Map<String, String> map = new HashMap<>();
      map.put(RecipeColumns.STEPS, LIST_STEPS);
      return map;
    }

    @ContentUri(path = Path.RECIPE, type = "vnd.android.cursor.dir/recipes", defaultSort = RecipeColumns._ID + " ASC")
    public static final Uri RECIPES = Uri.parse("content://" + AUTHORITY + "/recipes");

    @InexactContentUri(
      name = "RECIPE_WITH_ID",
      path = Path.RECIPE + "/#",
      type = "vnd.android.cursor.dir/recipe",
      whereColumn = RecipeColumns._ID,
      pathSegment = 1)
    public static Uri withId(int recipeId) {
      return buildUri(Path.RECIPE, String.valueOf(recipeId));
    }

    static final String LIST_STEPS = "(SELECT COUNT(*) FROM "
                                       + BakingDatabaseDeclaration.STEPS
                                       + " WHERE "
                                       + BakingDatabaseDeclaration.STEPS
                                       + "."
                                       + StepColumns.RECIPE_ID
                                       + "="
                                       + BakingDatabaseDeclaration.RECIPES
                                       + "."
                                       + RecipeColumns._ID
                                       + ")";
  }

  @TableEndpoint(table = BakingDatabaseDeclaration.STEPS)
  public static class Steps {

    @InexactContentUri(
      name = "STEPS_FROM_RECIPE",
      path = Path.RECIPE + "/" + Path.STEPS + "/#",
      type = "vnd.android.cursor.dir/steps",
      whereColumn = StepColumns.RECIPE_ID,
      pathSegment = 2)
    public static Uri fromRecipe(int recipeId) {
      return buildUri(Path.RECIPE, Path.STEPS, String.valueOf(recipeId));
    }

    @InexactContentUri(
      name = "STEP_FROM_RECIPE",
      path = Path.RECIPE + "/#/" + Path.STEPS + "/#",
      type = "vnd.android.cursor.dir/step",
      whereColumn = {StepColumns._ID, StepColumns.RECIPE_ID},
      pathSegment = {1, 3})
    public static Uri fromRecipeWithStep(int recipeId, int stepId) {
      return buildUri(Path.RECIPE, String.valueOf(recipeId), Path.STEPS, String.valueOf(stepId));
    }
  }


  @TableEndpoint(table = BakingDatabaseDeclaration.INGREDIENTS)
  public static class Ingredients {

    @InexactContentUri(
      name = "INGREDIENTS_FROM_RECIPE",
      path = Path.RECIPE + "/" + Path.INGREDIENTS + "/#",
      type = "vnd.android.cursor.dir/ingredients",
      whereColumn = IngredientColumns.RECIPE_ID,
      pathSegment = 2)
    public static Uri fromRecipe(int recipeId) {
      return buildUri(Path.RECIPE, Path.INGREDIENTS, String.valueOf(recipeId));
    }
  }


  private static Uri buildUri(String... paths) {
    Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
    for (String path : paths) {
      builder.appendPath(path);
    }
    return builder.build();
  }


}

