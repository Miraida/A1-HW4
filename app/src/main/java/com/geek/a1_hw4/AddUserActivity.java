package com.geek.a1_hw4;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class AddUserActivity extends AppCompatActivity {

    Uri imageUri = null;
    ImageView imageView;
    EditText editTextName;
    EditText editTextPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        imageView = findViewById(R.id.userImageView);
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextTextPhone);
    }

    public void addUserPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }
    }

    public void returnTransition(View view) {
        UserContactModel model = new UserContactModel();
        String name = editTextName.getText().toString();
        if (!name.equals(""))
        {
            String phone = editTextPhone.getText().toString();
            if (imageUri == null) model.setImage(null);
            else{
                    String  image = imageUri.toString();
                    model.setImage(image);
                }

            model.setName(name);
            model.setPhone(phone);
            Intent intent = new Intent();
            intent.putExtra("userData",model);
            setResult(RESULT_OK,intent);
            finish();
        }
        else {
            Toast toast = Toast.makeText(this,"You must enter the NAME", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
        }
    }
}