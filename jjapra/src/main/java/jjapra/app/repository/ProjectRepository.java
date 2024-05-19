package jjapra.app.repository;

import jjapra.app.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, String> {
    Optional<Project> findByTitle(String title);
}
