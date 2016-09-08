package com.gorynalexander.audioplayergoryn;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvSong = (RecyclerView) findViewById(R.id.rView);
        rvSong.setLayoutManager(new LinearLayoutManager(this));
        SongsAdapter songsAdapter = new SongsAdapter(uploadSongs());
        rvSong.setAdapter(songsAdapter);
    }

    private List<Song> uploadSongs() {
        String[] projection = {
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";


        Cursor cursor = getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);
        List<Song> song = new ArrayList<>();
        while (cursor.moveToNext()) {
          //  song.add(new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            song.add(new Song(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));

        }

        return song;
    }
}
