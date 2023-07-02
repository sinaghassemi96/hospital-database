package com.example.hospitaldatabase.repository;

import com.example.hospitaldatabase.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log,Long> {

}
