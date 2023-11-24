package com.example.travel_tour_booking_app;

import java.io.Serializable;

public enum Country implements Serializable {
    //Quốc tế
    Dubai(R.drawable.img_dubai,"Dubai",false),
    Iceland(R.drawable.img_iceland,"Iceland",false),
    Phap(R.drawable.img_phap,"Pháp",false),
    TayBanNha(R.drawable.img_taybannha,"Tây Ban Nha",false),
    ThoNhiKy(R.drawable.img_thonhiky,"Thổ Nhĩ Kỳ",false),
    TrungQuoc(R.drawable.img_trungquoc,"Trung Quốc",false),
    Canada(R.drawable.img_canada,"Canada",false),
    NhatBan(R.drawable.img_nhatban,"Nhật Bản",false),
    ThaiLan(R.drawable.img_thailan, "Thái Lan", false),

    //Nội địa
    HaNoi(R.drawable.img_hanoi, "Hà Nội", true),
    HaiPhong(R.drawable.img_haiphong,"Hải Phòng",true),
    HoChiMinh(R.drawable.img_hcm, "Hồ Chí Minh", true),
    DaNang(R.drawable.img_hcm,"Đà Nẵng", true),
    DaLat(R.drawable.img_dalat,"Đà Lạt",true),
    NhaTrang(R.drawable.img_nhatrang,"Nha Trang",true),
    CanTho(R.drawable.img_cantho,"Cần Thơ",true),
    PhuQuoc(R.drawable.img_phuquoc,"Phú Quốc",true),
    Vinh(R.drawable.img_vinh,"Vinh",true)
    ;


    int Image;
    String Name;
    Boolean isInland;

    Country(){}
    Country(int image, String name, Boolean isInland) {
        Image = image;
        Name = name;
        this.isInland = isInland;
    }

    public int getImage() {
        return Image;
    }

    public String getName() {
        return Name;
    }

    public void setImage(int image) {
        Image = image;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getInland() {
        return isInland;
    }

    public void setInland(Boolean inland) {
        isInland = inland;
    }
}
