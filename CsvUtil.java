package repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CsvUtil (Plain Java, no external libraries)
 * - Reads CSV files line-by-line.
 * - Splits by comma (assumes no commas inside values).
 * - Provides writeAll() to overwrite file after updates/deletes.
 */
public final class CsvUtil {

    private CsvUtil() {
        // Utility class: prevent instantiation
    }

    public static List<String[]> readAll(Path csvPath) throws IOException {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(csvPath)) {
            String line;
            while ((line = br.readLine()) != null) {
                rows.add(line.split(",", -1));
            }
        }

        return rows;
    }

    /**
     * Appends a single row to an existing CSV file.
     * Assumes the file already exists and has a header.
     */
    public static void appendRow(Path csvPath, String[] row) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(
                csvPath,
                StandardOpenOption.APPEND
        )) {
            bw.newLine();
            bw.write(String.join(",", row));
        }
    }


    /**
     * Overwrites the CSV file with the given rows.
     * Row 0 should be the header row.
     */
    public static void writeAll(Path csvPath, List<String[]> rows) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(
                csvPath,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.CREATE
        )) {
            for (int i = 0; i < rows.size(); i++) {
                bw.write(String.join(",", rows.get(i)));
                if (i < rows.size() - 1) bw.newLine();
            }
        }
    }
}
