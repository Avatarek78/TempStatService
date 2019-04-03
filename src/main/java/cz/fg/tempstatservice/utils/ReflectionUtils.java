package cz.fg.tempstatservice.utils;

import java.lang.reflect.Field;

/**
 * Helper utilities for reflection.
 */
public final class ReflectionUtils {

    /**
     * To prevent instantiation.
     */
    private ReflectionUtils() {
    }

    /**
     * Sets all fields of T entity which isn't null in objFrom into same fields in objTo.
     * @param objFrom source instance of object.
     * @param objTo target instance of object.
     */
    public static <T> void mergeNotNullFields(T objFrom, T objTo) {
        for (Field field : objFrom.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object valueFrom = field.get(objFrom);
                if (valueFrom != null) {
                    field.set(objTo, valueFrom);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
