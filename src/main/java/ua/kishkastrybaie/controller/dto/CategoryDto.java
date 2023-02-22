package ua.kishkastrybaie.controller.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Relation(itemRelation = "category", collectionRelation = "categories")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto extends RepresentationModel<CategoryDto> {
    private Long id;
    private String name;
    private CategoryDto parentCategory;
}
