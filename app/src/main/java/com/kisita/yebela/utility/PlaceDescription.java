package com.kisita.yebela.utility;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ELECTRO DEPOT on 08-01-17.
 */

public class PlaceDescription {
    private LatLng latlng;
    private String name;
    private String address;
    private String website;
    private String phoneNumber;

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
