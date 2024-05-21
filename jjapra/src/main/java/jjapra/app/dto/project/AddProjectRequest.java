package jjapra.app.dto.project;

import jjapra.app.model.project.Project;
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
