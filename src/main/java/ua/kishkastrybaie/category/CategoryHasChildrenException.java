package ua.kishkastrybaie.category;

public class CategoryHasChildrenException extends RuntimeException {
    public CategoryHasChildrenException(Long id) {
        super("Category with id " + id + " has children");
    }
}
