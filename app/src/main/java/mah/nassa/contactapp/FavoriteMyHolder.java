package mah.nassa.contactapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteMyHolder extends RecyclerView.ViewHolder {
    ImageView photo , delete;
    TextView name, phone;

    public FavoriteMyHolder(@NonNull View itemView) {
        super(itemView);
        name=itemView.findViewById(R.id.name_View);
        phone=itemView.findViewById(R.id.phone_View);
        photo=itemView.findViewById(R.id.profile_image);
        delete=itemView.findViewById(R.id.deletBtn);
    }
}
