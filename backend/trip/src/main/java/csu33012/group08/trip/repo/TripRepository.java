package csu33012.group08.trip.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import csu33012.group08.trip.domain.Trip;


public interface TripRepository extends CrudRepository<Trip, String> {
    List<Trip> findAll();
}
