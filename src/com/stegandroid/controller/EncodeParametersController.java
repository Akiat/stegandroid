package com.stegandroid.controller;

import android.content.Context;

import com.stegandroid.R;
import com.stegandroid.parameters.EncodeParameters;

public class EncodeParametersController {

	//Private attributes
	private Context _context;
	private String _errorMessage;
	
	// TODO: Complete the control of data if necessary [ IT IS NECESSARY ;) ]
	public EncodeParametersController(Context context) {
		_context = context;
		_errorMessage = "";
	}

	public Boolean controlAllData(EncodeParameters parameters) {
		return controlSrcVideoPath(parameters) 
				&& controlDestVideoPath(parameters)
				&& controlTextToHide(parameters)
				&& controlFileToHide(parameters);
	}

	private Boolean controlSrcVideoPath(EncodeParameters parameters) {
		if (parameters.getSrcVideoPath() == null || parameters.getSrcVideoPath().isEmpty()) {
			setErrorMessage(R.string.error_src_video_path_empty_string);
			return false;
		}
		return true;
	}
	
	private Boolean controlDestVideoPath(EncodeParameters parameters) {
		if (parameters.getDestVideoPath() == null || parameters.getDestVideoPath().isEmpty()) {
			setErrorMessage(R.string.error_dest_video_empty_string);
			return false;
		}
		return true;
	}
	
	private Boolean controlTextToHide(EncodeParameters parameters) {
		if (parameters.isUsingHideText()) {
			if (parameters.getTextToHide() == null || parameters.getTextToHide().isEmpty()) {
				setErrorMessage(R.string.error_text_to_hide_empty_string);
				return false;
			}
		}
		return true;
	}
	
	private Boolean controlFileToHide(EncodeParameters parameters) {
		if (!parameters.isUsingHideText()) {
			if (parameters.getFileToHidePath() == null || parameters.getFileToHidePath().isEmpty()) {
				setErrorMessage(R.string.error_file_to_hide_empty_string);
				return false;
			}
		}
		return true;
	}
	
	private void setErrorMessage(int code) {
		if (_context != null) {
			_errorMessage = _context.getResources().getString(code);
		}
	}
	
	public String getErrorMessage() {
		return _errorMessage;
	}
}
