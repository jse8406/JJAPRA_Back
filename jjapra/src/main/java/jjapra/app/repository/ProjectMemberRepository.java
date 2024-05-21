package jjapra.app.repository;

import jjapra.app.model.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    ProjectMember save(ProjectMember projectMember);

    List<ProjectMember> findByMemberId(String memberId);
}
