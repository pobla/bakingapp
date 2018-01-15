package com.pobla.baking.ui.recipe.view;

import android.content.res.Configuration;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

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
  private static final String RESUME_WINDOW = "RESUME_WINDOW";
  private static final String RESUME_POSITION = "RESUME_POSITION";
  private static final String MODEL = "MODEL";
  private static final String PLAYSTATE = "playstate";

  private int resumeWindow = C.INDEX_UNSET;
  private long resumePosition = C.INDEX_UNSET;
  private boolean isPlayWhenReady = true;
  private Uri mediaUri;

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
  @BindView(R.id.recipe_detail_image)
  ImageView detailImage;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.presenter = new DefaultStepDetailPresenter(this, getActivity().getContentResolver());

    StepDetail model = null;
    if (getArguments().containsKey(RECIPE_ID)) {
      model = new StepDetail(getArguments().getInt(STEP_ID), getArguments().getInt(RECIPE_ID));
    }
    if (savedInstanceState != null && savedInstanceState.containsKey(RESUME_WINDOW)) {
      resumeWindow = savedInstanceState.getInt(RESUME_WINDOW, C.INDEX_UNSET);
      resumePosition = savedInstanceState.getLong(RESUME_POSITION, C.INDEX_UNSET);
      isPlayWhenReady = savedInstanceState.getBoolean(PLAYSTATE, true);

    }
    model = (savedInstanceState != null && savedInstanceState.containsKey(MODEL)) ? (StepDetail) savedInstanceState.getParcelable(MODEL) : model;
    presenter.setModel(model);
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
      showThumbnail(cursor);
      showVideo(cursor);
    }
  }

  private void showThumbnail(Cursor cursor) {
    String thumbnail = Cursors.getString(cursor, StepColumns.THUMBNAIL_URL);
    RequestCreator requestCreator = loadImage(thumbnail);
    requestCreator.placeholder(R.drawable.recipe_placeholder)
      .error(R.drawable.recipe_placeholder)
      .into(detailImage);
  }

  private RequestCreator loadImage(String thumbnail) {
    Picasso picasso = Picasso.with(getContext());
    return !TextUtils.isEmpty(thumbnail) ? picasso.load(thumbnail) : picasso.load(R.drawable.recipe_placeholder);
  }

  private void showVideo(Cursor cursor) {
    String videoUrl = Cursors.getString(cursor, StepColumns.VIDEO_URL);
    showVideoIfExists(videoUrl);
  }

  private void showVideoIfExists(String videoUrl) {
    if (!TextUtils.isEmpty(videoUrl)) {
      detailImage.setVisibility(View.GONE);
      videoPlayerView.setVisibility(View.VISIBLE);
      this.mediaUri = Uri.parse(videoUrl);
      play();
      textViewDescription.setVisibility(isVideoFullScreen() ? View.GONE : View.VISIBLE);
    } else {
      stopPlayer();
      videoPlayerView.setVisibility(View.GONE);
      detailImage.setVisibility(isVideoFullScreen() ? View.GONE : View.VISIBLE);
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (mediaUri != null)
      play();
  }

  @Override
  public void onPause() {
    super.onPause();
    stopPlayer();
  }

  @OnClick(R.id.recipe_detail_button_back)
  public void previousStep() {
    resetVideo();
    presenter.showPreviousStep();
  }

  private void resetVideo() {
    resumeWindow = C.INDEX_UNSET;
    resumePosition = C.INDEX_UNSET;
    isPlayWhenReady = true;
  }

  @OnClick(R.id.recipe_detail_button_next)
  public void nextStep() {
    resetVideo();
    presenter.showNextStep();
  }

  @Override
  public void showBack(boolean show) {
    fabBack.setVisibility(!isVideoFullScreen() && show ? View.VISIBLE : View.GONE);
  }

  @Override
  public void showNext(boolean show) {
    fabNext.setVisibility(!isVideoFullScreen() && show ? View.VISIBLE : View.GONE);
  }

  private void play() {
    initPlayerIfRequired();

    String userAgent = Util.getUserAgent(this.getContext(), getString(R.string.app_name));
    MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(this.getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
    SimpleExoPlayer player = videoPlayerView.getPlayer();
    if (resumeWindow != C.INDEX_UNSET) {
      player.seekTo(resumeWindow, resumePosition);
    }
    player.prepare(mediaSource);
    player.setPlayWhenReady(isPlayWhenReady);
  }

  private void initPlayerIfRequired() {
    if (videoPlayerView.getPlayer() == null) {
      TrackSelector trackSelector = new DefaultTrackSelector();
      SimpleExoPlayer videoPlayer = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector);
      videoPlayerView.setPlayer(videoPlayer);
    }
  }


  private void stopPlayer() {
    SimpleExoPlayer player = videoPlayerView.getPlayer();
    if (player != null) {
      resumeWindow = player.getCurrentWindowIndex();
      resumePosition = Math.max(0, player.getContentPosition());
      isPlayWhenReady = player.getPlayWhenReady();
      player.stop();
    }
  }

  public boolean isVideoFullScreen() {
    int display_mode = getResources().getConfiguration().orientation;
    boolean isTablet = getResources().getBoolean(R.bool.isTablet);
    return !isTablet && display_mode == Configuration.ORIENTATION_LANDSCAPE;
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(RESUME_WINDOW, resumeWindow);
    outState.putLong(RESUME_POSITION, resumePosition);
    outState.putBoolean(PLAYSTATE, isPlayWhenReady);
    outState.putParcelable(MODEL, presenter.getModel());
  }
}
