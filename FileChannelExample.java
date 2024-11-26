import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class FileChannelExample {

    private static final String fileName = "file.txt";

    static void write() {
        Path filePath = Path.of(fileName);

        try (FileChannel fileChannel = FileChannel.open(
                filePath,
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE
        )) {
            Scanner scanner = new Scanner(System.in);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            System.out.println("Введите текст для записи в файл (пустая строка для выхода):");

            while (true) {
                String input = scanner.nextLine();
                // Проверка на пустую строку
                if (input.isEmpty()) {
                    break;
                }
                buffer.clear();
                buffer.put(input.getBytes());
                buffer.flip();
                while (buffer.hasRemaining()) {
                    fileChannel.write(buffer);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }

    static void read() {
        Path filePath = Path.of(fileName);

        // Проверка на существование файла перед чтением
        if (!Files.exists(filePath)) {
            System.err.println("Файл не найден: " + fileName);
            return;
        }

        try (FileChannel fileChannel = FileChannel.open(
                filePath,
                StandardOpenOption.READ
        )) {
            long fileSize = fileChannel.size();
            ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
            fileChannel.read(buffer);
            buffer.flip();
            System.out.println("Содержимое файла:");
            System.out.println(new String(buffer.array()));
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}