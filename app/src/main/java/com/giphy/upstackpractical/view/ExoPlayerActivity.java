package com.giphy.upstackpractical.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.widget.Button;
import android.widget.TextView;

import com.giphy.upstackpractical.R;
import com.giphy.upstackpractical.model.Vote;
import com.giphy.upstackpractical.model.Vote_;
import com.giphy.upstackpractical.presenter.BaseApplication;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import io.objectbox.BoxStore;



/*
Created by: Ayal Fieldust
Date: 8/2017
Description:
This Example app was created to show a simple example of ExoPlayer Version 2.5.1.
There is an option to play mp4 files or live stream content.
Exoplayer provides options to play many different formats, so the code can easily be tweaked to play the requested format.
scroll down to "ADJUST HERE:" I & II to change between sources.
 */

public class ExoPlayerActivity extends AppCompatActivity implements VideoRendererEventListener {


    private static final String TAG = "ExoPlayerActivity";
    private SimpleExoPlayer player;

    @BindView(R.id.resolution_textView)
    TextView resolutionTextView;
    @BindView(R.id.player_view)
    SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.title_textView)
    TextView txtTitle;
    @BindView(R.id.button_up)
    Button btnUp;
    @BindView(R.id.button_down)
    Button btnDown;

    String urlMp4;
    private Box<Vote> notesBox;
    String myID = null;
    Vote votes = null;
    int up = 0;
    int down = 0;
    int latestUp = 0;
    int latestDown = 0;
    MediaSource mediaSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_screen);
        ButterKnife.bind(this);

        fetchBundle();
        initObjectBox();
        setPlayer();
        setPlayerListener();
        player.setPlayWhenReady(true); //run file/link when ready to play.
        player.setVideoDebugListener(this); //for listening to resolution change and  outputing the resolution
    }//End of onCreate

    private void setPlayerListener() {
        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                Log.v(TAG, "Listener-onTimelineChanged...");
            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.v(TAG, "Listener-onTracksChanged...");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {
                Log.v(TAG, "Listener-onLoadingChanged...isLoading:" + isLoading);
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.v(TAG, "Listener-onPlayerStateChanged..." + playbackState);
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {
                Log.v(TAG, "Listener-onRepeatModeChanged...");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.v(TAG, "Listener-onPlayerError...");
                player.stop();
                player.prepare(mediaSource);
                player.setPlayWhenReady(true);
            }

            @Override
            public void onPositionDiscontinuity() {
                Log.v(TAG, "Listener-onPositionDiscontinuity...");
            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
                Log.v(TAG, "Listener-onPlaybackParametersChanged...");
            }
        });
    }

    private void setPlayer() {


// 1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

// 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
//Set media controller
        simpleExoPlayerView.setUseController(true);
        simpleExoPlayerView.requestFocus();

// Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);
//Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
//Produces DataSource instances through which media data is loaded.
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "exoplayer2example"), bandwidthMeterA);
//Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

         mediaSource = new ExtractorMediaSource(Uri.parse(urlMp4),
                dataSourceFactory, extractorsFactory, null, null);
// Prepare the player with the source.
        player.prepare(mediaSource);


    }

    private void initObjectBox() {
        BoxStore boxStore = ((BaseApplication) getApplication()).getBoxStore();

        notesBox = boxStore.boxFor(Vote.class);
        votes = notesBox.query().equal(Vote_.idVideo, myID).build().findFirst();
        if (votes != null) {
            up = Integer.parseInt(votes.getUpVote());
            down = Integer.parseInt(votes.getDownVote());
            latestDown=down;
            latestUp=up;
        }
        setVotes(up,down);
    }

    private void setVotes(int up, int down) {
        btnDown.setText("Down ("+down+")");
        btnUp.setText("Up ("+up+")");
    }

    private void fetchBundle() {
        Bundle extras = getIntent().getExtras();
        String title = null;
        if (extras != null) {
            urlMp4 = extras.getString("url"); // retrieve the data using keyName
            myID = extras.getString("id");
            title = extras.getString("title");
        }
        txtTitle.setText(title);
    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.v(TAG, "onVideoSizeChanged [" + " width: " + width + " height: " + height + "]");
        resolutionTextView.setText("RES:(WxH):" + width + "X" + height + "\n           " + height + "p");
    }

    @Override
    public void onRenderedFirstFrame(Surface surface) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }


//-------------------------------------------------------ANDROID LIFECYCLE---------------------------------------------------------------------------------------------

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG, "onStop()...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG, "onStart()...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume()...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG, "onPause()...");
    }

    @OnClick(R.id.button_down)
    public void submitDown() {

        if (votes != null) {
            latestDown = Integer.parseInt(votes.getDownVote()) + 1;
            votes.setDownVote(String.valueOf(latestDown));
            notesBox.put(votes);
        } else {
            latestDown = 1;
            Vote vt = new Vote();
            vt.setDownVote(String.valueOf(latestDown));
            vt.setUpVote(String.valueOf(latestUp));
            vt.setId(myID);
            notesBox.put(vt);
            votes = vt;
        }
        setVotes(latestUp,latestDown);

    }


    @OnClick(R.id.button_up)
    public void submitUp() {
        if (votes != null) {
            latestUp = Integer.parseInt(votes.getUpVote()) + 1;
            votes.setUpVote(String.valueOf(latestUp));
            notesBox.put(votes);
        } else {
            latestUp = 1;
            Vote vt = new Vote();
            vt.setDownVote(String.valueOf(latestDown));
            vt.setUpVote(String.valueOf(latestUp));
            vt.setId(myID);
            notesBox.put(vt);
            votes = vt;
        }
        setVotes(latestUp,latestDown);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (down != latestDown || up != latestUp) {
            Intent intnet = new Intent("fav.USER_ACTION");
            sendBroadcast(intnet);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}
