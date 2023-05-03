package mah.nassa.contactapp.Modules_Class;

import java.io.Serializable;

public class ContactInfo implements Serializable {
    private String name;
    private String phone;
    private String address;
    private String email;
    private String photo;
    private int id;

    public ContactInfo(){

    }

    public ContactInfo(String name, String phone, String address, String email, String photo) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.photo = photo;

    }

    public ContactInfo(String name, String phone, String address, String email, String photo, int id) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.photo = photo;
        this.id = id;
    }


    public ContactInfo(String name, String phone, String email, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return address;
    }

    public void setAdress(String adress) {
        this.address = adress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
