package net.ccbluex.liquidbounce.utils.reflect;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

public class ReflectionUtils {

    public static Object getFieldValue(Object obj, String... fields) {
        Class<?> clazz = obj.getClass();
        for (String string : fields) {
            try {
                Field f = clazz.getDeclaredField(string);
                f.setAccessible(true);
                try {
                    return f.get(obj);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }
            } catch (NoSuchFieldException ignored) {
            }
        }
        return null;
    }

    public static void letMeAccess(AccessibleObject object) {
        object.setAccessible(true);
    }


    public static Object getFieldValue(Class<?> clazz, String... fields) {
        for (String string : fields) {
            try {
                Field f = clazz.getDeclaredField(string);
                if (f == null)
                    continue;
                f.setAccessible(true);
                try {
                    return f.get(null);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }
            } catch (NoSuchFieldException e) {
                continue;
            }
        }
        return null;
    }

    public static void setFieldValue(Object obj, Object value, String... fields) {
        Class<?> clazz = obj.getClass();
        for (String string : fields) {
            try {
                Field f = clazz.getDeclaredField(string);
                if (f == null)
                    continue;
                f.setAccessible(true);
                try {
                    f.set(obj, value);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return;
                }
            } catch (NoSuchFieldException e) {
                continue;
            }
        }
    }

    public static void setFieldValue(Class<?> clazz, Object value, String... fields) {
        for (String string : fields) {
            try {
                Field f = clazz.getDeclaredField(string);
                if (f == null)
                    continue;
                f.setAccessible(true);
                try {
                    f.set(null, value);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } catch (NoSuchFieldException e) {
                continue;
            }
        }
    }

    public static <T> T copy(T src, T dst) {
        for (Field f : getAllFields(src.getClass())) {
            if (Modifier.isFinal(f.getModifiers()) || Modifier.isStatic(f.getModifiers()))
                continue;
            f.setAccessible(true);
            try {
                f.set(dst, f.get(src));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return dst;
    }

    public static Field[] getAllFields(Class<?> clazz) {
        ArrayList<Field> fields = new ArrayList<>();
        do {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        } while (clazz != Object.class && clazz != null);
        return fields.toArray(new Field[0]);
    }
}
