package ru.otus.runner;

import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.regex.Pattern;

/**
 * Created by nkargin on 03.03.2018.
 * hei@spark-mail.ru
 */
public class PackageTestRunner implements TestRunner {

    private String packageName;
    private Pattern pattern = Pattern.compile("^.*Test.*$");

    public static PackageTestRunner getRunnerFor(String packageName) {
        PackageTestRunner packageTestRunner = new PackageTestRunner();
        packageTestRunner.packageName = packageName;
        return packageTestRunner;
    }

    @Override
    public void invoke() {
        invokeAllTestClassesInPackage();
    }

    private void invokeAllTestClassesInPackage() {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        reflections.getSubTypesOf(Object.class)
                .stream()
                .map(aClass -> Pair.of(aClass, aClass.getName()))
                .filter(pair -> pattern.asPredicate().test(pair.getRight()))
                .map(Pair::getLeft)
                .map(ClassTestRunner::getRunnerFor)
                .forEach(ClassTestRunner::invoke);
    }
}
