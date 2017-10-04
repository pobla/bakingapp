package com.pobla.baking.data.storage.db;


import com.pobla.baking.data.storage.db.BakingDatabaseDeclaration;

import net.simonvt.schematic.annotation.Constraints;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.ForeignKeyConstraint;
import net.simonvt.schematic.annotation.References;
import net.simonvt.schematic.annotation.UniqueConstraint;

import static net.simonvt.schematic.annotation.ConflictResolutionType.REPLACE;
import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

@Constraints(
  unique = @UniqueConstraint(
    name = "UNQ_STEP_FOR_RECIPE",
    columns = {StepColumns._ID, StepColumns.RECIPE_ID},
    onConflict = REPLACE),
  foreignKey = @ForeignKeyConstraint(
    name = "FK_RECIPE_ID",
    columns = StepColumns.RECIPE_ID,
    referencedTable = BakingDatabaseDeclaration.RECIPES,
    referencedColumns = RecipeColumns._ID
  )

)
public interface StepColumns {
  @DataType(INTEGER)
  String _ID = "_id";

  @DataType(TEXT)
  String SHORT_DESCRIPTION = "shortDescription";

  @DataType(TEXT)
  String DESCRIPTION = "description";

  @DataType(TEXT)
  String VIDEO_URL = "videoURL";

  @DataType(TEXT)
  String THUMBNAIL_URL = "thumbnailURL";

  @DataType(INTEGER)
  @References(table = BakingDatabaseDeclaration.RECIPES, column = RecipeColumns._ID)
  String RECIPE_ID = "recipeId";


}
