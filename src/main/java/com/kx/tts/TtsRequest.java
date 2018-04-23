package com.kx.tts;

public class TtsRequest {
    private String encode_type;
    private String voice_name;
    private int volume;
    private int sample_rate;
    private int speech_rate;
    private int pitch_rate;
    private int tts_nus;
    private int background_music_id;
    private int background_music_offset;
    private int background_music_volume;
    public String getEncodeType() {
        return encode_type;
    }
    public void setEncodeType(String encode_type) {
        this.encode_type = encode_type;
    }
    public String getVoiceName() {
        return voice_name;
    }
    public void setVoiceName(String voice_name) {
        this.voice_name = voice_name;
    }
    public int getVolume() {
        return volume;
    }
    public void setVolume(int volume) {
        this.volume = volume;
    }
    public int getSampleRate() {
        return sample_rate;
    }
    public void setSampleRate(int sample_rate) {
        this.sample_rate = sample_rate;
    }
    public int getSpeechRate() {
        return speech_rate;
    }
    public void setSpeechRate(int speech_rate) {
        this.speech_rate = speech_rate;
    }
    public int getPitchRate() {
        return pitch_rate;
    }
    public void setPitchRate(int pitch_rate) {
        this.pitch_rate = pitch_rate;
    }
    public int getTtsNus() {
        return tts_nus;
    }
    public void setTtsNus(int tts_nus) {
        this.tts_nus = tts_nus;
    }
    public int getBackgroundMusicId() {
        return background_music_id;
    }
    public void setBackgroundMusicId(int background_music_id) {
        this.background_music_id = background_music_id;
    }
    public int getBackgroundMusicOffset() {
        return background_music_offset;
    }
    public void setBackgroundMusicOffset(int background_music_offset) {
        this.background_music_offset = background_music_offset;
    }
    public int getBackgroundMusicVolume() {
        return background_music_volume;
    }
    public void setBackgroundMusicVolume(int background_music_volume) {
        this.background_music_volume = background_music_volume;
    }
}
