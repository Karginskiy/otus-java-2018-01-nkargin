package ru.otus.homework_2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework_2.calculation.ObjectSizeCalculator;
import ru.otus.homework_2.calculation.ReflectionSizeCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

/**
 * Created by nkargin on 05.02.2018.
 * hei@spark-mail.ru
 *
 */
public class InstanceMeasurement {

    private static final Logger logger = LoggerFactory.getLogger(InstanceMeasurement.class);

    private ObjectSizeCalculator sizeCalculator = new ReflectionSizeCalculator();

    public Map<String, Long> measure(List<String> classNames) {
        List<Object> instances = getInstancesForClasses(getClassesForNames(classNames));
        return instances.stream()
                .collect(toMap(object -> object.getClass().getCanonicalName(), sizeCalculator::calculate));
    }

    private List<Class> getClassesForNames(List<String> classNames) {
        List<Class> classes = new ArrayList<>();

        for (String className : classNames) {
            Class<?> aClass;
            try {
                aClass = Class.forName(className);
                classes.add(aClass);
            } catch (ClassNotFoundException e) {
                logger.error("No class with name '{}' found at the runtime. Execution continuing.", className);
            }
        }

        return classes;
    }

    private List<Object> getInstancesForClasses(List<Class> classes) {
        List<Object> objects = new ArrayList<>();
        for (Class aClass : classes) {
            try {
                Object instance = aClass.newInstance();
                objects.add(instance);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return objects;
    }

}
