package com.example.elijah.kusoma.eventbus;

public class SettingsChangeEvent {

    public final float mPitch;
    public final float mSpeed;

    public SettingsChangeEvent(float pitch, float speed) {
        mPitch = pitch;
        mSpeed = speed;
    }

}
