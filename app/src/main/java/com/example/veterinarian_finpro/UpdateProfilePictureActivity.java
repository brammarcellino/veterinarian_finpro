package com.example.veterinarian_finpro;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UpdateProfilePictureActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ImageView imageView;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private  FirebaseUser firebaseUser;
    private static  final  int PICK_IMAGE_REQUEST=1;
    private Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile_picture);
        getSupportActionBar().setTitle("upload profile picture");
        auth = FirebaseAuth.getInstance();
        Button buttonloadpicture= findViewById(R.id.upload_pic_choose_button);
        Button buttonuploadpic = findViewById(R.id.upload_pic_choose_button);
        imageView = findViewById(R.id.imageView_profile_dp);
        progressBar = findViewById(R.id.progressBar);
        storageReference = FirebaseStorage.getInstance().getReference("DisplayPicUser");
        Uri uri = firebaseUser.getPhotoUrl();
        Picasso.get().load(uri).into(imageView);


        buttonloadpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfileChose();
            }
        });

    buttonuploadpic.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            Uploadpic();
        }
    });
    }

    private void Uploadpic() {
        if (uriImage != null){
            StorageReference fileReferance= storageReference.child(auth.getCurrentUser().getUid()+"."+getfileExtension(uriImage) );
            fileReferance.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  fileReferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {
                          Uri downloaduri = uri;
                          firebaseUser =auth.getCurrentUser();

                          UserProfileChangeRequest profileupdate= new UserProfileChangeRequest.Builder().setPhotoUri(downloaduri).build();
                          firebaseUser.updateProfile(profileupdate);
                      }
                  });
                  progressBar.setVisibility(View.GONE);
                    Toast.makeText(UpdateProfilePictureActivity.this, "upload Succesfull", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateProfilePictureActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateProfilePictureActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(UpdateProfilePictureActivity.this, "No file Selected", Toast.LENGTH_SHORT).show();
        }
    }
    private String getfileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void openfileChose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       if (requestCode== PICK_IMAGE_REQUEST&& requestCode==RESULT_OK&&data !=null && data.getData()!=null){
           uriImage = data.getData();
           imageView.setImageURI(uriImage);
       }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu_profile,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_refresh) {
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);

        }else if (id== R.id.menu_update_profile){
            Intent intent = new Intent(UpdateProfilePictureActivity.this,UpdateUserProfileActivity.class);
            startActivity(intent);
            finish();
        }else if (id == R.id.menu_Update_Email){
            Intent intent= new Intent(UpdateProfilePictureActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        } else if(id== R.id.menu_setting){
            Toast.makeText(UpdateProfilePictureActivity.this, "menu_setting", Toast.LENGTH_SHORT).show();
        }else if(id==R.id.menu_chage_password){
            Intent intent= new Intent(UpdateProfilePictureActivity.this,ChagePasswodActivity.class);
            startActivity(intent);
            finish();
        } else if (id==R.id.menu_delete_profile){
            Intent intent = new Intent(UpdateProfilePictureActivity.this,DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        } else if(id==R.id.menu_Logout){
            auth.signOut();
            Toast.makeText(UpdateProfilePictureActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateProfilePictureActivity.this,MainActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        } else{
            Toast.makeText(UpdateProfilePictureActivity.this, "Somthing went Wrong", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}