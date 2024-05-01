package com.example.facereg.server;

public class ResponseApi {
    private String nrp;
    private String name;
    private String image;

    public String getNrp() {
        return nrp;
    }

    public void setNrp(String nrp) {
        this.nrp = nrp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ResponseApi(String nrp, String name ,String image) {
        this.nrp = nrp;
        this.name = name;
        this.image = image;
    }
}
