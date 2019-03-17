package cz.fg.tempstatservice.utils;

import java.lang.reflect.Field;

public final class ReflectionUtils {

    /**
     * To prevent instantiation.
     */
    private ReflectionUtils() {
    }

    /**
     * Sets all fields of T entity which isn't null in object objFrom into same fields in object objTo.
     * @param objFrom source instance of Temperature entity.
     * @param objTo target instance of Temperature entity.
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
