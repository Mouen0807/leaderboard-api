package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.models.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestRepository extends JpaRepository<Contest, UUID> {
    public Optional<Contest> findById(UUID id);
    public List<Contest> findByOwnerId(UUID id);
}

