package mah.nassa.contactapp.Acyivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
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

import mah.nassa.contactapp.DataBase.ContactInfo_Base;
import mah.nassa.contactapp.Modules_Class.ContactInfo;
import mah.nassa.contactapp.Modules_Class.ImageMethods;
import mah.nassa.contactapp.R;
import mah.nassa.contactapp.databinding.ActivityAddContactsBinding;

public class AddContacts extends AppCompatActivity implements View.OnClickListener {
    private ActivityAddContactsBinding addContactsBinding;
    EditText name, phone, email, address;
    int REQUEST_CODE_CAMERA = 1;
    int REQUEST_CODE_GALLERY = 2;
    String imageString;
    public static SQLiteDatabase writableDataBase;
    private ContactInfo_Base contactInfo_base;
    ImageMethods imageMethods;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContactsBinding = ActivityAddContactsBinding.inflate(getLayoutInflater());
        setContentView(addContactsBinding.getRoot());

        // Binding View
        name = addContactsBinding.editTextTextPersonName;
        phone = addContactsBinding.editTextTextPhoneNum;
        email = addContactsBinding.editTextTextEmail;
        address = addContactsBinding.editTextTextAdress;

        contactInfo_base = new ContactInfo_Base(this, "contact", 2);
        writableDataBase = contactInfo_base.getWritableDatabase();
        imageMethods=new ImageMethods();

        // Listener
        addContactsBinding.check.setOnClickListener(this);
        addContactsBinding.image.setOnClickListener(this);
        addContactsBinding.back.setOnClickListener(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            addContactsBinding.image.setImageBitmap(bitmap);
            imageString = imageMethods.BitMapToString(bitmap);

        } else if (requestCode == REQUEST_CODE_GALLERY && RESULT_OK == resultCode) {
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                addContactsBinding.image.setImageBitmap(bitmap);
                imageString = imageMethods.BitMapToString(bitmap);

            } catch (IOException e) {
                Toast.makeText(this, imageString, Toast.LENGTH_SHORT).show();
            }


        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == REQUEST_CODE_GALLERY) {
//            // If request is cancelled, the result arrays are empty.
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
//            } else {
//                //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
//            }
//        }
//    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check:
                if (checkInput() == true) {

                    if (imageString != null ) {
                        ContactInfo contactInfo1 = new ContactInfo(name.getText().toString(), phone.getText().toString(), address.getText().toString(), email.getText().toString(), imageString);
                        Toast.makeText(this, contactInfo1.getAdress().toString(), Toast.LENGTH_SHORT).show();
                        contactInfo_base.saveData(contactInfo1, this);
                        startActivity(new Intent(AddContacts.this, MainActivity.class));

                    } else {
                        ContactInfo contactInfo = new ContactInfo(name.getText().toString(), phone.getText().toString(), email.getText().toString(), address.getText().toString());
                        contactInfo_base.saveData(contactInfo, this);
                        startActivity(new Intent(AddContacts.this, MainActivity.class));
                    }
                }
                break;

            case R.id.back:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;

            case R.id.image:
                createBottomSheetDialog();
                break;

            default:

        }
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);

            //لم يتم أخذ الصلاحية لأن فحص الصالحية يساوي -1

        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
        }

    }

    // Check Gallery Permission
    public void checkGalleryPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
        } else {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
        }

    }


}