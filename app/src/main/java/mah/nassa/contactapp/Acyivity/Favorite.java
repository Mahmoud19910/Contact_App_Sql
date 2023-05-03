package mah.nassa.contactapp.Acyivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import mah.nassa.contactapp.DataBase.FavoriteDataBase;
import mah.nassa.contactapp.FavoriteMyAdapter;
import mah.nassa.contactapp.Interface.OnItemClick;
import mah.nassa.contactapp.MyAdapter;
import mah.nassa.contactapp.R;
import mah.nassa.contactapp.databinding.ActivityFavoriteBinding;

public class Favorite extends AppCompatActivity implements OnItemClick {

    ActivityFavoriteBinding favoriteBinding;
     FavoriteDataBase favoriteDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        favoriteBinding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(favoriteBinding.getRoot());


        favoriteDataBase=new FavoriteDataBase(this ,"favorite" , 1 );



        favoriteBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Favorite.this, MainActivity.class));
            }
        });

        FavoriteMyAdapter myAdapter=new FavoriteMyAdapter(favoriteDataBase.getData() , R.layout.favoritr_contact_design ,this , this);
        favoriteBinding.favRecycler.setAdapter(myAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(layoutManager.VERTICAL);
        favoriteBinding.favRecycler.setLayoutManager(layoutManager);


    }


    @Override
    public void onItemCilckListener(int id) {
        Toast.makeText(this, "ID:" + id, Toast.LENGTH_SHORT).show();
        favoriteDataBase.deletById(id);

    }
}