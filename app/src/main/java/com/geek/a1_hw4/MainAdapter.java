package com.geek.a1_hw4;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;
import static com.geek.a1_hw4.R.*;
import static com.geek.a1_hw4.R.id.*;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    public Context context;
    public List<UserContactModel> list;
    ItemClickListener listener;
    public MainAdapter(Context context, List<UserContactModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_recycler_view,parent,false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
          holder.onBind(list.get(position));
          holder.menuImageView.setOnClickListener(v -> {

              PopupMenu popupMenu = new PopupMenu(context,holder.menuImageView);
               popupMenu.inflate(menu.recycler_menu);
               popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                   @Override
                   public boolean onMenuItemClick(MenuItem item) {
                       switch (item.getItemId()) {
                           case deleteMenu:
                               list.remove(position);
                               notifyItemRemoved(position);
                               notifyItemRangeChanged(position, getItemCount());
                               break;
                           case callMenu:
                               String number = list.get(position).getPhone();
                               if (!number.equals("")) {
//                                   Intent call = new Intent(Intent.ACTION_CALL);
//                                   call.setData(Uri.parse("tel:"+number));
//                                   if(ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//                                      break;
//                                   }
//                                   context.startActivity(call);

                                   context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null)));
                               } else Toast.makeText(context,"You don't add phone number!",Toast.LENGTH_LONG).show();

                               break;
                       }
                       return false;
                   }
               });
               popupMenu.show();
          });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
          private final TextView textViewName;
          private final TextView textViewPhone;
          private final ImageView imageView;
          private final ImageView menuImageView;
          UserContactModel model;
        public MainViewHolder(@NonNull View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                textViewName = itemView.findViewById(recyclerTextName);
                textViewPhone = itemView.findViewById(recyclerTextPhone);
                imageView = itemView.findViewById(recyclerImage);
                menuImageView = itemView.findViewById(imageMenu);
        }

        public void onBind(UserContactModel userContactModel) {
                    this.model = userContactModel;
                    textViewName.setText(model.getName());
                    textViewPhone.setText(model.getPhone());
                    if (model.getImage() != null ) {
                        Glide.with(context)
                                .load(Uri.parse(model.getImage()))
                                .apply(RequestOptions.circleCropTransform())
                                .into(imageView);
                    }
                    else  {
                        Glide.with(context)
                                .load(drawable.profile)
                                .apply(RequestOptions.circleCropTransform())
                                .into(imageView);
                    }
        }

        @Override
        public void onClick(View v) {
             if (listener != null){ listener.onItemClick(getAdapterPosition()); }
        }
    }
    public  void setOnClickListener(ItemClickListener mListener){ this.listener = mListener; }
   public interface ItemClickListener{
        void onItemClick(int position);
   }
}