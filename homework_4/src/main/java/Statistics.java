import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by nkargin on 24.02.2018.
 * hei@spark-mail.ru
 */
public class Statistics {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("./homework_4/logs/gclog.log");
        Stream<String> lines = Files.lines(path);
        Stream<String> stringStream = lines.filter(s -> s.contains("Full") || s.contains("Allocation Failure"));

        stringStream.map(line -> {
            if (line.contains("Full")) {
                return "Full";
            } else {
                return "Young";
            }
        }).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .forEach((key, value) -> System.out.println(String.format("%s collections count: %d", key, value)));

        System.out.println();

        path = Paths.get("./homework_4/logs/gclog.log");
        lines = Files.lines(path);

        lines.filter(s -> s.contains("Full") || s.contains("Allocation Failure"))
                .map(line -> {
                    String group = "0.0";
                    if (line.contains("real=")) {
                        Pattern compile = Pattern.compile("real=([0-9]+\\.[0-9]+)");
                        Matcher matcher = compile.matcher(line);
                        if (matcher.find()) {
                            group = matcher.group(1);
                        }
                    }
                    return Pair.of(line.contains("Full") ? "Full (Major)" : "Young", Double.valueOf(group));
                })
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight, (firstTime, secondTime) -> firstTime + secondTime))
                .forEach((key, value) -> System.out.println(String.format("%s gcs spent %.2f seconds. ", key, value)));

    }
}
