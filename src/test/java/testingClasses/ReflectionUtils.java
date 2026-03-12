package testingClasses;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class ReflectionUtils {
    public static Object invokePrivate(Class<?> clazz, String name,
                                       Class<?>[] paramTypes, Object... params) throws NoSuchMethodException,
                                                                                       InvocationTargetException,
                                                                                       IllegalAccessException {
        Method method = clazz.getDeclaredMethod(name, paramTypes);
        method.setAccessible(true);
        Object instance = Modifier.isStatic(method.getModifiers()) ? null : clazz;
        return method.invoke(instance, params);
    }
    
    public static Object invokePrivate(Object target, String name,
                                       Class<?>[] paramTypes, Object... params) throws NoSuchMethodException,
                                                                                       InvocationTargetException,
                                                                                       IllegalAccessException {
        Method method = target.getClass().getDeclaredMethod(name, paramTypes);
        method.setAccessible(true);
        return method.invoke(target, params);
    }
    
    public static Object getPrivate(Class<?> clazz, String name) throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(clazz);
    }
    
    public static Object getPrivate(Object target, String name) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }
    
    public static void setPrivate(Class<?> clazz, String name, Object newValue) throws NoSuchFieldException,
                                                                                       IllegalAccessException {
        Field field = clazz.getDeclaredField(name);
        Object instance = Modifier.isStatic(field.getModifiers()) ? null : clazz;
        if (!field.canAccess(instance))
            field.setAccessible(true);
        field.set(instance, newValue);
    }
    
    public static void setPrivate(Object target, String name, Object newValue) throws NoSuchFieldException,
                                                                                      IllegalAccessException {
        Field field = target.getClass().getDeclaredField(name);
        if (!field.canAccess(target))
            field.setAccessible(true);
        field.set(target, newValue);
    }
}
