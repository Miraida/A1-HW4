package com.geek.a1_hw4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class changeUserDataActivity extends AppCompatActivity {
    ImageView imageView;
    EditText editTextName;
    EditText editTextPhone;
    UserContactModel model;
    Uri imageUri = null;
    boolean isPhotoChanged = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_data);
        imageView = findViewById(R.id.changeUserImageView);
        editTextName = findViewById(R.id.changeEditTextName);
        editTextPhone = findViewById(R.id.changeEditTextPhone);
        Intent intent = new Intent();
        intent = getIntent();
        model = (UserContactModel) intent.getSerializableExtra("changeUserData");
        if (model.getImage()!=null){
           Glide.with(this)
                   .load(Uri.parse(model.getImage()))
                   .apply(RequestOptions.circleCropTransform())
                   .into(imageView);
        }
        else { Glide.with(this)
                .load(R.drawable.profile)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);}
        editTextName.setText(model.getName());
        editTextPhone.setText(model.getPhone());
    }

    public void returnTransitionFromChangeUserData(View view) {
        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        UserContactModel changedModel = new UserContactModel();
        changedModel.setName(name);
        changedModel.setPhone(phone);
        if (isPhotoChanged){
                if (imageUri != null)
                    changedModel.setImage(imageUri.toString());
                else changedModel.setImage(null);
        }
        else changedModel.setImage(model.getImage());
        Intent intent = new Intent();
        intent.putExtra("changedUserData",changedModel);
        setResult(RESULT_OK,intent);
        finish();
    }

    public void changeUserPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK ){
            isPhotoChanged = true;
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }

    }
}