package com.dibya.programming.utils;

import java.lang.reflect.Field;

public class DtoUtils {

    public static void trimStrings(Object dto) {
        Field[] fields = dto.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(String.class)) {
                field.setAccessible(true);
                try {
                    String value = (String) field.get(dto);
                    if (value != null) {
                        field.set(dto, value.trim());
                    }
                } catch (IllegalAccessException ignored) {}
            }
        }
    }
}
