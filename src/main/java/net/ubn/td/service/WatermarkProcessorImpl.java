package net.ubn.td.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class WatermarkProcessorImpl implements WatermarkProcessor {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public Path applyWatermark(Path input, String text) throws IOException {
        String ext = StringUtils.getFilenameExtension(input.getFileName().toString());
        Path outDir = Paths.get(uploadDir, "watermarked");
        Files.createDirectories(outDir);
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        Path output = ext == null ? outDir.resolve(timestamp) : outDir.resolve(timestamp + "." + ext);

        if (ext != null && (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("png"))) {
            BufferedImage image = ImageIO.read(input.toFile());
            Graphics2D g2d = image.createGraphics();
            g2d.setFont(new Font("Arial", Font.BOLD, 36));
            g2d.setColor(new Color(255, 0, 0, 128));
            FontMetrics fm = g2d.getFontMetrics();
            int x = image.getWidth() - fm.stringWidth(text) - 10;
            int y = image.getHeight() - fm.getHeight();
            g2d.drawString(text, x, y);
            g2d.dispose();
            ImageIO.write(image, ext, output.toFile());
        } else {
            // For video or other types, simply copy the file
            Files.copy(input, output);
        }
        return output;
    }
}
