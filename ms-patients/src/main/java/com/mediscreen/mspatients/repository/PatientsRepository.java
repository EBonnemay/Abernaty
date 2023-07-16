package com.mediscreen.mspatients.repository;

import com.mediscreen.mspatients.model.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientsRepository extends CrudRepository<Patient, Integer> {


    //@Query(value="SELECT patient FROM table_patients WHERE patient.family = :family and patient.given = :given", nativeQuery = true)
    //Optional<Patient> findByFullName(@Param("family")String family, @Param("given") String given);
   @Query(value = "SELECT * FROM table_patients WHERE family = :family AND given = :given", nativeQuery = true)
   Optional<Patient> findByFullName(@Param("family") String family, @Param("given") String given);

    @Override
    boolean existsById(Integer integer);
}
