package Tests;

import MicroBlog.Utils.RandomIdGenerator;

public class RandomIdGenerator_TEST {
    public static void main(String[] args) {

        try {

            // genero un id
            String id = RandomIdGenerator.generateId();

            // controllo del sistema di univocità
            if (RandomIdGenerator.isIdAvailable(id)) {
                throw new Exception("A generated id shouldn't be available");
            }

            // creo un id lungo 13 caratteri
            String id2 = RandomIdGenerator.generateId(13);

            // controlla la lunghezza
            if (id2.length() != 13) {
                throw new Exception("Wrong length");
            }

            System.out.println("RandomIdGenerator: OK");

        } catch (Exception e) {
            System.out.println("Something is wrong :(");
            System.out.println(e.toString());
        }

    }
}
