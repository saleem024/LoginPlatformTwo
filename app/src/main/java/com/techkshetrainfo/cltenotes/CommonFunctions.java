package com.techkshetrainfo.cltenotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public  class CommonFunctions {

    public static boolean isnetworkavailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

        public static void show_dialog(final Context context, String title, String message){

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            // Setting Dialog Title
            alertDialog.setTitle(title);

            // Setting Dialog Message
            alertDialog.setMessage(message);

            // Setting Icon to Dialog
            //alertDialog.setIcon(R.drawable.delete);

            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int which) {

                    // Write your code here to invoke YES event
                   // Toast.makeText(context, "You clicked on YES", Toast.LENGTH_SHORT).show();
                }
            });

            alertDialog.show();


        }

    public static boolean isValidMobile(String phone2)
    {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone2))
        {
            check = !(phone2.length() < 6 || phone2.length() > 13);
        }
        else
        {
            check=false;
        }
        return check;
    }

    public static boolean isValidMail(String email2)
    {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email2);
        check = m.matches();


        return check;
    }


    public void show_dialog_action(final Context context, String title, String message){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                // Write your code here to invoke YES event
                Toast.makeText(context, "You clicked on YES", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(context, "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();


    }

    public static Target picassoImageTarget(Context context, final String imageDir, final String imageName) {
        // Log.d("picassoImageTarget", " picassoImageTarget");
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("image", "image saved to >>>" + myImageFile.getAbsolutePath());

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {}
            }
        };
    }
    public static boolean Filedelete(Context context, String fileName) {
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir("uploads", Context.MODE_PRIVATE);
        File myImageFile = new File(directory, fileName);

        return myImageFile.delete();
        //return myImageFile.exists();
    }
    public static boolean isFilePresent(Context context, String fileName) {
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir("uploads", Context.MODE_PRIVATE);
        File myImageFile = new File(directory, fileName);

        return myImageFile.exists();
    }
    public static boolean isFileok(Context context, String fileName) {
        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir("uploads", Context.MODE_PRIVATE);
        File myImageFile = new File(directory, fileName);

        long length = myImageFile.length();
        return length != 0;


    }


}
