package mah.nassa.contactapp.Acyivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import mah.nassa.contactapp.DataBase.ContactInfo_Base;
import mah.nassa.contactapp.DataBase.FavoriteDataBase;
import mah.nassa.contactapp.Modules_Class.ContactInfo;
import mah.nassa.contactapp.Modules_Class.EditeContactInfo;
import mah.nassa.contactapp.Modules_Class.ImageMethods;
import mah.nassa.contactapp.R;
import mah.nassa.contactapp.databinding.ActivityViewContactBinding;

public class ViewContact extends AppCompatActivity implements View.OnClickListener {

    ActivityViewContactBinding contactBinding;
    ContactInfo_Base contactInfo_base;
    ImageMethods imageMethods;
    ContactInfo contactInfo;
    FavoriteDataBase favoriteDataBase;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactBinding = ActivityViewContactBinding.inflate(getLayoutInflater());
        setContentView(contactBinding.getRoot());

        contactInfo_base = new ContactInfo_Base(this, "contact", 2);
        favoriteDataBase = new FavoriteDataBase(this, "favorite", 1);
        imageMethods = new ImageMethods();

        // View Data in Fields
        viewData();


        // Listener
        contactBinding.back.setOnClickListener(this);
        contactBinding.favorite.setOnClickListener(this);
        contactBinding.sendEmail.setOnClickListener(this);
        contactBinding.sendMessage.setOnClickListener(this);
        contactBinding.call.setOnClickListener(this);
        contactBinding.editBut.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back:
                startActivity(new Intent(this, MainActivity.class));
                break;

            case R.id.call:
                openDialerPhone();
                break;

            case R.id.sendMessage:
                sendMessage();
                break;

            case R.id.editBut:
                Intent in = new Intent(this, EditeContactInfo.class);
                in.putExtra("id", id);
                startActivity(in);
                break;

            case R.id.favorite:
                contactBinding.favorite.setImageResource(R.drawable.fillstar);

                // Save Favorite Contact To Favorite Data Base
                favoriteDataBase.saveData(contactInfo, ViewContact.this, "favorite");
                Toast.makeText(this, "Success Add To Favorite", Toast.LENGTH_SHORT).show();
                break;

            case R.id.sendEmail:
                openGmail();
                break;

            default:
        }

    }

    //Adapter عرض البيانات المستقبلة من
    public void viewData() {

        Intent intent = getIntent();
        id = intent.getIntExtra("Id", 0);
        contactInfo = contactInfo_base.getDataById(id);

//    contactInfo= (ContactInfo) intent.getSerializableExtra("obj");
        if (contactInfo != null) {
            contactBinding.mobileNumber.setText(contactInfo.getPhone().toString());
            contactBinding.location.setText(contactInfo.getAdress().toString());
            contactBinding.emailAddress.setText(contactInfo.getEmail().toString());
            contactBinding.PersonName.setText(contactInfo.getName().toString());

            if (contactInfo.getPhoto() != null) {
                Bitmap bitmap = imageMethods.StringToBitMap(contactInfo.getPhoto());
                contactBinding.personImage.setImageBitmap(bitmap);
            } else {
                contactBinding.personImage.setImageResource(R.drawable.img);
            }

        }


    }


    // Open Gmail By Implicit Intent
    public void openGmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "abc@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text");
        startActivity(Intent.createChooser(emailIntent, null));
    }

    // Open Message By Implicit Intent
    public void sendMessage() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", contactInfo.getPhone().toString(), null)));
    }

    // Open Phone Dialer ByImplicit Intent
    public void openDialerPhone() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:1234567890"));
        startActivity(intent);
    }


}