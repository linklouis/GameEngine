package gameengine.threed.graphics.raytraceing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class KernelReader {
    public static String readKernelSource(String filePath) {
        StringBuilder source = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("oclcode/" + filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                source.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return source.toString();
    }

    /**
     * Reads OpenCL kernel code from several files into a single program
     * in the order they appear in the input.
     * <p></p>
     * By default, all file paths not containing the indented OCL code
     * directory ("oclcode") will have the directory appended to the front,
     * and all file paths not containing a '.' indicating a file type, will
     * have ".txt" appended to the end.
     * <p>
     * If the omission of the oclcode directory or file type is intentional,
     * include a '*' at the beginning or the end of the path, respectively.
     * <p></p>
     *
     * @param filePaths The paths to the OpenCL kernel files.
     * @return A single {@code String} containing the contents of all the files
     * in {@code filePaths}.
     */
    public static String readKernelSources(String... filePaths) {
        if (filePaths.length == 0) {
            return "";
        }

        filePaths = Arrays.stream(filePaths)
                .filter(path -> path != null && !path.isEmpty())
                .map(path -> {
                    if (!path.contains("oclcode/")) {
                        if (path.indexOf("*") == 0) {
                            path = path.substring(1);
                        } else {
                            path = "oclcode/" + path;
                        }
                    }

                    if (!path.contains(".")) {
                        if (path.indexOf("*") == path.length() - 1) {
                            path = path.substring(0, path.length() - 1);
                        } else {
                            path += ".txt";
                        }
                    }

                    return path;
                }).toArray(String[]::new);

        StringBuilder source = new StringBuilder();
        int i = 0;
        int lineNumber;
        for (String filePath : filePaths) {
            lineNumber = 0;
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    source.append(line).append("\n");
                    lineNumber++;
                }
            } catch (IOException e) {
                System.err.println("An IOException occurred while reading OpenCL kernels from files."
                        + "\nException: " + e.getClass().toString().substring(6)
                        + "\nRelevant file(index=" + i + ", line=" + lineNumber + "): " + filePath
                        + (source.isEmpty() ? "" : ("\n\nProcessed code:\n" + source))
                        + "\nAll source files: " + Arrays.toString(filePaths)
                        + (e.getCause() == null ? "" : ("\nCause: " + e.getCause()))
                        + "\n\nOccurred at: " + extractLocation(e)
                );
                System.exit(0);
            }
            i++;
        }
        return source.toString();
    }

    public static String extractLocation(IOException e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder locationInfo = new StringBuilder();

        for (StackTraceElement element : stackTrace) {
            if (!element.toString().contains("java.io")
                    && !element.toString().contains("QuantumToolkit")
                    && !element.toString().contains("com.sun.")) {
                locationInfo
                        .append("\n")
                        .append(element.getClassName())
                        .append(".")
                        .append(element.getMethodName())
                        .append("(")
                        .append(element.getFileName())
                        .append(":")
                        .append(element.getLineNumber())
                        .append(")");
            }
        }
        return locationInfo.isEmpty() ? "unknown location" : locationInfo.toString();
    }
}
