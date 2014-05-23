package com.example.myfucntest.recorder;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.util.Log;

import com.example.myfucntest.R;

public class AudioHelper implements AudioAction,Recorder.OnStateChangedListener,SoundPool.OnLoadCompleteListener{

	private Context context;
    private static final String TAG = "AudioRecordTest";
    private String mFileName = null;
    private boolean isRecording = false;
	
	public boolean isRecording() {
		return isRecording;
	}

	public void setRecording(boolean isRecording) {
		this.isRecording = isRecording;
	}

	boolean isPlaying = false;

	public boolean isPlaying() {
		return isPlaying;
	}

	public void setPlaying(boolean isPlaying) {
		this.isPlaying = isPlaying;
	}
	
	int [] sounds = new int[]{R.raw.office};
	SoundPool pool;

	private AudioHelper(Context context) {
		super();
		this.context = context;
		mRecorder = new Recorder();
		mRecorder.setOnStateChangedListener(this);
		pool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		pool.setOnLoadCompleteListener(this);
	}
	
	public static AudioHelper instance = null;
	
	public static AudioHelper getInstance(Context context){
		if(instance==null){
			instance = new AudioHelper(context);
		}
		return instance;
	}
	
	
	Recorder mRecorder;
	
	public interface OnRecorderListener{
		public void onRecording(int maxAmplitude);
	}
	
	class RecordingListenerThread extends Thread{
		
		@Override
		public void run() {
			while(isRecording&&mRecordingListener!=null){
				try {

					int maxAmplitude = mRecorder.getMaxAmplitude();
					
					Log.i(TAG,"maxAmplitude:"+maxAmplitude);
					
					mRecordingListener.onRecording(maxAmplitude);
					
					Thread.sleep(100);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mRecordingListener = null;
		}
	}
	
	OnRecorderListener mRecordingListener;
	
	@Override
	public void startRecoding(String fileName,OnRecorderListener recorderListener) {
		// TODO Auto-generated method stub
		this.mRecordingListener = recorderListener;
		this.mFileName = fileName;
		
		Log.i(TAG, mFileName);
		
		stopAudioPlayback();
		mRecorder.startRecording(fileName,MediaRecorder.OutputFormat.AMR_NB,".amr", context);
		
		isRecording = true;
		
		new RecordingListenerThread().start();
	}

	@Override
	public void stop() {
		mRecorder.stop();
		isRecording = false;
		isPlaying = false;
	}

	@Override
	public void startPlaying() {
		// TODO Auto-generated method stub
		mRecorder.startPlayback();
	}
	
	public interface onStateChangedListener{
		public void onPlaying();
		public void onCompleted();
		public void onError();
	}
	
	onStateChangedListener listener;
	
	public void setListener(onStateChangedListener listener) {
		this.listener = listener;
	}

	@Override
	public File getVoiceFile() {
		return mRecorder.getFile();
	}

	@Override
	public void onStateChanged(int state) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStateChanged:"+state);
		if(listener!=null){
			if(state==Recorder.PLAYING_STATE){
				listener.onPlaying();
			}else if(state==Recorder.IDLE_STATE){
				listener.onCompleted();
			}
		}
	}

	@Override
	public void onError(int error) {
		Log.e(TAG, "onError:"+error);
		if(listener!=null){
			listener.onError();
		}
	}
	
	private void stopAudioPlayback()
	{
		// Shamelessly copied from MediaPlaybackService.java, which
		// should be public, but isn't.
		Intent i = new Intent("com.android.music.musicservicecommand");
		i.putExtra("command", "pause");
		context.sendBroadcast(i);
	}

	public void delete() {
		mRecorder.delete();
	}

	public int getState() {
		return mRecorder.mState;
	}

	public void playFile(File file) {
		// TODO Auto-generated method stub
		if(file!=null&&file.exists()){
			mRecorder.startPlayback(file);
		}else{
			Log.e(TAG, "播放失败：文件不存在");
		}
	}
	
	public void playOfficeSound(){
		pool.load(context, sounds[0], 1);
	}

	private int loop = 0;
	@Override
	public void onLoadComplete(SoundPool pool, int sampleId, int status){
		pool.play(sampleId, 0.8f, 0.8f, 16, loop, 1.0f);
	}


	
}
