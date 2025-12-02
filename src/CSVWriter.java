import java.io.*;
import java.nio.file.*;

public class CSVWriter {
    private final BufferedWriter bw;
    private boolean headerWritten = false;

    public CSVWriter(String outputPath) throws IOException {
        Path p = Paths.get(outputPath);
        if (!Files.exists(p.getParent())) Files.createDirectories(p.getParent());
        this.bw = Files.newBufferedWriter(p);
    }

    public void writeHeader() throws IOException {
        if (headerWritten) return;
        bw.write("file,method,run,ms,occurrences,size_kb,target\n");
        headerWritten = true;
    }

    public void writeLine(String file, String method, int run, long ms, int occ, long sizeKb, String target) throws IOException {
        bw.write(String.format("%s,%s,%d,%d,%d,%d,%s\n", escape(file), method, run, ms, occ, sizeKb, escape(target)));
    }

    private String escape(String s) {
        return s.replace("\n", " ").replace(',', '_');
    }

    public void flush() throws IOException { bw.flush(); }
    public void close() throws IOException { bw.close(); }
}
