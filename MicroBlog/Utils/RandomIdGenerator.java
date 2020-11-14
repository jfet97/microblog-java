package MicroBlog.Utils;

import java.util.HashSet;

public final class RandomIdGenerator {
    // OVERVIEW: generatore di id univoci

    // non è instanziabile
    private RandomIdGenerator() {}


    // generatedIds tiene traccia degli id già generati
    private static HashSet<String> generatedIds = new HashSet<String>();


    /*
     * REQUIRES: id != NULL
     * THROWS: se id == NULL lancia una NullPointerException (eccezione unchecked)
     * EFFECTS: restituisce true se id è disponibile (non generato in precedenza)
     */
    public static boolean isIdAvailable(String id) {
        if(id == null) throw new NullPointerException();
        else return !RandomIdGenerator.generatedIds.contains(id);
    }


    /*
     * MODIFIES: RandomIdGenerator.generatedIds
     * EFFECTS: genera un id univoco lungo 20 caratteri numerici
     */
    public static String generateId() {
        return RandomIdGenerator.generateId(20);
    }


    /*
     * REQUIRES: len > 0
     * THROWS: se le <= 0 lancia una IllegalArgumentException (eccezione unchecked)
     * EFFECTS: genera un id univoco lungo len caratteri numerici
     */
    public static String generateId(int len) {
        if(len <= 0) {
            throw new IllegalArgumentException();
        } else {

            String id = "";
            for (int i = 0; i < len; i++) {
                Double randomDouble = Math.random() * 10;
                id += (new Integer(randomDouble.intValue())).toString();
            }

            // controllo se l'id appena generato fosse già utilizzato e in caso ne genero un altro
            if (RandomIdGenerator.generatedIds.contains(id)) {
                return RandomIdGenerator.generateId(len);
            } else {
                RandomIdGenerator.generatedIds.add(id);
                return id;
            }
            }
    }
}
