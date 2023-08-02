package ua.kishkastrybaie.shared;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Data
@EqualsAndHashCode(callSuper = false)
@Relation(itemRelation = "breadcrumb", collectionRelation = "breadcrumbs")
public class BreadCrumb extends RepresentationModel<BreadCrumb> {
  private final String name;
  private final String path;
  private final BreadCrumb descendant;
}
