package jjapra.app.model.issue;

import jakarta.persistence.*;
import jjapra.app.model.issue.Issue;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId", updatable = false)
    private Integer commentId;

    @ManyToOne
    @JoinColumn(name = "issueId", nullable = false)
    @JsonBackReference
    private Issue issue;

    @Setter
    @Column(name = "writerId")
    private String writerId;

    @Setter
    @Column(name = "content", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder
    public Comment(Issue issue, String writerId, String content, LocalDateTime createdAt) {
        this.issue = issue;
        this.writerId = writerId;
        this.content = content;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }
}
