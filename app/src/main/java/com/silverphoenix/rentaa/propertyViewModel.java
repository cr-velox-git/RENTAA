package com.silverphoenix.rentaa;

public class propertyViewModel {

    private String id,image, city, flat_type, price, installment, address, des, owner_info, posted_on, pincode;
    private boolean drink, lateNight;

    public propertyViewModel(String id, String image, String city, String flat_type, String price, String installment, String address, String pincode, String des, String owner_info, String posted_on, boolean drink, boolean lateNight) {
        this.id = id;
        this.image = image;
        this.city = city;
        this.flat_type = flat_type;
        this.price = price;
        this.installment = installment;
        this.address = address;
        this.pincode = pincode;
        this.des = des;
        this.owner_info = owner_info;
        this.posted_on = posted_on;
        this.drink = drink;
        this.lateNight = lateNight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public boolean isDrink() {
        return drink;
    }

    public void setDrink(boolean drink) {
        this.drink = drink;
    }

    public boolean isLateNight() {
        return lateNight;
    }

    public void setLateNight(boolean lateNight) {
        this.lateNight = lateNight;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFlat_type() {
        return flat_type;
    }

    public void setFlat_type(String flat_type) {
        this.flat_type = flat_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getOwner_info() {
        return owner_info;
    }

    public void setOwner_info(String owner_info) {
        this.owner_info = owner_info;
    }

    public String getPosted_on() {
        return posted_on;
    }

    public void setPosted_on(String posted_on) {
        this.posted_on = posted_on;
    }
}
