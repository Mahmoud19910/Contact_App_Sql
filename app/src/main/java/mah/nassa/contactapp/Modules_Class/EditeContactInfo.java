package mah.nassa.contactapp.Modules_Class;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.IOException;

import mah.nassa.contactapp.Acyivity.AddContacts;
import mah.nassa.contactapp.DataBase.ContactInfo_Base;
import mah.nassa.contactapp.Acyivity.MainActivity;
import mah.nassa.contactapp.R;
import mah.nassa.contactapp.databinding.ActivityEditeContactInfoBinding;


public class EditeContactInfo extends AppCompatActivity implements View.OnClickListener{
    ActivityEditeContactInfoBinding editeContactInfoBinding;
    ContactInfo_Base contactInfo_base;
    ImageMethods imageMethods;
    ContactInfo contactInfo;
    AddContacts addContactsActivity;
    int REQUEST_CODE_CAMERA = 1;
    int REQUEST_CODE_GALLERY = 2;
    String imageString;
    EditText name, phone, email, address;
    int _id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editeContactInfoBinding = ActivityEditeContactInfoBinding.inflate(getLayoutInflater());
        setContentView(editeContactInfoBinding.getRoot());

        contactInfo_base = new ContactInfo_Base(this, "contact", 2);
        addContactsActivity = new AddContacts();
        imageMethods = new ImageMethods();

        // Binding View
        name = editeContactInfoBinding.editTextTextPersonName;
        phone = editeContactInfoBinding.editTextTextPhoneNum;
        email = editeContactInfoBinding.editTextTextEmail;
        address = editeContactInfoBinding.editTextTextAdress;

        Intent intent = getIntent();

     _id=intent.getIntExtra("id", 0);
        // View Data in Fields
        viewData();

        //Listener
        editeContactInfoBinding.image.setOnClickListener(this);
        editeContactInfoBinding.checkEdite.setOnClickListener(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            editeContactInfoBinding.image.setImageBitmap(bitmap);
            imageString = imageMethods.BitMapToString(bitmap);

        } else if (requestCode == REQUEST_CODE_GALLERY && RESULT_OK == resultCode) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                editeContactInfoBinding.image.setImageBitmap(bitmap);
                imageString = imageMethods.BitMapToString(bitmap);

            } catch (IOException e) {
                Toast.makeText(this, imageString, Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_GALLERY) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            } else {
                //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
            }
        }
    }

    //Adapter عرض البيانات المستقبلة من
    public void viewData() {
        contactInfo = contactInfo_base.getDataById(_id);

        if(contactInfo!=null){
            editeContactInfoBinding.editTextTextPhoneNum.setText(contactInfo.getPhone().toString());
            editeContactInfoBinding.editTextTextEmail.setText(contactInfo.getEmail().toString());
            editeContactInfoBinding.editTextTextPersonName.setText(contactInfo.getName().toString());

            if (contactInfo.getPhoto()!=null) {
                Bitmap bitmap = imageMethods.StringToBitMap(contactInfo.getPhoto());
                editeContactInfoBinding.image.setImageBitmap(bitmap);
            }
            else{
                editeContactInfoBinding.image.setImageResource(R.drawable.img);

            }
            if( contactInfo.getAdress()!=null){
                editeContactInfoBinding.editTextTextAdress.setText(contactInfo.getAdress());
            }
            else
            {
                editeContactInfoBinding.editTextTextAdress.setText("Address");
            }

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.image:
                createBottomSheetDialog();
                break;

            case R.id.checkEdite:

                if (checkInput() == true && imageString != null) {
                    ContactInfo contactInfo1 = new ContactInfo(name.getText().toString(), phone.getText().toString(), address.getText().toString(), email.getText().toString(), imageString);
                    contactInfo_base.editDataBase(contactInfo.getId(), contactInfo1);
                    startActivity(new Intent(this, MainActivity.class));

                }
                else
                if(checkInput() == true && imageString == null) {
                    ContactInfo contactInfo1 = new ContactInfo(name.getText().toString(), phone.getText().toString(), address.getText().toString(), email.getText().toString(),contactInfo.getPhoto());
                    contactInfo_base.editDataBase(contactInfo.getId(), contactInfo1);
                    startActivity(new Intent(this, MainActivity.class));
                }
                else {
                    Toast.makeText(EditeContactInfo.this, "Error in the inputs!!", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.backEdit:
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
    }

    // Check The Input
    public boolean checkInput() {
        boolean saveChecked = true;

        if (name.getText().toString().isEmpty()) {
            name.setError("required");
            saveChecked = false;
        }
        else
        if (phone.getText().toString().isEmpty()) {
            phone.setError("required");
            saveChecked = false;
        }
        else
        if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            email.setError("Invalid is Email");
            saveChecked = false;
        }

        return saveChecked;
    }


    // create bottom sheet Dialog
    public void createBottomSheetDialog() {
        BottomSheetDialog termsDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottomsheetcamera, null);
        LinearLayout camera = view.findViewById(R.id.camera);
        // Open The Camera
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCameraPermission();
                termsDialog.dismiss();
            }
        });

        // Open The Gallery
        LinearLayout gallery = view.findViewById(R.id.gallery);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGalleryPermission();
            }
        });

        termsDialog.setContentView(view);
        termsDialog.show();
    }

    // Check Permission Camera
    public void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);

            //لم يتم أخذ الصلاحية لأن فحص الصالحية يساوي -1

        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
        }

    }

    // Check Gallery Permission
    public void checkGalleryPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
        }

    }

}

