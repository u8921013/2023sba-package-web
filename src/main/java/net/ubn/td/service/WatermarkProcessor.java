package net.ubn.td.service;

import java.io.IOException;
import java.nio.file.Path;

public interface WatermarkProcessor {
    Path applyWatermark(Path input, String text) throws IOException;
}
