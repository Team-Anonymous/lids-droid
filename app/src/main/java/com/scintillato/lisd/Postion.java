package com.scintillato.lisd;

import java.util.Date;

/**
 * Created by adikundiv on 01-04-2017.
 */

public class Postion {
    private Double latitude,longitude;
    private Date time;

    public Postion(Double latitude, Double longitude, long time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = new Date(time);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = new Date(time);
    }
}
