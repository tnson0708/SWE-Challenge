package com.example.demo.repository;

import com.example.demo.domain.GPS;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface GPSRepository extends CrudRepository<GPS, Integer> {
    @Override
    List<GPS> findAll();

    @Override
    Optional<GPS> findById(Integer integer);

}
