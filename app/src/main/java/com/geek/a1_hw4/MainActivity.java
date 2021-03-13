package com.geek.a1_hw4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.geek.a1_hw4.R.id.position;

public class MainActivity extends AppCompatActivity implements MainAdapter.ItemClickListener {
     MainAdapter adapter;
     RecyclerView recyclerView;
     List<UserContactModel> list;

    private int positionOnItemClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter(MainActivity.this,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
    }


    public void addUser(View view) {
        Intent intent = new Intent(MainActivity.this,AddUserActivity.class);
        startActivityForResult(intent,1);
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this,changeUserDataActivity.class);
        intent.putExtra("changeUserData",list.get(position));
        positionOnItemClick = position;
        startActivityForResult(intent,3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK){
            UserContactModel model = (UserContactModel) data.getSerializableExtra("userData");
            list.add(model);
            adapter.notifyDataSetChanged();
        }
        if (requestCode == 3 && resultCode == RESULT_OK){
            UserContactModel model = (UserContactModel) data.getSerializableExtra("changedUserData");
            list.remove(positionOnItemClick);
            adapter.notifyItemRemoved(positionOnItemClick);
            adapter.notifyItemRangeChanged(positionOnItemClick, adapter.getItemCount());
            list.add(positionOnItemClick,model);
            adapter.notifyDataSetChanged();
        }

    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP |
            ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
           int dragPosition = viewHolder.getAdapterPosition();
           int targetPosition = target.getAdapterPosition();
            Collections.swap(adapter.list,dragPosition,targetPosition);
           adapter.notifyItemMoved(dragPosition,targetPosition);
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
           adapter.list.remove(viewHolder.getAdapterPosition());
           adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                new MainAdapter(this,list).makePhoneCall();
            }
        }
    }
}