/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.recorder;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.example.util.Constants;
import com.example.util.FileUtils;

public class Recorder implements OnCompletionListener, OnErrorListener
{
	public static final int IDLE_STATE = 0;
	public static final int RECORDING_STATE = 1;
	public static final int PLAYING_STATE = 2;

	int mState = IDLE_STATE;

	public static final int NO_ERROR = 0;
	public static final int SDCARD_ACCESS_ERROR = 1;
	public static final int INTERNAL_ERROR = 2;
	public static final int IN_CALL_RECORD_ERROR = 3;
	private static final String TAG = Recorder.class.getSimpleName();

	public interface OnStateChangedListener
	{
		public void onStateChanged(int state);

		public void onError(int error);
	}

	OnStateChangedListener mOnStateChangedListener = null;

	long mSampleStart = 0; // time at which latest record or play operation
							// started
	int mSampleLength = 0; // length of current sample

	MediaRecorder mRecorder = null;
	MediaPlayer mPlayer = null;

	public Recorder()
	{
	}

	public int getMaxAmplitude()
	{
		if (mState != RECORDING_STATE)
			return 0;
		return mRecorder.getMaxAmplitude();
	}

	public void setOnStateChangedListener(OnStateChangedListener listener)
	{
		mOnStateChangedListener = listener;
	}

	public int state()
	{
		return mState;
	}

	public int progress()
	{
		if (mState == RECORDING_STATE || mState == PLAYING_STATE)
			return (int) ((System.currentTimeMillis() - mSampleStart) / 1000);
		return 0;
	}

	public int sampleLength()
	{
		return mSampleLength;
	}

	/**
	 * Resets the recorder state. If a sample was recorded, the file is left on
	 * disk and will be reused for a new recording.
	 */
	public void clear()
	{
		stop();

		mSampleLength = 0;

		signalStateChanged(IDLE_STATE);
	}

	
	File targetFile;
	
	public void startRecording(String fileName,int outputfileformat, String extension,
			Context context)
	{
		stop();
		
		File dir = new File(Environment.getExternalStorageDirectory(),Constants.tempDir);
		if (!dir.exists())
			dir.mkdirs();
		targetFile = new File(dir,fileName+extension);
		
		if (!targetFile.exists()){
			try{
				targetFile.createNewFile();	
			} catch (IOException e){
				e.printStackTrace();
				setError(SDCARD_ACCESS_ERROR);
				return;
			}
		}

		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(outputfileformat);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setOutputFile(targetFile.getAbsolutePath());

		// Handle IOException
		try
		{
			mRecorder.prepare();
		} catch (IOException e)
		{
			e.printStackTrace();
			setError(INTERNAL_ERROR);
			mRecorder.reset();
			mRecorder.release();
			mRecorder = null;
			return;
		}
		// Handle RuntimeException if the recording couldn't start
		try
		{
			mRecorder.start();
		} catch (RuntimeException exception)
		{
			exception.printStackTrace();
			AudioManager audioMngr = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			boolean isInCall = ((audioMngr.getMode() == AudioManager.MODE_IN_CALL) || (audioMngr
					.getMode() == AudioManager.MODE_CURRENT));
			if (isInCall)
			{
				setError(IN_CALL_RECORD_ERROR);
			} else
			{
				setError(INTERNAL_ERROR);
			}
			mRecorder.reset();
			mRecorder.release();
			mRecorder = null;
			return;
		}
		mSampleStart = System.currentTimeMillis();
		setState(RECORDING_STATE);
	}

	public void stopRecording()
	{
		if (mRecorder == null)
			return;

		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;

		mSampleLength = (int) ((System.currentTimeMillis() - mSampleStart) / 1000);
		setState(IDLE_STATE);
	}

	public void startPlayback()
	{
		stop();

		mPlayer = new MediaPlayer();
		try
		{
			mPlayer.setDataSource(targetFile.getAbsolutePath());
			mPlayer.setOnCompletionListener(this);
			mPlayer.setOnErrorListener(this);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			setError(INTERNAL_ERROR);
			mPlayer = null;
			return;
		} catch (IOException e)
		{
			e.printStackTrace();
			setError(SDCARD_ACCESS_ERROR);
			mPlayer = null;
			return;
		}

		mSampleStart = System.currentTimeMillis();
		setState(PLAYING_STATE);
	}

	public void stopPlayback()
	{
		if (mPlayer == null) // we were not in playback
			return;

		mPlayer.stop();
		mPlayer.release();
		mPlayer = null;
		setState(IDLE_STATE);
	}

	public void stop()
	{
		stopRecording();
		stopPlayback();
	}

	public boolean onError(MediaPlayer mp, int what, int extra)
	{
		stop();
		setError(SDCARD_ACCESS_ERROR);
		return true;
	}

	public void onCompletion(MediaPlayer mp)
	{
		stop();
	}

	private void setState(int state)
	{
		if (state == mState)
			return;

		mState = state;
		signalStateChanged(mState);
	}

	private void signalStateChanged(int state)
	{
		if (mOnStateChangedListener != null)
			mOnStateChangedListener.onStateChanged(state);
	}

	private void setError(int error)
	{
		if (mOnStateChangedListener != null)
			mOnStateChangedListener.onError(error);
	}

	public void delete() {
		// TODO Auto-generated method stub
		if(targetFile!=null&&targetFile.exists()){
			Log.i(TAG, "delete:"+targetFile.getAbsolutePath());
			FileUtils.deleteFile(targetFile);
		}
	}

	public File getFile() {
		return targetFile;
	}

	public void startPlayback(File file)
	{
		stop();

		mPlayer = new MediaPlayer();
		try
		{
			mPlayer.setDataSource(file.getAbsolutePath());
			mPlayer.setOnCompletionListener(this);
			mPlayer.setOnErrorListener(this);
			mPlayer.prepare();
			mPlayer.start();
		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			setError(INTERNAL_ERROR);
			mPlayer = null;
			return;
		} catch (IOException e)
		{
			e.printStackTrace();
			setError(SDCARD_ACCESS_ERROR);
			mPlayer = null;
			return;
		}

		mSampleStart = System.currentTimeMillis();
		setState(PLAYING_STATE);
	}
}
