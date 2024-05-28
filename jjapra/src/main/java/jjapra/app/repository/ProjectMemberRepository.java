package jjapra.app.repository;

import jjapra.app.model.member.Member;
import jjapra.app.model.member.Role;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {
    ProjectMember save(ProjectMember projectMember);
    List<ProjectMember> findByMemberId(String id);
    @Query("SELECT pm FROM ProjectMember pm WHERE pm.project = :project AND pm.member = :member")
    Optional<ProjectMember> findByProjectAndMember(@Param("project") Project project, @Param("member") Member member);

    @Query("SELECT pm FROM ProjectMember pm WHERE pm.project = :project")
    List<ProjectMember> findByProject(@Param("project") Project project);
}
