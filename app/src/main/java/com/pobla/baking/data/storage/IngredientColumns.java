package com.pobla.baking.data.storage;


import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.Constraints;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.ForeignKeyConstraint;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;
import net.simonvt.schematic.annotation.UniqueConstraint;

import static net.simonvt.schematic.annotation.ConflictResolutionType.REPLACE;
import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.REAL;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

@Constraints(
  unique = @UniqueConstraint(
    name = "UNQ_STEP_FOR_RECIPE",
    columns = {IngredientColumns._ID, IngredientColumns.RECIPE_ID},
    onConflict = REPLACE),
  foreignKey = @ForeignKeyConstraint(
    name = "FK_RECIPE_ID",
    columns = IngredientColumns.RECIPE_ID,
    referencedTable = BakingDatabase.RECIPES,
    referencedColumns = RecipeColumns._ID
  )
)
public interface IngredientColumns {
  @DataType(INTEGER)
  @PrimaryKey
  @AutoIncrement
  String _ID = "_id";
  @DataType(REAL)
  String QUANTITY = "quantity";
  @DataType(TEXT)
  String MEASURE = "measure";
  @DataType(TEXT)
  String INGREDIENT = "ingredient";
  @DataType(INTEGER)
  @References(table = BakingDatabase.RECIPES, column = RecipeColumns._ID)
  String RECIPE_ID = "recipeId";

}
