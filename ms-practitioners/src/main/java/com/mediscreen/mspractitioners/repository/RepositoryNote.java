package com.mediscreen.mspractitioners.repository;

import com.mediscreen.mspractitioners.Model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryNote extends MongoRepository<Note, String> {
    List<Note> findByPatId(String patId);

}
