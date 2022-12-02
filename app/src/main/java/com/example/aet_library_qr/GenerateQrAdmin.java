package com.example.aet_library_qr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

public class GenerateQrAdmin extends AppCompatActivity {
    ImageView qrCodeImage;
    Button qrSaveImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_admin);
        qrCodeImage = (ImageView) findViewById(R.id.qrCodeImage);
        qrSaveImage = (Button) findViewById(R.id.qrSaveImage);

        /**
         * Need ng key para makapag generate ng qr code
         * pag walang key return sa main activity with message
         */
        Intent keyIntent = getIntent();
        if(keyIntent.getStringExtra("key") == null) {
            Intent mainIntent = new Intent(GenerateQrAdmin.this, MainActivity.class);
            Toast.makeText(GenerateQrAdmin.this, "Cannot generate qr code without key", Toast.LENGTH_SHORT).show();
            startActivity(mainIntent);
            return;
        }

        String key = keyIntent.getStringExtra("key");
        generateQr(key);

        qrSaveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadImage2();
            }
        });
    }

    private void downloadImage2() {
        Uri images = null;
        ContentResolver resolver = getContentResolver();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else {
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "images/");

        Uri uri = resolver.insert(images, contentValues);

        try {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) qrCodeImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            OutputStream outputStream = getContentResolver().openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

            Objects.requireNonNull(outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(GenerateQrAdmin.this, "Success", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(GenerateQrAdmin.this, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void downloadImage() {
        // Working but not recommended
        BitmapDrawable drawable = (BitmapDrawable) qrCodeImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        String path = getApplicationContext().getExternalFilesDir(null).toString() +"/images/"+ System.currentTimeMillis()+".jpg";
        File file = new File(path);
        file.getParentFile().mkdir();
        try {
            OutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        File dir = new File(path);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//        File file = new File(dir, System.currentTimeMillis()+".png");
//        try(FileOutputStream outputStream = new FileOutputStream(file)) {
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//            outputStream.flush();
//            outputStream.close();
//        } catch (IOException e) {
//            Toast.makeText(GenerateQrAdmin.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }

    }

    private void generateQr(String key)
    {
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(key, BarcodeFormat.QR_CODE, 1000, 1000);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}