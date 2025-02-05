package ru.gb.lesson2.hw;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;

public class TestProcessor {

    /**
     * Данный метод находит все void методы без аргументов в классе, и запускеет их.
     * <p>
     * Для запуска создается тестовый объект с помощью конструткора без аргументов.
     */
    public static void runTest(Class<?> testClass) {
        final Constructor<?> declaredConstructor;
        try {
            declaredConstructor = testClass.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Для класса \"" + testClass.getName() + "\" не найден конструктор без аргументов");
        }

        final Object testObj;
        try {
            testObj = declaredConstructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось создать объект класса \"" + testClass.getName() + "\"");
        }

        List<Method> beforeEachMethods = getMethodsAnnotatedWith(testClass, BeforeEach.class);
        List<Method> afterEachMethods = getMethodsAnnotatedWith(testClass, AfterEach.class);

        List<Method> methods = Arrays.stream(testClass.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(Test.class) && !it.isAnnotationPresent(Skip.class))
                .peek(TestProcessor::checkTestMethod)
                .sorted(Comparator.comparingInt(it -> it.getAnnotation(Test.class).order()))
                .toList();

        methods.forEach(it -> {
            beforeEachMethods.forEach(x -> runTest(x, testObj));
            runTest(it, testObj);
            afterEachMethods.forEach(x -> runTest(x, testObj));
        });
    }

    private static <T extends Annotation> List<Method> getMethodsAnnotatedWith(Class<?> objClass,
                                                                               Class<T> annoClass) {
        return Arrays.stream(objClass.getDeclaredMethods())
                .filter(it -> it.isAnnotationPresent(annoClass))
                .toList();
    }

    private static void checkTestMethod(Method method) {
        if (!method.getReturnType().isAssignableFrom(void.class) || method.getParameterCount() != 0) {
            throw new IllegalArgumentException("Метод \"" + method.getName() + "\" должен быть void и не иметь аргументов");
        }
    }

    private static void runTest(Method testMethod, Object testObj) {
        try {
            testMethod.invoke(testObj);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось запустить тестовый метод \"" + testMethod.getName() + "\"");
        } catch (AssertionError e) {

        }
    }

}
