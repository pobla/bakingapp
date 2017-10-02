package com.pobla.baking.data.storage;


import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = BakingDatabase.VERSION)
public final class BakingDatabase {

  static final int VERSION = 4;

  @Table(RecipeColumns.class)
  public static final String RECIPES = "recipes";

  @Table(StepColumns.class)
  public static final String STEPS = "step";


}
