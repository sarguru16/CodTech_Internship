package FileHandlingUtility; // Importing from own folder name as package
import java.io.*;  // Importing necessary classes for file handling
import java.nio.file.*;  // Importing classes for file operations using NIO
import java.util.List;  // Importing List to store file content
import java.util.stream.Collectors;  // Importing Collectors for stream operations

public class FileHandlingUtility {

    private static final String FILE_PATH = "sample.txt";  // Defining the file path as a constant

    public static void main(String[] args) {
        try {
            // Writing content to the file
            writeFile("Hello, this is a sample text file.\nLet's modify it later!");
            
            // Reading and displaying file content
            readFile();
            
            // Modifying the file content by replacing "sample" with "updated sample"
            modifyFile("sample", "updated sample");
            
            // Reading and displaying updated file content
            readFile();
        } catch (IOException e) {
            // Handling any file-related exceptions
            System.err.println("Error handling file: " + e.getMessage());
        }
    }

    /**
     * Writes content to the file.
     * If the file already exists, it overwrites the content.
     * 
     * @param content The text content to write to the file.
     * @throws IOException If an I/O error occurs.
     */
    public static void writeFile(String content) throws IOException {
        Files.write(Paths.get(FILE_PATH), content.getBytes(), 
                    StandardOpenOption.CREATE,  // Create file if it doesn't exist
                    StandardOpenOption.TRUNCATE_EXISTING);  // Overwrite existing content
        System.out.println("File written successfully.");
    }

    /**
     * Reads the content of the file and prints it to the console.
     * 
     * @throws IOException If an I/O error occurs.
     */
    public static void readFile() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));  // Reading all lines from the file
        System.out.println("File Contents:");
        lines.forEach(System.out::println);  // Printing each line of the file
    }

    /**
     * Modifies the file content by replacing occurrences of a target word with a replacement word.
     * 
     * @param target The word to be replaced.
     * @param replacement The new word to replace the target word.
     * @throws IOException If an I/O error occurs.
     */
    public static void modifyFile(String target, String replacement) throws IOException {
        Path path = Paths.get(FILE_PATH);
        
        // Reading all lines, replacing target word, and collecting updated lines
        List<String> updatedLines = Files.readAllLines(path)
                .stream()
                .map(line -> line.replace(target, replacement))  // Replacing occurrences of the target word
                .collect(Collectors.toList());
        
        // Writing the modified content back to the file
        Files.write(path, updatedLines, 
                    StandardOpenOption.WRITE, 
                    StandardOpenOption.TRUNCATE_EXISTING);  // Overwriting the existing content
        
        System.out.println("File modified successfully.");
    }
}
