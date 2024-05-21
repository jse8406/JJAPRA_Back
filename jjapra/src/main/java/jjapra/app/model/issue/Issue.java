package jjapra.app.model.issue;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issueId", updatable = false)
    private Integer issueId;

    @Column(name = "projectId")
    private Integer projectId;

    @Column(name = "title", length = 30)
    private String title;

    @Column(name = "description")
    private String description;

    @Setter
    @Column(name = "writer")
    private String writer;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Comment> comments;

    @Builder
    public Issue(Integer projectId, String title, String description, String writer, LocalDateTime createdAt, Priority priority, Status status){
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.writer = writer;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.priority = priority != null ? priority : Priority.MAJOR;
        this.status = status != null ? status : Status.NEW;
    }

}
