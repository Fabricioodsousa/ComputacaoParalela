import java.nio.file.*;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            Path samplesDir = Paths.get("amostras");
            if (!Files.exists(samplesDir) || !Files.isDirectory(samplesDir)) {
                System.out.println("Pasta amostras/ não encontrada. Crie e coloque arquivos .txt.");
                return;
            }

            Path outDir = Paths.get("resultados");
            if (!Files.exists(outDir)) Files.createDirectories(outDir);
            Path csv = outDir.resolve("resultados.csv");

            CSVWriter writer = new CSVWriter(csv.toString());
            writer.writeHeader();

            List<Path> files = Files.list(samplesDir)
                    .filter(p -> p.toString().toLowerCase().endsWith(".txt"))
                    .sorted()
                    .toList();

            if (files.isEmpty()) {
                System.out.println("Nenhum .txt em amostras/");
                return;
            }

            String target = (args.length >= 1) ? args[0] : "the";

            int maxProcs = Runtime.getRuntime().availableProcessors();
            List<Integer> threadTests = List.of(1, Math.max(1, Math.min(2, maxProcs)), Math.max(1, Math.min(4, maxProcs)), maxProcs);
            LinkedHashSet<Integer> set = new LinkedHashSet<>(threadTests);
            List<Integer> threadsToTest = new ArrayList<>(set);

            int repetitions = 3;

            try {
                ParallelGPU.initOnceIfNeeded();
            } catch (Throwable t) {
                System.out.println("Aviso: GPU não inicializada: " + t.getMessage());
            }

            for (Path file : files) {
                String fileName = file.getFileName().toString();
                String text = Files.readString(file);
                long fileSizeKb = Files.size(file) / 1024;

                for (int r = 1; r <= repetitions; r++) {
                    long t1 = System.nanoTime();
                    int occ = SerialCPU.countWordSerial(text, target);
                    long t2 = System.nanoTime();
                    long ms = (t2 - t1) / 1_000_000;
                    writer.writeLine(fileName, "SerialCPU", r, ms, occ, fileSizeKb, target);
                }

                for (int th : threadsToTest) {
                    for (int r = 1; r <= repetitions; r++) {
                        long t1 = System.nanoTime();
                        int occ = ParallelCPU.countWordParallel(text, target, th);
                        long t2 = System.nanoTime();
                        long ms = (t2 - t1) / 1_000_000;
                        writer.writeLine(fileName, "ParallelCPU("+th+")", r, ms, occ, fileSizeKb, target);
                    }
                }

                try {
                    ParallelGPU.countGPUWarmup(text, target);

                    for (int r = 1; r <= repetitions; r++) {
                        long t1 = System.nanoTime();
                        int occ = ParallelGPU.countGPU(text, target);
                        long t2 = System.nanoTime();
                        long ms = (t2 - t1) / 1_000_000;
                        writer.writeLine(fileName, "ParallelGPU", r, ms, occ, fileSizeKb, target);
                    }
                } catch (Throwable t) {
                    System.out.println("GPU falhou para arquivo " + fileName + ": " + t.getMessage());
                    writer.writeLine(fileName, "ParallelGPU", 0, -1, -1, fileSizeKb, target);
                }

                writer.flush();
            }

            System.out.println("Benchmark finalizado. CSV gravado em: " + csv.toAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
