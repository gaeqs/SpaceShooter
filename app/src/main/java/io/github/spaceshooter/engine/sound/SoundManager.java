package io.github.spaceshooter.engine.sound;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {

    public static final int MAX_STREAMS = 10;
    public static final float DEFAULT_MUSIC_VOLUME = 0.6f;

    private final Context context;
    private final SoundPool pool;
    private MediaPlayer musicPlayer;

    private final Map<String, Integer> registeredSounds = new HashMap<>();

    public SoundManager(Context context) {
        this.context = context;

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        pool = new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .setMaxStreams(MAX_STREAMS)
                .build();
    }

    public boolean registerSound(String key, String file) {
        if (registeredSounds.containsKey(key)) return false;
        try {
            AssetFileDescriptor descriptor = context.getAssets().openFd("sfx/" + file);
            registeredSounds.put(key, pool.load(descriptor, 1));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void play(String sound) {
        Integer id = registeredSounds.get(sound);
        if (id != null) {
            pool.play(id, 1.0f, 1.0f, 0, 0, 1.0f);
        }
    }

    public void playMusic(String file) {
        if (musicPlayer != null) {
            musicPlayer.stop();
        } else {
            musicPlayer = new MediaPlayer();
        }
        try {

            AssetFileDescriptor afd = context.getAssets().openFd("sfx/" + file);
            musicPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();

            musicPlayer.setLooping(true);
            musicPlayer.setVolume(DEFAULT_MUSIC_VOLUME, DEFAULT_MUSIC_VOLUME);
            musicPlayer.prepare();
            musicPlayer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void dispose() {
        pool.release();
        musicPlayer.release();
        registeredSounds.clear();
    }

}
