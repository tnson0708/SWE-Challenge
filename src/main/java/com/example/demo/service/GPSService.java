package com.example.demo.service;

import com.example.demo.domain.GPS;
import com.example.demo.repository.GPSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GPSService {
    @Autowired
    private GPSRepository gpsRepository;

    public List<GPS> getGPSs(){
        return gpsRepository.findAll();
    }

    public GPS getGPSById(Integer gpsId){
        return gpsRepository.findById(gpsId).get();
    }

    public void saveGPS(GPS gps){
        gpsRepository.save(gps);
    }

}
