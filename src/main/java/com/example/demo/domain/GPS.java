package com.example.demo.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "gps")
public class GPS {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer gpsId;
    private String creator;
    private String version;
    @OneToOne(cascade = CascadeType.ALL)
    private Metadata metadata;

    @OneToMany(cascade = CascadeType.ALL)
    private List<WayPoint> wayPoints;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Track> tracks;

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public Integer getGpsId() {
        return gpsId;
    }

    public void setGpsId(Integer gpsId) {
        this.gpsId = gpsId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<WayPoint> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(List<WayPoint> wayPoints) {
        this.wayPoints = wayPoints;
    }
}
