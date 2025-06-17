package net.ubn.td.util;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

public class UuidV7Generator {
    private static final SecureRandom random = new SecureRandom();

    public static UUID generate() {
        long unixMillis = Instant.now().toEpochMilli();
        long msb = (unixMillis << 16) & 0xffffffffffff0000L;
        msb |= 0x7000; // version 7
        msb |= random.nextInt(1 << 12);

        long lsb = random.nextLong();
        lsb = (lsb & 0x3fffffffffffffffL) | 0x8000000000000000L; // variant 2
        return new UUID(msb, lsb);
    }
}
