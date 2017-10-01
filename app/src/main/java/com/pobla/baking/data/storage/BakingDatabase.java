package com.pobla.baking.data.storage;


import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = BakingDatabase.VERSION)
public final class BakingDatabase {

  static final int VERSION = 2;

  @Table(RecipeColumns.class)
  public static final String RECIPES = "recipes";

}
