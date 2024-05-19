package jjapra.app.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.context.annotation.DependsOn;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@DependsOn(value={"Project", "Account"})
@Getter
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "projectId", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "memberId", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
//    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Role role;
}
