package com.pobla.baking.data.storage;


import android.net.Uri;

import com.pobla.baking.BuildConfig;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import static com.pobla.baking.data.storage.RecipeProvider.AUTHORITY;

@ContentProvider(authority = AUTHORITY, database = BakingDatabase.class)
public final class RecipeProvider {

  public static final String AUTHORITY = BuildConfig.PROVIDER_AUTHORITY;

  @TableEndpoint(table = BakingDatabase.RECIPES)
  public static class Recipes {
    @ContentUri(path = "recipes", type = "vnd.android.cursor.dir/recipes", defaultSort = RecipeColumns._ID + " ASC")
    public static final Uri RECIPES = Uri.parse("content://" + AUTHORITY + "/recipes");
  }

}

