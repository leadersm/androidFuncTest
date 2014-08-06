package com.example.recorder;

import java.io.File;

import com.example.recorder.AudioHelper.OnRecorderListener;

public interface AudioAction {

	public void startRecoding(String fileName,OnRecorderListener recorderListener);
	public void startPlaying();
	public void stop();
	public File getVoiceFile();
	
}
