package com.example.demo.controller;

import com.example.demo.domain.GPS;
import com.example.demo.service.GPSService;
import io.jenetics.jpx.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class GPSController {

    private String fileLocation;

    @Autowired
    private GPSService gpsService;

    @RequestMapping(path = {"/homepage", "/"})
    public String homepage(HttpServletRequest request) {
        request.setAttribute("mode", "MODE_HOME");
        return "homepage";
    }

    @PostMapping("/api/upload")
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            return new ResponseEntity<>("Please select a file!", HttpStatus.OK);
        }
        try {
            saveUploadedFiles(Arrays.asList(multipartFile));
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Successfully uploaded - " +
                multipartFile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
    }

    @GetMapping("/api/getgps")
    @ResponseBody
    public GPS getGPSById(@RequestParam Integer id){
        return gpsService.getGPSById(id);
    }

    @GetMapping("/api/getgps/latest")
    @ResponseBody
    public List<com.example.demo.domain.WayPoint> getLatestGPSById(@RequestParam Integer id){
        GPS gpsById = gpsService.getGPSById(id);
        List<com.example.demo.domain.WayPoint> wayPoints = new ArrayList<>();
        if(gpsById != null &&  gpsById.getTracks() != null) {
            gpsById.getTracks().forEach(track -> {
                if( track.getTrackSegments() != null) {
                    track.getTrackSegments().forEach(trackSegment -> {
                        if(trackSegment != null) {
                            wayPoints.addAll(trackSegment.getWayPoints());
                        }
                    });
                }
            });
        }
        Collections.sort(wayPoints);
        return wayPoints;
    }

    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            File rootFile = new File(".");
            String rootPath = rootFile.getAbsolutePath();
            byte[] bytes = file.getBytes();
            fileLocation = rootPath.substring(0, rootPath.length() - 1) + file.getOriginalFilename();
            Path filePath = Paths.get(fileLocation);
            Files.write(filePath, bytes);
            GPX gpx = GPX.read(fileLocation);
            gpsService.saveGPS(convertFromGPXtoGPS(gpx));
        }

    }

    private GPS convertFromGPXtoGPS(GPX gpx) {
        GPS gps = new GPS();
        Metadata metadata = gpx.getMetadata().get();
        gps.setCreator(gpx.getCreator());
        gps.setVersion(gpx.getVersion());
        com.example.demo.domain.Metadata gpsMetadata = new com.example.demo.domain.Metadata();
        com.example.demo.domain.Person person = new com.example.demo.domain.Person();
        person.setName(metadata.getAuthor().get().getName().orElse(""));
        gpsMetadata.setAuthor(person);
        gpsMetadata.setDescription(metadata.getDescription().get());
        List<com.example.demo.domain.Link> links = new ArrayList<>();
        metadata.getLinks().forEach(link -> {
            com.example.demo.domain.Link gpsLink = new com.example.demo.domain.Link();
            gpsLink.setHref(link.getHref());
            gpsLink.setText(link.getText().orElse(""));
            gpsLink.setType(link.getType().orElse(""));
            links.add(gpsLink);

        });
        gpsMetadata.setLinks(links);
        gpsMetadata.setName(metadata.getName().get());
        gpsMetadata.setTime(metadata.getTime().get());
        gps.setMetadata(gpsMetadata);
        List<com.example.demo.domain.WayPoint> wayPointList = new ArrayList<>();
        gpx.getWayPoints().forEach(wayPoint -> {
            com.example.demo.domain.WayPoint point = new com.example.demo.domain.WayPoint();
            point.setLatitude(wayPoint.getLatitude().doubleValue());
            point.setLongitude(wayPoint.getLongitude().doubleValue());
            if (wayPoint.getElevation().isPresent()) {
                point.setElevation(wayPoint.getElevation().get().longValue());
            }
            point.setName(wayPoint.getName().orElse(""));
            point.setSymbol(wayPoint.getSymbol().orElse(""));
            if (wayPoint.getTime().isPresent()) {
                point.setTime(wayPoint.getTime().get());
            }
            wayPointList.add(point);
        });
        gps.setWayPoints(wayPointList);

        List<com.example.demo.domain.Track> trackList = new ArrayList<>();
        for (Track track : gpx.getTracks()) {
            com.example.demo.domain.Track gpsTrack = new com.example.demo.domain.Track();
            List<com.example.demo.domain.TrackSegment> trackSegments = new ArrayList<>();
            for (TrackSegment segment : track.getSegments()) {
                com.example.demo.domain.TrackSegment trackSegment = new com.example.demo.domain.TrackSegment();
                List<com.example.demo.domain.WayPoint> pointList = new ArrayList<>();
                for (WayPoint wayPoint : segment.getPoints()) {
                    com.example.demo.domain.WayPoint point = new com.example.demo.domain.WayPoint();
                    point.setLatitude(wayPoint.getLatitude().doubleValue());
                    point.setLongitude(wayPoint.getLongitude().doubleValue());
                    point.setElevation(wayPoint.getElevation().get().longValue());
                    point.setName(wayPoint.getName().orElse(""));
                    point.setSymbol(wayPoint.getSymbol().orElse(""));
                    point.setTime(wayPoint.getTime().orElse(null));
                    pointList.add(point);
                }
                trackSegment.setWayPoints(pointList);
                trackSegments.add(trackSegment);
            }
            gpsTrack.setTrackSegments(trackSegments);
            trackList.add(gpsTrack);
        }

        gps.setTracks(trackList);
        return gps;
    }

}
