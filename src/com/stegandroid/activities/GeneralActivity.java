//package com.stegandroid.activities;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Matrix;
//import android.graphics.drawable.BitmapDrawable;
//import android.media.ThumbnailUtils;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.stegandroid.R;
//import com.stegandroid.directorydialog.ChoosenDirectoryListener;
//import com.stegandroid.directorydialog.DirectoryDialog;
//import com.stegandroid.process.EncodeProcess;
//import com.stegandroid.tools.Utils;
//
//public class GeneralActivity extends Activity {
//
//	final int CHOOSE_VIDEO_CONTAINER = 0;
//	final int CHOOSE_FILE_CONTENT = 1;
//
//	private String _pathToVideoContainer;
//	private String _pathToVideoDestination;
//	private String _pathToFileToHide;
//	private Boolean _fileToHide;
//	
//	private Button _btnChooseVideoContainer;
//	private Button _btnChooseVideoDest;
//	private Button _btnTextContent;
//	private Button _btnFileContent;
//	private Button _btnChooseFileToHide;
//	private Button _btnProcess;
//	private EditText _editTextContentToHide;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_general);
//
////		_btnChooseVideoContainer = (Button) findViewById(R.id.btn_choose_video_container);
////		_btnChooseVideoDest = (Button) findViewById(R.id.btn_choose_video_destination);
////		_btnTextContent = (Button) findViewById(R.id.btn_text_content);
////		_btnFileContent = (Button) findViewById(R.id.btn_file_content);
////		_btnProcess = (Button) findViewById(R.id.btn_process);
////		_btnChooseFileToHide = (Button) findViewById(R.id.btn_choose_file_to_hide);
////		_editTextContentToHide = (EditText) findViewById(R.id.edit_text_content_to_hide);
////		_fileToHide = false;
////		testEnableProcessButton();
////		
////		_btnChooseVideoContainer.setOnClickListener(onClickListener);
////		_btnChooseVideoDest.setOnClickListener(onClickListener);
////		_btnTextContent.setOnClickListener(onClickListener);
////		_btnFileContent.setOnClickListener(onClickListener);
////		_btnProcess.setOnClickListener(onClickListener);
////		_btnChooseFileToHide.setOnClickListener(onClickListener);
////		_editTextContentToHide.addTextChangedListener(onTextChangedListener);
//	}
//
//	private void testEnableProcessButton() {
////		String text = ((EditText) findViewById(R.id.edit_text_content_to_hide)).getText().toString();
////		
////		if (_pathToVideoContainer == null || _pathToVideoContainer.equals(R.string.lbl_empty_choose_video_container)
////				|| _pathToVideoDestination == null || _pathToVideoDestination.equals(R.string.lbl_empty_choose_video_destination)
////			    || (_fileToHide && (_pathToFileToHide == null || _pathToFileToHide.equals(R.string.lbl_empty_choose_file_to_hide))
////			    || (!_fileToHide && (text == null || text.isEmpty()))))
////			_btnProcess.setEnabled(false);
////		else
////			_btnProcess.setEnabled(true);
//	}
//	
//	private void showFileChooser(int code) {
////	    Intent intent = new Intent(Intent.ACTION_GET_CONTENT); 
////	    intent.addCategory(Intent.CATEGORY_OPENABLE);
////
////	    if (code == CHOOSE_VIDEO_CONTAINER) {
////	    	intent.setType("video/*"); 
////	    } else {
////	    	intent.setType("*/*"); 
////	    }
////
////	    try {
////	        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.lbl_choose_video_container)), code);
////	    } catch (android.content.ActivityNotFoundException ex) {
////	        Toast.makeText(this, getResources().getString(R.string.err_file_manager), Toast.LENGTH_SHORT).show();
////	    }
//	}
//	
//	private void callbackFileChooserVideoContainer(int requestCode, int resultCode, Intent data) {
////		Uri selectedVideoLocation;
////		Bitmap bitmap;
////		BitmapDrawable bitmapDrawable;
////		Matrix matrix;
////		DisplayMetrics displayMetrics;
////		
////		if (resultCode == Activity.RESULT_OK) {
////			selectedVideoLocation = data.getData();
////			_pathToVideoContainer = Utils.getRealPathFromUri(this, selectedVideoLocation);
////			_pathToVideoDestination = Utils.getBasenameFromPath(_pathToVideoContainer);
////			((TextView) findViewById(R.id.label_path_choosen_video_container)).setText(Utils.getFileNameFromPath(_pathToVideoContainer));
////			((TextView) findViewById(R.id.label_path_choosen_video_destination)).setText(Utils.getFileNameFromPath(_pathToVideoDestination));
////			
////			displayMetrics = new DisplayMetrics();
////			getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
////			matrix = new Matrix();
////			matrix.postScale(displayMetrics.scaledDensity, displayMetrics.scaledDensity);
////			
////			bitmap = ThumbnailUtils.createVideoThumbnail(_pathToVideoContainer, MediaStore.Images.Thumbnails.MICRO_KIND); 
////			bitmap = Bitmap.createScaledBitmap(bitmap,48, 48, true);
////			bitmap = Bitmap.createBitmap(bitmap, 0, 0,  bitmap.getWidth(), bitmap.getHeight(), matrix, true);
////			bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
////			((TextView) findViewById(R.id.label_path_choosen_video_container)).setCompoundDrawablesWithIntrinsicBounds(bitmapDrawable, null, null, null);
////			
////		}
////		testEnableProcessButton();
//	}
//	
//	private void callbackFileChooserFileToHide(int requestCode, int resultCode, Intent data) {
////		Uri selectedFileLocation;
////		
////		if (resultCode == Activity.RESULT_OK) {
////			selectedFileLocation = data.getData();
////			_pathToFileToHide = Utils.getRealPathFromUri(this, selectedFileLocation);
////			((TextView) findViewById(R.id.label_path_choosen_file_to_hide)).setText(Utils.getFileNameFromPath(_pathToFileToHide));
////		}
////		testEnableProcessButton();
//	}
//		
//	private void showDirectoryChooser() {
////		DirectoryDialog dialog = new DirectoryDialog(this);
////		dialog.setChoosenDirectoryListener(onChoosenDirectoryListener);
////		dialog.show();
//	}
//
//	private void showTextContent() {
////		((EditText) findViewById(R.id.edit_text_content_to_hide)).setVisibility(View.VISIBLE);
////		((LinearLayout) findViewById(R.id.layout_file_to_hide)).setVisibility(View.GONE);
////		_fileToHide = false;
////		testEnableProcessButton();
//	}
//	
//	private void showFilePathContent() {
////		((EditText) findViewById(R.id.edit_text_content_to_hide)).setVisibility(View.GONE);
////		((LinearLayout) findViewById(R.id.layout_file_to_hide)).setVisibility(View.VISIBLE);		
////		_fileToHide = true;
////		testEnableProcessButton();
//	}
//	
//	private void process() {
////		EncodeProcess encodeProcess = new EncodeProcess();
////
////        Toast.makeText(this, "Processing ... (or not)", Toast.LENGTH_SHORT).show();
////		
////		encodeProcess.encode(_pathToVideoContainer, _pathToVideoDestination);
////		
//	}	
//	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.general, menu);
//		return true;
//	}
//
////	@Override
////	public void onActivityResult(int requestCode, int resultCode, Intent data) {
////		super.onActivityResult(requestCode, resultCode, data);
////
////		switch (requestCode) {
////			case CHOOSE_VIDEO_CONTAINER:
////				callbackFileChooserVideoContainer(requestCode, resultCode, data);
////				break;
////			case CHOOSE_FILE_CONTENT:
////				callbackFileChooserFileToHide(requestCode, resultCode, data);
////				break;
////			default:
////				Log.d("DEBUG", "There is a big problem there!");
////		}
////	}
////
////	private ChoosenDirectoryListener onChoosenDirectoryListener = new ChoosenDirectoryListener() {
////
////		@Override
////		public void onChoosenDir(String directory) {
////			_pathToVideoDestination = directory;
////			((TextView) findViewById(R.id.label_path_choosen_video_destination)).setText(_pathToVideoDestination);
////			testEnableProcessButton();
////		}
////	};
////
////	
////	private OnClickListener onClickListener = new OnClickListener() {
////
////		@Override
////		public void onClick(View arg0) {
////			switch (arg0.getId()) {
////				case R.id.btn_choose_video_container:
////					showFileChooser(CHOOSE_VIDEO_CONTAINER);
////					break;
////				case R.id.btn_choose_video_destination:
////					showDirectoryChooser();
////					break;
////				case R.id.btn_text_content:
////					showTextContent();
////					break;
////				case R.id.btn_file_content:
////					showFilePathContent();
////					break;
////				case R.id.btn_choose_file_to_hide:
////					showFileChooser(CHOOSE_FILE_CONTENT);
////					break;
////				case R.id.btn_process:
////					process();
////					break;
////				default:
////					Log.d("DEBUG", "There is a big problem there!");
////			}
////		}
////	};
////
////	private TextWatcher onTextChangedListener = new TextWatcher() {
////		
////		@Override
////		public void onTextChanged(CharSequence s, int start, int before, int count) {
////			
////		}
////		
////		@Override
////		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
////		}
////		
////		@Override
////		public void afterTextChanged(Editable s) {
////			testEnableProcessButton();
////		}
////	};
//}
