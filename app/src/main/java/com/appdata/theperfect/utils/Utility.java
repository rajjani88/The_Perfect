package com.appdata.theperfect.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class Utility {

    public static String getDateFormatFromOneToOther(String date, String your_date_format, String result_date_format) {
        SimpleDateFormat df = new SimpleDateFormat(your_date_format, Locale.getDefault());
        Date new_date;
        long t = 0L;
        SimpleDateFormat f = new SimpleDateFormat(result_date_format,Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            new_date = df.parse(date);
            t = new_date.getTime();
            calendar.setTimeInMillis(t);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String s = f.format(calendar.getTime());
        Log.d("date:- ", "" + date);
        Log.d("your_date_format:- ", "" + your_date_format);
        Log.d("result_date_format:- ", "" + s);
//        s = s.replace("..", ".");

        return s;
    }

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


    public static boolean validateString(String str) {
        return stringNotNull(str) && stringNotEmpty(str);
    }

    public static boolean isValidPhoneNumber(String code, String phone) {
        //return phone.length() >= 9 && phone.length() <= 13 && Patterns.PHONE.matcher(phone).matches();
        return (phone.length() == 10 && Patterns.PHONE.matcher(phone).matches()) || phone.length() == 0;
    }

    private static boolean stringNotNull(String str) {
        return str != null;
    }

    private static boolean stringNotEmpty(String str) {
        return !str.isEmpty();
    }

    public static boolean validateEmail(String email) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        return emailPattern.matcher(email).matches();
    }

    public static boolean validatePassword(String password) {
        return !(TextUtils.isEmpty(password) || password.length() < 8);
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        if (validateFilePath(contentUri.getPath())) {
            return contentUri.getPath();
        }
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            if (cursor == null)
                return null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private static boolean validateFilePath(String path) {
        return validateString(path) && validateFile(new File(path));
    }

    private static boolean validateFile(File file) {
        return file.exists();
    }

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void expand(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();

            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        if(activity.getCurrentFocus() != null)
            inputMethodManager.hideSoftInputFromWindow(Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
    }

    public static String getAndroidId(Context mContext) {

        @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return android_id;
    }

    public static int getAppVersionCode(Context mContext) {
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getAppVersionName(Context mContext) {
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAppPackageName(Context mContext) {
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /*public static boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ChatTypingService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }*/

}
