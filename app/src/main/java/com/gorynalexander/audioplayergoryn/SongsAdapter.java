package com.gorynalexander.audioplayergoryn;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import java.net.URI;
import java.util.List;
import java.util.jar.Pack200;

/**
 * Created by Odinn on 08.09.2016.
 */
public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongListHolder> {
    private List<Song> songList;
    private MediaPlayer mediaPlayer;
    private SeekBar lastSeek;

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

            }
        });
        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mediaPlayer.seekTo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
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
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(context, uri);
            mediaPlayer.start();
            h.seekBar.setVisibility(View.VISIBLE);
            h.seekBar.setMax(mediaPlayer.getDuration());
            lastSeek = h.seekBar;
        } else {
            if (lastSeek.getMax() == h.seekBar.getMax()){
                mediaPlayer.stop();
                h.seekBar.setVisibility(View.INVISIBLE);
                mediaPlayer.release();
                mediaPlayer = null;
            } else {

                lastSeek.setVisibility(View.INVISIBLE);
                h.seekBar.setVisibility(View.VISIBLE);
                mediaPlayer.stop();
                lastSeek = h.seekBar;
                mediaPlayer = MediaPlayer.create(context, uri);
                mediaPlayer.start();
            }
        }
    }
}
