package com.scintillato.lisd;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by adikundiv on 01-04-2017.
 */

public class Postion  implements Serializable{
    private Double latitude,longitude;
    private Date time;
    int Trip_id;

    public Postion(Double latitude, Double longitude, long time,int Trip_id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = new Date(time);
        this.Trip_id= Trip_id;
    }

    public int getTrip_id() {
        return Trip_id;
    }

    public void setTrip_id(int trip_id) {
        Trip_id = trip_id;
    }

    public void setTime(Date time) {
        this.time = time;
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
