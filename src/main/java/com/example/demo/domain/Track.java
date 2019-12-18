package com.example.demo.domain;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "track")
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL)
    private List<TrackSegment> trackSegments;

    public List<TrackSegment> getTrackSegments() {
        return trackSegments;
    }

    public void setTrackSegments(List<TrackSegment> trackSegments) {
        this.trackSegments = trackSegments;
    }
}
