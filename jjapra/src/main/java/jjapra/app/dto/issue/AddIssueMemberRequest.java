package jjapra.app.dto.issue;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddIssueMemberRequest {
    private String id;
    private String role;
}
