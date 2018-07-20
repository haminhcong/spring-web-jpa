package com.spring.webapp.repository;

import com.spring.webapp.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Note findByTitle(String tite);
}