package com.example.facereguser.server;

public class ResponseApiName {
    private String nrp;
    private String name;


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



    public ResponseApiName(String nrp, String name ) {
        this.nrp = nrp;
        this.name = name;

    }
}
