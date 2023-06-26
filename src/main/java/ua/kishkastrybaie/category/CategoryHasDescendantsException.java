package ua.kishkastrybaie.category;

public class CategoryHasDescendantsException extends RuntimeException {
    public CategoryHasDescendantsException(Long id) {
        super("Category with id " + id + " has descendants");
    }
}
