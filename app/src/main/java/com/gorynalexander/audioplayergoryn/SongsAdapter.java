package com.gorynalexander.audioplayergoryn;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Odinn on 08.09.2016.
 */
public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongListHolder> {
    private List<Song> songList;
    private Thread updateSeekBarThread;

    private MediaPlayer mediaPlayer;
    private SeekBar lastSeek;
    private SeekBar seekBar;


    private Handler handler = new Handler();


    public SongsAdapter(List<Song> songList) {
        this.songList = songList;
    }

    @Override
    public SongListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false);
        return new SongListHolder(view);
    }

    @Override
    public void onBindViewHolder(final SongListHolder holder, final int position) {
        final Song song = songList.get(position);
        holder.tvSongName.setText(song.getSongName());
        holder.tvSongTime.setText(transferToMinSecs(Integer.parseInt(song.getSongTime())));
        holder.tvAuthor.setText(song.getAuthor());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(song.getSongURI());
                checkMediaPlayer(view.getContext(), uri, holder );
                seekBar = holder.seekBar;



            }
        });
        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //TODO: if (b==user)
                if (b){
                    mediaPlayer.seekTo(i);
                }
               //
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }



    @Override
    public int getItemCount() {
        return songList.size();
    }


    public class SongListHolder extends RecyclerView.ViewHolder implements MediaController.MediaPlayerControl {
        TextView tvAuthor;
        TextView tvSongName;
        TextView tvSongTime;
        SeekBar seekBar;


        public SongListHolder(View itemView) {
            super(itemView);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvSongName = (TextView) itemView.findViewById(R.id.tvSongName);
            tvSongTime = (TextView) itemView.findViewById(R.id.tvTime);
            seekBar = (SeekBar) itemView.findViewById(R.id.seekBar);
        }

        @Override
        public void start() {
            mediaPlayer.start();
        }

        @Override
        public void pause() {
            seekBar.setVisibility(View.INVISIBLE);
            mediaPlayer.pause();
        }

        @Override
        public int getDuration() {
            return mediaPlayer.getDuration();
        }

        @Override
        public int getCurrentPosition() {
            return mediaPlayer.getCurrentPosition();
        }

        @Override
        public void seekTo(int i) {
            mediaPlayer.seekTo(i);
        }

        @Override
        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }

        @Override
        public int getBufferPercentage() {
            return 0;
        }

        @Override
        public boolean canPause() {
            return true;
        }

        @Override
        public boolean canSeekBackward() {
            return true;
        }

        @Override
        public boolean canSeekForward() {
            return true;
        }

        @Override
        public int getAudioSessionId() {
            return mediaPlayer.getAudioSessionId();
        }
    }



    private String transferToMinSecs(int durationTime) {
        int durationSecs = durationTime / 1000;
        int resultMins = durationSecs / 60;
        int resultSecs = durationSecs % 60;
        String timeString = String.format("%02d:%02d", resultMins, resultSecs);
        return timeString;
        //   return resultMins + ":"+ resultSecs;
    }

    private void checkMediaPlayer(Context context, Uri uri, SongListHolder h){
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, uri);

            mediaPlayer.start();
            h.seekBar.setVisibility(View.VISIBLE);
            h.seekBar.setMax(mediaPlayer.getDuration());
            lastSeek = h.seekBar;
            updateProgressBar();




        } else {
            if (lastSeek.getMax() == h.seekBar.getMax()) {
                mediaPlayer.stop();
                //  mHandler.removeCallbacks(mUpdateTimeTask);
                h.seekBar.setProgress(0);
                h.seekBar.setVisibility(View.INVISIBLE);
                mediaPlayer.release();
                mediaPlayer = null;

            } else {

                lastSeek.setVisibility(View.INVISIBLE);
                h.seekBar.setVisibility(View.VISIBLE);
                mediaPlayer.stop();
                //     mHandler.removeCallbacks(mUpdateTimeTask);
                lastSeek.setProgress(0);
                h.seekBar.setProgress(0);
                lastSeek = h.seekBar;
                mediaPlayer = null;
                mediaPlayer = MediaPlayer.create(context, uri);
                mediaPlayer.start();
                updateProgressBar();
            }

        }

    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (mediaPlayer != null) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 100);
            }

        }
    };
    private void updateProgressBar() {
        // TODO Auto-generated method stub

        handler.postDelayed(mUpdateTimeTask, 100);
    }

}
