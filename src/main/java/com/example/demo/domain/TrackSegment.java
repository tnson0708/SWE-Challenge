package com.example.demo.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tracksegment")
public class TrackSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @OneToMany(cascade = CascadeType.ALL)
    private List<WayPoint> wayPoints;

    public List<WayPoint> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(List<WayPoint> wayPoints) {
        this.wayPoints = wayPoints;
    }
}
