package com.appdata.theperfect;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.appdata.theperfect.databinding.ProgresBarAllBinding;
import com.appdata.theperfect.interfaces.CallBack;
import com.appdata.theperfect.utils.PermissionCaller;
import com.appdata.theperfect.utils.Utility;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Stack;

import static com.appdata.theperfect.utils.AppConstants.REQUEST_CAMERA;
import static com.appdata.theperfect.utils.AppConstants.REQUEST_GALLERY;
import static com.appdata.theperfect.utils.AppConstants.txt_choose_from_camera;
import static com.appdata.theperfect.utils.AppConstants.txt_choose_from_gallery;

public class BaseActivity extends AppCompatActivity  {
    private static BaseActivity instance;
    private static final String TAG = BaseActivity.class.getSimpleName();
    private int minnumberoffragments = 1;
    private Stack<Fragment> mfragments = new Stack<Fragment>();
    private static final int permission_location_request_code = 91;
    private CallBack callBack;
    private  static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 103;
    public static Uri captureMediaFile = null;
    private String userChooseTask = "";
    private String imageFileName = "";
    private String mCurrentPhotoPath = "";
    private AlertDialog alertDialogselectImage ;
    private Dialog progressDialog  = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      //  AppController.setActivity(BaseActivity.this);
      //  instance = this;
       /* LayoutInflater inflater = getLayoutInflater();
        //View contentview = inflater.inflate(getlayoutid(), null);
        setContentView(contentview);*/
    }

    public static BaseActivity getinstance() {
        return instance;
    }


   /* public void directOpenCamera(CallBack callBack){
        this.callBack = callBack;
        userChooseTask = txt_choose_from_camera;
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkAndRequestPermissions(BaseActivity.this)) {
                openCamera();
            }
        } else {
            openCamera();
        }
    }*/


    public static String getTempMediaDirectory(Context context) {
        String state = Environment.getExternalStorageState();
        File dir = null;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            dir = context.getExternalCacheDir();
        } else {
            dir = context.getCacheDir();
        }

        if (dir != null && !dir.exists()) {
            dir.mkdirs();
        }
        if (dir.exists() && dir.isDirectory()) {
            return dir.getAbsolutePath();
        }
        return null;
    }

    public void loadProgressBar(Context mContext) {
        if (progressDialog == null) {
            ProgresBarAllBinding progresBarAllBinding = DataBindingUtil.inflate(LayoutInflater
                    .from(BaseActivity.this), R.layout.progres_bar_all, null, false);
//            ImageLoadInView.loadProgressGif(progresBarAllBinding.ivProgressLoader);
            progressDialog = new Dialog(mContext);
            Objects.requireNonNull(progressDialog.getWindow()).clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setCancelable(false);
            progressDialog.setContentView(progresBarAllBinding.getRoot());
            progressDialog.show();
        }
    }

    public void dismissProgressBar() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = null;
        }
    }
}



