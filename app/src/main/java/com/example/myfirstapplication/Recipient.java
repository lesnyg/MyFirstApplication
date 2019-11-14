package com.example.myfirstapplication;
import android.graphics.Bitmap;

public class Recipient {
    private String personId;
    private String photoId;
    private String name;
    private String indiviPay;
    private String rating;
    private Bitmap bitmap;

    public Recipient(Bitmap bitmap, String name, String indiviPay, String rating) {
        this.bitmap = bitmap;
        this.name = name;
        this.indiviPay = indiviPay;
        this.rating = rating;
    }
    public Recipient(String photoId,Bitmap bitmap){
        this.photoId = photoId;
        this.bitmap = bitmap;
    }
    public Recipient(){

    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndiviPay() {
        return indiviPay;
    }

    public void setIndiviPay(String indiviPay) {
        this.indiviPay = indiviPay;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
