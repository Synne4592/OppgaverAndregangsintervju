package oppgaver;

import java.nio.file.Path;

public class Oppgave2 {

    public static void run() throws Exception {
        FileStore store = new FileStore(
                Path.of("passordFil.txt"),
                Path.of("midlertidigFil.txt"),
                Path.of("hashVault.txt")
        );

        store.clear(store.midlertidigFil());
        store.clear(store.hashVault());

        int n = store.size(store.passordFil());

        hanoi(store, n, store.passordFil(), store.midlertidigFil(), store.hashVault());
    }

    private static void hanoi(FileStore store, int n, Path from, Path aux, Path to) throws Exception {
        if (n == 0) return;

        hanoi(store, n - 1, from, to, aux);
        store.move(from, to);
        hanoi(store, n - 1, aux, from, to);
    }


}
