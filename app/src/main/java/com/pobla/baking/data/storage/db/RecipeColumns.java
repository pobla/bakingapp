package com.pobla.baking.data.storage.db;


import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface RecipeColumns {
  @DataType(INTEGER)
  @PrimaryKey
  String _ID = "_id";
  @DataType(TEXT)
  @NotNull
  String NAME = "name";
  @DataType(INTEGER)
  String SERVINGS = "servings";
  @DataType(TEXT)
  String IMAGE = "image";

}
