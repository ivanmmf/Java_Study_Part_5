package com.example.java_study_part_5.utils;
import lombok.experimental.UtilityClass;
import java.util.Optional;

@UtilityClass
public class AnnotationUtils {

    public static String fieldDescription(Class<?> cls, String field) {
        try {
            return Optional.of(cls.getDeclaredField(field))
                    .map(it -> it.getName())
                    .get();
        } catch (Exception e) {
            return null;
        }
    }

}

