package mah.nassa.contactapp;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import mah.nassa.contactapp.Acyivity.Favorite;
import mah.nassa.contactapp.DataBase.FavoriteDataBase;
import mah.nassa.contactapp.Interface.OnItemClick;
import mah.nassa.contactapp.Modules_Class.ContactInfo;
import mah.nassa.contactapp.Modules_Class.ImageMethods;


public class FavoriteMyAdapter extends RecyclerView.Adapter<FavoriteMyHolder> {
    private ArrayList<ContactInfo> contactInfoArrayList;
    private int layout;
    private Context context;
    OnItemClick listener;

    public FavoriteMyAdapter(ArrayList<ContactInfo> contactInfoArrayList, int layout, Context context , OnItemClick listener) {
        this.contactInfoArrayList = contactInfoArrayList;
        this.layout = layout;
        this.context = context;
        this.listener= listener;
    }








    @NonNull
    @Override
    public FavoriteMyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        FavoriteMyHolder myHolder = new FavoriteMyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteMyHolder holder, @SuppressLint("RecyclerView") int position) {

        if (holder != null) {

            holder.name.setText(contactInfoArrayList.get(position).getName());
            holder.phone.setText(contactInfoArrayList.get(position).getPhone());
            if (contactInfoArrayList.get(position).getPhoto() == null) {
                holder.photo.setImageResource(R.drawable.img);
            } else {
                holder.photo.setImageBitmap(new ImageMethods().StringToBitMap(contactInfoArrayList.get(position).getPhoto()));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, contactInfoArrayList.get(position).getId()+"", Toast.LENGTH_SHORT).show();
                }
            });

            // Create Alert Dialog to delete from favorite list
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog = new Dialog(context);
                    View view = LayoutInflater.from(context).inflate(R.layout.delete_dialog, null);
                    ((View) view).findViewById(R.id.delBt).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          listener.onItemCilckListener(contactInfoArrayList.get(position).getId());
                            contactInfoArrayList.remove(position);
                            notifyDataSetChanged();
                            dialog.dismiss();

                        }
                    });
                    view.findViewById(R.id.cancelBt).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    TextView text=view.findViewById(R.id.nameDel);
                    text.setText(contactInfoArrayList.get(position).getName().toString());
                    dialog.setContentView(view);
                    dialog.create();
                     dialog.show();



                }
            });

        }

    }


    @Override
    public int getItemCount() {
        return contactInfoArrayList.size();
    }


}
