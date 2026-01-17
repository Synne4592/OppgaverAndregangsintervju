package oppgaver;

public class Oppgave1 {

    public static void run() throws Exception {
        FirstNameIndex index = FirstNameIndex.load();

        RockyouReader.testRead(index);
    }
}
