package ru.otus.runner;

import ru.otus.annotation.After;
import ru.otus.annotation.Before;
import ru.otus.annotation.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ClassTestRunner implements TestRunner {

    private static final Class<? extends Annotation> BEFORE = Before.class;
    private static final Class<? extends Annotation> AFTER = After.class;

    private Method beforeMethod;
    private Method afterMethod;
    private List<Method> testMethods;

    private Class<?> testClass;

    public static ClassTestRunner getRunnerFor(String string) {
        try {
            Class<?> aClass = Class.forName(string);
            return getRunnerFor(aClass);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("No test class found with given name.");
        }
    }

    public static ClassTestRunner getRunnerFor(Class<?> clazz) {
        return new ClassTestRunner(clazz);
    }

    @Override
    public void invoke() {
        try {
            Object testClassInstance = testClass.newInstance();
            for (Method testMethod : testMethods) {
                invokeAroundMethod(beforeMethod, testClassInstance);
                testMethod.invoke(testClassInstance);
                invokeAroundMethod(afterMethod, testClassInstance);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private ClassTestRunner(Class<?> clazz) {
        testClass = clazz;
        beforeMethod = getAroundMethod(BEFORE);
        afterMethod = getAroundMethod(AFTER);

        testMethods = getTestMethods();
    }

    private void invokeAroundMethod(Method method, Object testClassInstance)
            throws IllegalAccessException, InvocationTargetException {
        if (Objects.nonNull(method)) {
            method.invoke(testClassInstance);
        }
    }

    private Method getAroundMethod(Class<? extends Annotation> annotationType) {
        List<Method> beforeMethods = new ArrayList<>();
        Method[] methods = testClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotationType)) {
                beforeMethods.add(method);
            }
        }

        if (beforeMethods.size() > 1) {
            throw new IllegalArgumentException("Only one @Before-annotated method have to exist.");
        }

        return beforeMethods.isEmpty() ? null : beforeMethods.get(0);
    }

    private List<Method> getTestMethods() {
        List<Method> testMethods = new ArrayList<>();
        Method[] methods = testClass.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }

        return testMethods;
    }
}
