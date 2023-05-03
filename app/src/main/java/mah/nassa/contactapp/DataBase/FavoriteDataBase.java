package mah.nassa.contactapp.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import mah.nassa.contactapp.Modules_Class.ContactInfo;

public class FavoriteDataBase extends SQLiteOpenHelper {
    public FavoriteDataBase(@Nullable Context context, @Nullable String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table favorite(_id integer primary key , name text not null , phone text not null , email text , address text , image text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists favorite");
        onCreate(db);
    }

    // اضافة البيانات
    public void saveData(ContactInfo contactInfo , Context context , String tableName){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("name" , contactInfo.getName());
        values.put("phone" , contactInfo.getPhone());
        values.put("address" , contactInfo.getAdress());
        values.put("email" , contactInfo.getEmail());
        values.put("image" , contactInfo.getPhoto());
        values.put("_id" , contactInfo.getId());
        long addData= db.insert(tableName , null , values);

        if(addData==-1){
            Toast.makeText(context, "Filed Add Data", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }

    }

    // ارجاع كافة البيانات
    public ArrayList<ContactInfo> getData(){
        SQLiteDatabase read=this.getReadableDatabase();
        ArrayList<ContactInfo> infoArrayList=new ArrayList<>();
        Cursor cursor=read.rawQuery("select * from favorite" , null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            int name_Num=cursor.getColumnIndex("name");
            String name_Text=cursor.getString(name_Num);

            int phone_Num=cursor.getColumnIndex("phone");
            String phone_Text=cursor.getString(phone_Num);

            int email_Num=cursor.getColumnIndex("email");
            String email_Text=cursor.getString(email_Num);

            int address_Num=cursor.getColumnIndex("address");
            String address_Text = cursor.getString(address_Num);

            int image_Num=cursor.getColumnIndex("image");
            String image_Text=cursor.getString(image_Num);
            int _id=cursor.getColumnIndex("_id");
            int id=cursor.getInt(_id);

            ContactInfo contactInfo=new ContactInfo(name_Text,phone_Text,address_Text,email_Text, image_Text , id);
            infoArrayList.add(contactInfo);
            cursor.moveToNext();
        }
        cursor.close();
        return infoArrayList;
    }


  public void deletById(int id){
      SQLiteDatabase db= this.getWritableDatabase();
      String sql = "DELETE FROM favorite WHERE _id = ?";
      db.execSQL(sql, new String[]{String.valueOf(id)});
  }
}
