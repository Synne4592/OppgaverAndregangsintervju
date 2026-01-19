package oppgaver;

import java.util.ArrayList;
import java.util.List;

public class CidrUtil {

    public static List<String> hostsToScan(String cidr) {
        String[] parts = cidr.trim().split("/");
        String baseIp = parts[0];
        int prefix = Integer.parseInt(parts[1]);

        long base = ipv4ToLong(baseIp);
        int hostBits = 32 - prefix;
        long total = 1L << hostBits;

        long first, last;
        if (prefix <= 30) {
            first = base + 1;
            last  = base + total - 2;
        } else if (prefix == 31) {
            first = base;
            last  = base + 1;
        } else {
            first = base;
            last  = base;
        }

        List<String> out = new ArrayList<>();
        for (long x = first; x <= last; x++) {
            out.add(longToIpv4(x));
        }
        return out;
    }

    private static long ipv4ToLong(String ip) {
        String[] o = ip.split("\\.");
        return (Long.parseLong(o[0]) << 24)
                | (Long.parseLong(o[1]) << 16)
                | (Long.parseLong(o[2]) << 8)
                | Long.parseLong(o[3]);
    }

    private static String longToIpv4(long v) {
        return ((v >> 24) & 255) + "." +
                ((v >> 16) & 255) + "." +
                ((v >> 8) & 255) + "." +
                (v & 255);
    }
}
