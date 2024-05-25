package jjapra.app.repository;

import jjapra.app.model.member.Member;
import jjapra.app.model.member.Role;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    ProjectMember save(ProjectMember projectMember);
    List<ProjectMember> findByMemberId(String id);
}
