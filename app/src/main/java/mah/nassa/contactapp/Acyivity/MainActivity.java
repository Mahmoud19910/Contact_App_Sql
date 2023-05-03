package mah.nassa.contactapp.Acyivity;

import static mah.nassa.contactapp.R.menu.menudroop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import mah.nassa.contactapp.DataBase.ContactInfo_Base;
import mah.nassa.contactapp.DataBase.FavoriteDataBase;
import mah.nassa.contactapp.Modules_Class.ContactInfo;
import mah.nassa.contactapp.MyAdapter;
import mah.nassa.contactapp.Interface.OnItemClick;
import mah.nassa.contactapp.R;
import mah.nassa.contactapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements OnItemClick {
    ActivityMainBinding mainBinding;
    private static ContactInfo_Base contactInfo_base;
    MyAdapter myAdapter;
    FavoriteDataBase favoriteDataBase;





    // Shared Preferences
    int NightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(mainBinding.getRoot());

        // Shared Preferences Dark Mode
        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        NightMode = sharedPreferences.getInt("NightModeInt", 1);
        AppCompatDelegate.setDefaultNightMode(NightMode);




        // Instance Of Data Base
        contactInfo_base = new ContactInfo_Base(this, "contact", 2);
        favoriteDataBase=new FavoriteDataBase(this ,"favorite" , 1 );


        myAdapter = new MyAdapter(contactInfo_base.getData(), R.layout.contact_design, (Context) this, MainActivity.this);
        mainBinding.recycler.setAdapter(myAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(layoutManager.VERTICAL);
        mainBinding.recycler.setLayoutManager(layoutManager);



        // floating button
        mainBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddContacts.class));
            }
        });

        // On Click Menu
        mainBinding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createMenu(MainActivity.this , v );
            }
        });

        // OnClick Search
        mainBinding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return true;
            }
        });




    }


    // Create Menu
    public void createMenu(Context context , View view){
        PopupMenu popupMenu=new PopupMenu(context , view);
        popupMenu.inflate(menudroop);
        popupMenu.show();
        // void methods to show icon in menu
        myAdapter.setForceShowIcon(popupMenu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.favorite:
                        startActivity(new Intent(MainActivity.this , Favorite.class));
                        return true;

                    case R.id.mode:
                        return true;

                    case R.id.onBut:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                        NightMode = AppCompatDelegate.getDefaultNightMode();
                        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putInt("NightModeInt", NightMode);
                        editor.apply();


                        return true;

                    case R.id.offBut:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        NightMode = AppCompatDelegate.getDefaultNightMode();

                        sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
                        editor = sharedPreferences.edit();

                        editor.putInt("NightModeInt", NightMode);
                        editor.apply();

                        return true;

                    default:
                        return false;
                }
            }
        });

    }

    // Implement Interface Method to delete item by id
    @Override
    public void onItemCilckListener(int id) {
        Toast.makeText(this, "id="+id, Toast.LENGTH_SHORT).show();
        contactInfo_base.deleteItemFromDatabase(id);
        favoriteDataBase.deletById(id);


    }
}