package com.pobla.baking.ui.recipe.view;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.pobla.baking.R;
import com.pobla.baking.data.storage.db.StepColumns;
import com.pobla.baking.ui.recipe.RecipeStepsListActivity;
import com.pobla.baking.ui.recipe.StepDetailActivity;
import com.pobla.baking.ui.recipe.presenter.DefaultStepDetailPresenter;
import com.pobla.baking.ui.recipe.presenter.StepDetail;
import com.pobla.baking.ui.recipe.presenter.StepDetailPresenter;

import net.simonvt.schematic.Cursors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeStepsListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment implements StepDetailView {

  public static final String RECIPE_ID = "RECIPE_ID";
  public static final String STEP_ID = "STEP_ID";

  public static StepDetailFragment newInstance(int recipeId, Integer stepId) {
    StepDetailFragment fragment = new StepDetailFragment();
    Bundle bundle = new Bundle();
    bundle.putInt(StepDetailFragment.RECIPE_ID, recipeId);
    bundle.putInt(StepDetailFragment.STEP_ID, stepId);
    fragment.setArguments(bundle);
    return fragment;
  }

  private StepDetailPresenter presenter;

  @BindView(R.id.recipe_detail_description)
  TextView textViewDescription;
  @BindView(R.id.recipe_detail_button_next)
  FloatingActionButton fabNext;
  @BindView(R.id.recipe_detail_button_back)
  FloatingActionButton fabBack;
  @BindView(R.id.recipe_detail_video_player)
  SimpleExoPlayerView videoPlayerView;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.presenter = new DefaultStepDetailPresenter(this, getActivity().getContentResolver());

    if (getArguments().containsKey(RECIPE_ID)) {
      presenter.setModel(new StepDetail(getArguments().getInt(STEP_ID), getArguments().getInt(RECIPE_ID)));
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    presenter.retrieveDetails();
  }

  @Override
  public void bindView(Cursor cursor) {
    if (cursor != null && cursor.moveToNext()) {
      getActivity().setTitle(Cursors.getString(cursor, StepColumns.SHORT_DESCRIPTION));
      textViewDescription.setText(Cursors.getString(cursor, StepColumns.DESCRIPTION));
      //TODO fix view when video is gone (description appears half way there
      //TODO show media player full screen when phone horizontal
      //TODO fix vide doesn't show if controllers are in the screen
      String videoUrl = Cursors.getString(cursor, StepColumns.VIDEO_URL);
      if(!TextUtils.isEmpty(videoUrl)){
        videoPlayerView.setVisibility(View.VISIBLE);
        play(Uri.parse(videoUrl));
      }else{
        stopPlayer();
        videoPlayerView.setVisibility(View.GONE);
      }
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    stopPlayer();
  }

  @OnClick(R.id.recipe_detail_button_back)
  public void previousStep() {
    presenter.showPreviousStep();
  }

  @OnClick(R.id.recipe_detail_button_next)
  public void nextStep() {
    presenter.showNextStep();
  }

  @Override
  public void showBack(boolean show) {
    fabBack.setVisibility(show ? View.VISIBLE : View.GONE);

  }

  @Override
  public void showNext(boolean show) {
    fabNext.setVisibility(show ? View.VISIBLE : View.GONE);
  }

  private void play(Uri mediaUri) {
    initilisePlayerIfRequired();

    String userAgent = Util.getUserAgent(this.getContext(), getString(R.string.app_name));
    MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(this.getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
    SimpleExoPlayer player = videoPlayerView.getPlayer();
    player.prepare(mediaSource);
    player.setPlayWhenReady(true);
  }

  private void initilisePlayerIfRequired() {
    if (videoPlayerView.getPlayer() == null) {
      TrackSelector trackSelector = new DefaultTrackSelector();
      LoadControl loadControl = new DefaultLoadControl();
      SimpleExoPlayer videoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector, loadControl);
      videoPlayerView.setPlayer(videoPlayer);
    }
  }


  private void stopPlayer() {
    SimpleExoPlayer player = videoPlayerView.getPlayer();
    if(player != null){
      player.stop();
    }
  }

}
