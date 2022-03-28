package com.example.homiegram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.parse.ProgressCallback;

public class PostActivity extends AppCompatActivity {

    private static final String TAG = "PostActivity";
    public final static int PICK_PHOTO_CODE = 1046;
    public String photoFileName = "photo.jpg";
    private Bitmap photo;

    private ImageView ivPicture;
    private EditText etDesc;
    private Button btnUpload;
    private Button btnPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        ivPicture = findViewById(R.id.ivPicture);
        etDesc = findViewById(R.id.etDesc);
        btnUpload = findViewById(R.id.btnUpload);
        btnPost = findViewById(R.id.btnPost);


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchGallery();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = etDesc.getText().toString();
                if(description.isEmpty()){
                    Toast.makeText(PostActivity.this, "Description can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                try {
                    savePost(description, currentUser, photo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }

    private void savePost(String description, ParseUser currentUser, Bitmap photo) throws IOException {
        Post post = new Post();
        post.setDescription(description);
        post.setUser(currentUser);

        File photoFile = new File(PostActivity.this.getCacheDir(), "photo.png");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(photoFile);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();

        post.setImage(new ParseFile(photoFile));

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(TAG, "Issue with posting post", e);
                    Toast.makeText(PostActivity.this, "Couldn't post", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Posted successfully");
            }
        });
    }
    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == PICK_PHOTO_CODE){
                Uri selectedImageUri = data.getData();
                if(null != selectedImageUri){
                    photo = loadFromUri(selectedImageUri);
                    ivPicture.setImageBitmap(photo);
                }
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new  File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    private void launchGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if(getIntent().resolveActivity(getPackageManager()) != null){
            startActivityForResult(i, PICK_PHOTO_CODE);
        }
    }
}