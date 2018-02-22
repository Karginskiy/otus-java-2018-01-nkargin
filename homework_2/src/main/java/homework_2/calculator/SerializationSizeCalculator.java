package homework_2.calculator;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Created by nkargin on 09.02.2018.
 * hei@spark-mail.ru
 */
public class SerializationSizeCalculator implements ObjectSizeCalculator {

    private static final int OBJECTS_COUNT = 1000000;
    private static final String CANNOT_CREATE = "Cannot create output stream";
    private static final Logger log = LoggerFactory.getLogger(SerializationSizeCalculator.class);
    private static final String SIZE_MESSAGE = "Serialization size of %s equal to %d";
    private static final String PROBLEM_DURING_SERIALIZATION = "Problem during object serialization";

    @Override
    public String getMessage() {
        return SIZE_MESSAGE + getUnits();
    }

    @Override
    public long calculate(Object o) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            IntStream.range(0, OBJECTS_COUNT)
                    .mapToObj(val -> ObjectUtils.cloneIfPossible(o))
                    .filter(Objects::nonNull)
                    .forEach(obj -> {
                        try {
                            objectOutputStream.writeObject(obj);
                        } catch (IOException e) {
                            log.info(PROBLEM_DURING_SERIALIZATION);
                        }
                    });
        } catch (IOException e) {
            log.info(CANNOT_CREATE);
        }

        return byteArrayOutputStream.toByteArray().length / OBJECTS_COUNT;
    }
}
