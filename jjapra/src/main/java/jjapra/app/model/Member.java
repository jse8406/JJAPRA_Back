package jjapra.app.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Member {
    @Id
    @Column(nullable = false, length = 30)
    private String id;
    @Column(nullable = false, length = 30)
    private String password;
    @Column(nullable = false, length = 30)
    private String name;
    @Column()
    private String email;
    @Column(nullable = false, length = 30)
    private String phone_num;
}
