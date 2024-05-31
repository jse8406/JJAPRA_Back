package jjapra.app.datainit;

import jjapra.app.model.issue.Issue;
import jjapra.app.model.member.Member;
import jjapra.app.model.member.MemberRole;
import jjapra.app.model.member.Role;
import jjapra.app.model.project.Project;
import jjapra.app.model.project.ProjectMember;
import jjapra.app.repository.IssueRepository;
import jjapra.app.repository.MemberRepository;
import jjapra.app.repository.ProjectMemberRepository;
import jjapra.app.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final IssueRepository issueRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        Member member1 = Member.builder()
                .id("admin")
                .password("admin")
                .name("admin")
                .email("admin@naver.com")
                .phone_num("01012345678")
                .role(MemberRole.ADMIN)
                .build();

        Member member2 = Member.builder()
                .id("user1")
                .password("user1")
                .name("user1")
                .email("tester@naver.com")
                .phone_num("01011112222")
                .role(MemberRole.USER)
                .build();

        Member member3 = Member.builder()
                .id("user2")
                .password("user2")
                .name("user2")
                .email("pl@naver.com")
                .phone_num("01011112222")
                .role(MemberRole.USER)
                .build();

        Member member4 = Member.builder()
                .id("user3")
                .password("user3")
                .name("user3")
                .email("dev@naver.com")
                .phone_num("01011112222")
                .role(MemberRole.USER)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        Project project1 = Project.builder()
                .title("DDing")
                .description("DDing project")
                .build();

        Project project2 = Project.builder()
                .title("Miging")
                .description("Miging project")
                .build();

        Project project3 = Project.builder()
                .title("Suzzang")
                .description("Suzzang project")
                .build();

        Project project4 = Project.builder()
                .title("Channy")
                .description("Channy project")
                .build();

        Project project5 = Project.builder()
                .title("Unsta9ram")
                .description("Unsta9ram project")
                .build();

        projectRepository.save(project1);
        projectRepository.save(project2);
        projectRepository.save(project3);
        projectRepository.save(project4);
        projectRepository.save(project5);

        ProjectMember projectMember1 = ProjectMember.builder()
                .project(project1)
                .member(member1)
                .role(Role.ADMIN)
                .build();

        ProjectMember projectMember2 = ProjectMember.builder()
                .project(project2)
                .member(member1)
                .role(Role.ADMIN)
                .build();

        ProjectMember projectMember3 = ProjectMember.builder()
                .project(project3)
                .member(member1)
                .role(Role.ADMIN)
                .build();

        ProjectMember projectMember4 = ProjectMember.builder()
                .project(project4)
                .member(member1)
                .role(Role.ADMIN)
                .build();

        ProjectMember projectMember5 = ProjectMember.builder()
                .project(project5)
                .member(member1)
                .role(Role.ADMIN)
                .build();

        ProjectMember projectMember6 = ProjectMember.builder()
                .project(project1)
                .member(member2)
                .role(Role.TESTER)
                .build();

        ProjectMember projectMember7 = ProjectMember.builder()
                .project(project2)
                .member(member2)
                .role(Role.TESTER)
                .build();

        ProjectMember projectMember8 = ProjectMember.builder()
                .project(project1)
                .member(member3)
                .role(Role.PL)
                .build();

        ProjectMember projectMember9 = ProjectMember.builder()
                .project(project1)
                .member(member4)
                .role(Role.DEV)
                .build();

        projectMemberRepository.save(projectMember1);
        projectMemberRepository.save(projectMember2);
        projectMemberRepository.save(projectMember3);
        projectMemberRepository.save(projectMember4);
        projectMemberRepository.save(projectMember5);
        projectMemberRepository.save(projectMember6);
        projectMemberRepository.save(projectMember7);
        projectMemberRepository.save(projectMember8);
        projectMemberRepository.save(projectMember9);

        Issue issue1 = Issue.builder()
                .projectId(project1.getId())
                .title("issue1")
                .description("issue1")
                .writer(member1.getId())
                .build();

        Issue issue2 = Issue.builder()
                .projectId(project1.getId())
                .title("issue2")
                .description("issue2")
                .writer(member2.getId())
                .build();

        issueRepository.save(issue1);
        issueRepository.save(issue2);
    }
}
