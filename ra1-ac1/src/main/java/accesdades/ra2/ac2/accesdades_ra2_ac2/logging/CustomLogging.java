package accesdades.ra2.ac2.accesdades_ra2_ac2.logging;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

// Gestió de logs
@Component
public class CustomLogging {

    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FILE_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String LOGS_DIR = "logs";
    private static final String PREFIX = "aplicacio-";

    private synchronized void writeLine(String level, String className, String methodName, String description) {
        try {
            Path logsDir = Paths.get(LOGS_DIR);
            if (Files.notExists(logsDir)) {
                Files.createDirectories(logsDir);
            }

            String fileName = PREFIX + LocalDate.now().format(FILE_DATE_FORMAT) + ".log";
            Path logFile = logsDir.resolve(fileName);

            String dateTime = LocalDateTime.now().format(DATE_TIME_FORMAT);
            String line = String.format("[%s] %s - %s - %s - %s", dateTime, level, className, methodName, description);

            try (BufferedWriter bw = Files.newBufferedWriter(logFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            // No more actions
        }
    }

    public void info(String className, String methodName, String description) {
        writeLine("INFO", className, methodName, description);
    }

    public void error(String className, String methodName, String description) {
        writeLine("ERROR", className, methodName, description);
    }
}
