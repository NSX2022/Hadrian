package testingClasses;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class ReflectionUtils {
    public static Object invokePrivateStatic(Class<?> clazz, String name,
                                             Class<?>[] paramTypes, Object... params) throws NoSuchMethodException {
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Method method = clazz.getDeclaredMethod(name, paramTypes);
                method.setAccessible(true);
                return method.invoke(null, params);
            } catch (NoSuchMethodException ignored) {} catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        
        throw new NoSuchMethodException(name);
    }
    
    public static Object invokePrivateInstance(Object target, String name,
                                               Class<?>[] paramTypes, Object... params) throws NoSuchMethodException {
        for (Class<?> c = target.getClass(); c != null; c = c.getSuperclass()) {
            try {
                Method method = c.getDeclaredMethod(name, paramTypes);
                method.setAccessible(true);
                return method.invoke(target, params);
            } catch (NoSuchMethodException ignored) {} catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        
        throw new NoSuchMethodException(name);
    }
    
    public static Object getPrivateStatic(Class<?> clazz, String name) throws NoSuchFieldException {
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(clazz);
            } catch (NoSuchFieldException ignored) {} catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        
        throw new NoSuchFieldException(name);
    }
    
    public static Object getPrivateInstance(Object target, String name) throws NoSuchFieldException {
        for (Class<?> c = target.getClass(); c != null; c = c.getSuperclass()) {
            try {
                Field field = c.getDeclaredField(name);
                field.setAccessible(true);
                return field.get(target);
            } catch (NoSuchFieldException ignored) {} catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        
        throw new NoSuchFieldException(name);
    }
    
    public static void setPrivateStatic(Class<?> clazz, String name, Object newValue) throws NoSuchFieldException {
        for (; clazz != null; clazz = clazz.getSuperclass()) {
            try {
                Field field = clazz.getDeclaredField(name);
                field.setAccessible(true);
                field.set(null, newValue);
                return;
            } catch (NoSuchFieldException ignored) {} catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        
        throw new NoSuchFieldException(name);
    }
    
    public static void setPrivateInstance(Object target, String name, Object newValue) throws NoSuchFieldException {
        for (Class<?> c = target.getClass(); c != null; c = c.getSuperclass()) {
            try {
                Field field = c.getDeclaredField(name);
                field.setAccessible(true);
                field.set(target, newValue);
                return;
            } catch (NoSuchFieldException ignored) {} catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        
        throw new NoSuchFieldException(name);
    }
}
