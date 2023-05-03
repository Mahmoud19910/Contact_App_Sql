package mah.nassa.contactapp;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder {
    ImageView photo;
    TextView name, phone;
    public MyHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.name_View);
        phone=itemView.findViewById(R.id.phone_View);
        photo=itemView.findViewById(R.id.profile_image);
    }
}
