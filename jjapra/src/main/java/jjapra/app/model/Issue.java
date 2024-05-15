package jjapra.app.model;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
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

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "status")
    private String status;

    @Builder
    public Issue(Integer projectId, String title, String description, LocalDateTime createdAt, Integer priority, String status){
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.priority = priority;
        this.status = status;
    }

    public void update(String status){
        this.status = status;
    }

}
