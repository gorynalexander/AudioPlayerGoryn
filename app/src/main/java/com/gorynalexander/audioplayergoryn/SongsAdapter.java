package com.gorynalexander.audioplayergoryn;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public SongsAdapter(List<Song> songList){
        this.songList = songList;
    }

    @Override
    public SongListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false);
        return new SongListHolder(view);
    }

    @Override
    public void onBindViewHolder(SongListHolder holder, int position) {
        final Song song = songList.get(position);
        holder.tvSongName.setText(song.getSongName());
        holder.tvSongTime.setText(song.getSongTime());
        holder.tvAuthor.setText(song.getAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                } else {

                    mediaPlayer = MediaPlayer.create(view.getContext(), Uri.parse(song.getSongURI()));
                    mediaPlayer.start();
                }

            }
        });


    }

    private void stopPlaying() {
        if (mediaPlayer != null) {

        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class SongListHolder extends RecyclerView.ViewHolder {
        TextView tvAuthor;
        TextView tvSongName;
        TextView tvSongTime;

        public SongListHolder(View itemView) {
            super(itemView);
            tvAuthor = (TextView) itemView.findViewById(R.id.tvAuthor);
            tvSongName = (TextView) itemView.findViewById(R.id.tvSongName);
            tvSongTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
}
