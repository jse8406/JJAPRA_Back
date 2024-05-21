package jjapra.app.repository;

import jjapra.app.model.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
    Optional<Project> findByTitle(String title);
    Optional<Project> findById(Integer id);
    void delete(Project project);
}
