package com.example.travel_tour_booking_app;

public enum Seclection {
    Place("Địa điểm",R.drawable.ic_place),
    Promotion("Khuyến mãi", R.drawable.ic_promotion),
    News("Tin tức",R.drawable.ic_news),
    Question("Hỗ trợ",R.drawable.ic_question),
    Facebook("Facebook", R.drawable.fb_logo),
    Google("Google", R.drawable.google_logo),
    Phone("Số điện thoại", R.drawable.ic_phone);
    String Name;
    int Image;

    Seclection(String Name, int Image) {
        this.Name = Name;
        this.Image = Image;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public int getImage() {
        return Image;
    }
}
