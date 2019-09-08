package ir.pitok.cafe.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class CafeUtilities {
    public static String path = "http://45.82.137.153";
    public static int googleSignInRequestCode = 1001;
    private static char[] validSigns = {'_','.','-'};


    private static String md5One(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String hashPassword(String s){
        for(int i =0;i<=9;i++){
            s= md5One(s);
        }
        return s;
    }
    public static float px2dp(int px){
        return (float) (px / Resources.getSystem().getDisplayMetrics().density);
    }
    public static float dp2Px(float dp) {
        return (dp * Resources.getSystem().getDisplayMetrics().density);
    }
    private static boolean isEnglish(char ch){
        int ascii = (int) ch;
        return (((ascii>=97 && ascii<=122) || (ascii>=65 && ascii<=90)));
    }
    private static boolean isNumber(char ch){
        int ascii = (int) ch;
        return (ascii>=48 && ascii<=57);
    }
    private static boolean isValidSign(char ch){
        for (char c : validSigns){
            if (ch == c)return true;
        }
        return false;
    }
    public static boolean isValidUsername(String username){
        for (char c : username.toCharArray()){
            if (!(isEnglish(c) || isNumber(c) || isValidSign(c) ))return false;
        }
        return true;
    }
    public static boolean isValidPhoneNumber(String phoneNumber){
        for (char c : phoneNumber.toCharArray()){
            if (!(isNumber(c)))return false;
        }
        if (phoneNumber.length()!=11)return false;
        return true;
    }
    public static boolean isValidPassword(String password){
        return password.length()>=6;
    }
    public static String getAgent(Context context){
        @SuppressLint("HardwareIds")
        String android_id = Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
        return "Android Version "+ Build.VERSION.SDK_INT +" | "+ android_id;
    }
    public static String getApiToken(Context context){
        SharedPreferences sp_tokens = context.getSharedPreferences("tokens",Context.MODE_PRIVATE);
        return sp_tokens.getString("api_token","");
    }
    public static boolean apiTokenExist(Context context){
        return !context.getSharedPreferences("tokens",Context.MODE_PRIVATE).getString("api_token", "").equals("");
    }
    public static String getFcmToken(Context context){
        SharedPreferences sp_tokens = context.getSharedPreferences("tokens",Context.MODE_PRIVATE);
        return sp_tokens.getString("fcm_token","");
    }
    public static void setApiToken(String apiToken,Context context){
        SharedPreferences.Editor editor_tokens = context.getSharedPreferences("tokens",Context.MODE_PRIVATE).edit();
        editor_tokens.putString("api_token",apiToken);
        editor_tokens.apply();
        editor_tokens.commit();
    }
    public static LatLng getLastLocation(Context context){
        SharedPreferences sp_locs = context.getSharedPreferences("locations",Context.MODE_PRIVATE);
        double lat = Double.parseDouble(sp_locs.getString("lat","32.646728"));
        double lng = Double.parseDouble(sp_locs.getString("long","51.667850"));
        return new LatLng(lat,lng);
    }
    public static void setLastLocation(LatLng latlng, Context context){
        SharedPreferences.Editor editor_locs = context.getSharedPreferences("locations",Context.MODE_PRIVATE).edit();
        editor_locs.putString("lat",latlng.latitude+"");
        editor_locs.putString("long", latlng.longitude +"");
        editor_locs.apply();
        editor_locs.commit();
    }
    public static void handleFailor(Throwable t,Context context){
        if (t instanceof IOException){
            try {
                Toast.makeText(context, "اینترنت شما قطع می باشد", Toast.LENGTH_LONG).show();
            } catch (Exception ignored) {}
        }else{
            try {
                Toast.makeText(context, "خطا در ارتباط با سرور", Toast.LENGTH_LONG).show();
            } catch (Exception ignored) {}
        }
    }
    public static String en2prText(String text) {
        String[] adad = new String[]{"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "۸", "۹"};
        for (int i=0;i<10;i++){
            try {
                text = text.replace(Character.forDigit(i, 10), adad[i].charAt(0));
            }catch (Exception ignored){}
        }
        return text;
    }
    public static boolean checkPermissions(Context context){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M)return true;
        boolean pr1 = (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) ;
        boolean pr2 = (context.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)== PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED);
        return pr1 && pr2;
    }
    public static void getPermissions(Context context, Activity activity){
        if (checkPermissions(context))return;
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M)return;
        activity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
    }
    public static void hideKeyboard(Activity activity) {
        try{
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }catch (Exception ignored){}
    }
    public static void showKeyboard(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            //Find the currently focused view, so we can grab the correct window token from it.
            View view = activity.getCurrentFocus();
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = new View(activity);
            }
            if (imm != null) {
                imm.showSoftInput(view, 0);
            }
        }catch (Exception ignored){}
    }
    public static void changeLocale(Context context){
        String languageToLoad = "fa_";
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }
    public static boolean isUrlValid(String url){
        return Patterns.WEB_URL.matcher(url).matches();
    }
    public static String getStandardUrl(String Url){
        if (!isUrlValid(Url))return Url;
        if (Url.startsWith("https://") || Url.startsWith("http://"))return Url;
        return "http://"+Url;
    }
    public static GradientDrawable getRectangleWithTopCorner(int color,int corner){
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setColor(color);
        shape.setCornerRadii(new float[]{corner,corner,corner,corner,0,0,0,0});
        return shape;
    }
    public static int getScreenHeight(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
    public static int getScreenWidth(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
    public static float getCornerFromOffset(float offset){
        float precent = 2f-(offset*2);
        if (offset>=0.5) return dp2Px(16*precent);
        return dp2Px(16);
    }
    public static String ItemsIdListToJsonArray(List<Integer> items){
        JSONArray array = new JSONArray();
        for(Integer item : items){
            JSONObject object = new JSONObject();
            try{object.put("item_id",item);}catch (JSONException ignored) {}
            array.put(object);
        }
        return array.toString();
    }


}
