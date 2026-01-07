package service;

import java.util.List;

/**
 * IdGenerator:
 * - Generates the next ID based on existing IDs in the CSV.
 * - Works when IDs end with digits (e.g., A0001, A0002) or (APT0001, APT0002).
 *
 * Strategy:
 * - Find the maximum numeric suffix among existing IDs that share the same prefix.
 * - Return prefix + nextNumber padded to the same width (defaults to 4 if unknown).
 */
public final class IdGenerator {

    private IdGenerator() {}

    public static String nextId(List<String> existingIds) {
        if (existingIds == null || existingIds.isEmpty()) {
            return "A0001"; // fallback if file is empty (shouldn't happen)
        }

        // Use first ID to infer prefix and numeric width
        String sample = existingIds.get(0).trim();

        String prefix = extractPrefix(sample);
        int width = extractNumericWidth(sample);
        if (width <= 0) width = 4;

        int max = 0;
        for (String id : existingIds) {
            if (id == null) continue;
            id = id.trim();
            if (!id.startsWith(prefix)) continue;

            Integer n = extractNumericSuffix(id);
            if (n != null && n > max) max = n;
        }

        int next = max + 1;
        return prefix + String.format("%0" + width + "d", next);
    }

    private static String extractPrefix(String id) {
        // prefix = everything before the trailing digits
        int i = id.length() - 1;
        while (i >= 0 && Character.isDigit(id.charAt(i))) i--;
        return id.substring(0, i + 1);
    }

    private static int extractNumericWidth(String id) {
        int i = id.length() - 1;
        int count = 0;
        while (i >= 0 && Character.isDigit(id.charAt(i))) {
            count++;
            i--;
        }
        return count;
    }

    private static Integer extractNumericSuffix(String id) {
        int i = id.length() - 1;
        while (i >= 0 && Character.isDigit(id.charAt(i))) i--;
        String digits = id.substring(i + 1);
        if (digits.isEmpty()) return null;
        try {
            return Integer.parseInt(digits);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
