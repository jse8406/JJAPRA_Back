package jjapra.app.repository;

import jjapra.app.model.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    ProjectMember save(ProjectMember projectMember);
}
