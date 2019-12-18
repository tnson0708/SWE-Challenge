package com.example.demo.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity
@Table(name = "waypoint")
public class WayPoint implements Serializable, Comparable<WayPoint> {
    public WayPoint() {
    }

    public WayPoint(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Double latitude;
    private Double longitude;
    private String name;
    private String symbol;
    private Long elevation;
    private ZonedDateTime time;

    public Long getElevation() {
        return elevation;
    }

    public void setElevation(Long elevation) {
        this.elevation = elevation;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public int compareTo(WayPoint wayPoint) {
        if (time.isEqual(wayPoint.getTime())) {
            return 0;
        } else if (time.isBefore(wayPoint.getTime())) {
            return 1;
        } else {
            return -1;
        }
    }
}
