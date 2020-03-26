package com.example.testtasklifehackstudio;

import android.net.Uri;

public class CompanyVisitCard {
    private String companyName;
    private Uri companyImage;
    private int id ;

    public int getId() {
        return id;
    }

    public CompanyVisitCard(String companyName, Uri companyImage, int id) {
        this.companyName = companyName;
        this.companyImage = companyImage;
        this.id = id;
    }


    public String getCompanyName() {
        return companyName;
    }


    public Uri getCompanyImage() {
        return companyImage;
    }


}
