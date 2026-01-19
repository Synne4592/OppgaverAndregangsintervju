package oppgaver;

import java.util.List;

public class Oppgave4 {

    public static void run() throws Exception {
        String cidr = "10.0.0.0/29";
        List<String> ips = CidrUtil.hostsToScan(cidr);
        int n = ips.size();

        if (n == 0) {
            throw new IllegalStateException("Ingen IP-adresser å scanne");
        }

        int lastPos1Based = lastScannedPosition1Based(n);

        String lastIp = ips.get(lastPos1Based - 1);

        System.out.println("Siste scannet IP er: " + lastIp);
    }

    private static int lastScannedPosition1Based(int n) {
        int start = 2;
        int k = 2;

        int posStandard = josephus(n, k);
        return rotate1based(posStandard, n, start);
    }

    private static int rotate1based(int pos1Based, int n, int start1Based) {
        return ((pos1Based + start1Based - 2) % n) + 1;
    }

    static int josephus(int n, int k) {
        int res0 = 0;
        for (int i = 2; i <= n; i++) {
            res0 = (res0 + k) % i;
        }
        return res0 + 1;
    }
}

