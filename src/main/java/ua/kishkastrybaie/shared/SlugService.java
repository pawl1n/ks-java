package ua.kishkastrybaie.shared;

import com.github.slugify.Slugify;

public interface SlugService {
  static String slugify(String text) {
    return Slugify.builder()
        .transliterator(true)
        .underscoreSeparator(true)
        .lowerCase(true)
        .build()
        .slugify(text);
  }
}
