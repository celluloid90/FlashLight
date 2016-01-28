package com.gpss.flashlight;

import java.util.List;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class GPSSFlashlight extends Activity {

	private ImageButton onOffButton;
	private String mFlashMode;
	private boolean mIsFlashOn;
	private Camera mCamera = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Log.d("GPSSFlashlight", "Flashlight is" + (mIsFlashOn ? " " : " not ")
				+ "on");
		this.onOffButton = (ImageButton) this.findViewById(R.id.onoff);
		setupButton();
	}

	private void setupButton() {
		ImageButton button = this.onOffButton;
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mIsFlashOn) {
					mIsFlashOn = turnFlashOff();
					v.setKeepScreenOn(false);
				} else {
					mIsFlashOn = turnFlashOn();
					v.setKeepScreenOn(true);
				}
			}
		});
	}

	private boolean turnFlashOff() {
		this.onOffButton.setImageResource(R.drawable.ic_power_on);
		mCamera.stopPreview();
		Parameters p = mCamera.getParameters();
		p.setFlashMode(mFlashMode);
		mCamera.setParameters(p);
		Log.d("GPSSFlashlight", "Turned flash off");
		return false;
	}

	private boolean turnFlashOn() {
		this.onOffButton.setImageResource(R.drawable.ic_power_off);
		Parameters p = mCamera.getParameters();
		List<String> flashModes = p.getSupportedFlashModes();
		if (flashModes.contains(Parameters.FLASH_MODE_TORCH)) {
			mFlashMode = p.getFlashMode();
			p.setFlashMode(Parameters.FLASH_MODE_TORCH);
			mCamera.setParameters(p);
			mCamera.startPreview();
			return true;
		}
		return false;
	}

	@Override
	protected void onPause() {
		super.onPause();
		mCamera.release();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mCamera = Camera.open();
	}
}