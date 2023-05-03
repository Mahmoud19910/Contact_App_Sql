package mah.nassa.contactapp;

import static mah.nassa.contactapp.R.menu.contact_menu;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import mah.nassa.contactapp.Acyivity.ViewContact;
import mah.nassa.contactapp.DataBase.ContactInfo_Base;
import mah.nassa.contactapp.Interface.OnItemClick;
import mah.nassa.contactapp.Modules_Class.ContactInfo;
import mah.nassa.contactapp.Modules_Class.EditeContactInfo;
import mah.nassa.contactapp.Modules_Class.ImageMethods;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> implements Filterable {
    private ArrayList<ContactInfo> contactInfoArrayList;
    private int layout;
    private Context context;
    OnItemClick listener;
    private ArrayList<ContactInfo> copyArrayList;




    public MyAdapter(ArrayList<ContactInfo> contactInfoArrayList, int layout, Context context , OnItemClick listener) {
        this.contactInfoArrayList = contactInfoArrayList;
        this.layout = layout;
        this.context = context;
        this.listener=listener;
        copyArrayList=contactInfoArrayList;
    }

    public MyAdapter(ArrayList<ContactInfo> contactInfoArrayList, int layout, Context context ) {
        this.contactInfoArrayList = contactInfoArrayList;
        this.layout = layout;
        this.context = context;

    }

    ImageMethods imageMethods=new ImageMethods();


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_design, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.phone.setText(contactInfoArrayList.get(position).getPhone());
        holder.name.setText(contactInfoArrayList.get(position).getName());

        //null اذا قام المستخدم باضافة صورة وقيمتها لاتساوي
        if (contactInfoArrayList.get(position).getPhoto() != null ) {
            Bitmap bitmap = imageMethods.StringToBitMap(contactInfoArrayList.get(position).getPhoto());
            holder.photo.setImageBitmap(bitmap);
        } else {
            holder.photo.setImageResource(R.drawable.img);
        }

        // On Click Item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent=new Intent(context, ViewContact.class);
//                    intent.putExtra("obj" , contactInfoArrayList.get(position));
                    intent.putExtra("Id" , contactInfoArrayList.get(position).getId());
                    context.startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        // Long Click item to create Context Menu
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context , holder.itemView);
                popupMenu.getMenuInflater().inflate(contact_menu , popupMenu.getMenu());

                // void methods to show icon in menu
                setForceShowIcon(popupMenu);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit:
                                Intent intent= new Intent(context, EditeContactInfo.class);
                                intent.putExtra("id" , contactInfoArrayList.get(position).getId());
                                context.startActivity(intent);
                                return true;
                            case R.id.delete:
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
                                TextView text=view.findViewById(R.id.nameDel);
                                text.setText(contactInfoArrayList.get(position).getName());
                                dialog.setContentView(view);
                                dialog.create();
                                dialog.show();

                                return true;
                            default:
                                return false;

                        }

                    }
                });
                popupMenu.show();
                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return contactInfoArrayList.size();
    }




// Filterable
    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                ArrayList<ContactInfo> newArray=new ArrayList<>();
                for(ContactInfo c:copyArrayList){
                    if(c.getName().contains(constraint)){
                        newArray.add(c);

                    }
                }
                filterResults.values=newArray;
                filterResults.count=newArray.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
             contactInfoArrayList= (ArrayList<ContactInfo>) results.values;
             notifyDataSetChanged();

            }
        };
        return filter;
    }


    //Context Menu ميثود لعرض الأيقونات في
    public void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field[] mFields = popupMenu.getClass().getDeclaredFields();
            for (Field field : mFields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> popupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method mMethods = popupHelper.getMethod("setForceShowIcon", boolean.class);
                    mMethods.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
