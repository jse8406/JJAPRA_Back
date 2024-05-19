package jjapra.app.dto;

import jjapra.app.model.Project;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddProjectRequest {

//    private Integer id;
    private String title;
    private String description;

    public Project toEntity(){
        return Project.builder()
//                .id(id)
                .title(title)
                .description(description)
                .build();
    }
}
