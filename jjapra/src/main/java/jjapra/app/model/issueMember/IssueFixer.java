package jjapra.app.model.issueMember;

import jakarta.persistence.*;
import jjapra.app.model.issue.Issue;
import jjapra.app.model.member.Member;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
public class IssueFixer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "issueId", nullable = false)
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;
}
