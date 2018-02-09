package ru.otus.homework_2.calculator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by nkargin on 09.02.2018.
 * hei@spark-mail.ru
 */
public class SerializationSizeCalculator implements ObjectSizeCalculator {

    private static final String CANNOT_CREATE = "Cannot create output stream";
    private static final Logger log = LoggerFactory.getLogger(SerializationSizeCalculator.class);
    private static final String SIZE_MESSAGE = "Serialization size of %s equal to %d";

    @Override
    public String getMessage() {
        return SIZE_MESSAGE;
    }

    @Override
    public long calculate(Object o) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();
            return byteArrayOutputStream.toByteArray().length;
        } catch (IOException e) {
            log.info(CANNOT_CREATE);
        }

        return 0L;
    }
}
