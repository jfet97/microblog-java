package Tests;

import MicroBlog.Utils.RandomIdGenerator;

public class RandomIdGenerator_TEST {
    public static void main(String[] args) {

        try {

            String id = RandomIdGenerator.generateId();

            if (RandomIdGenerator.isIdAvailable(id)) {
                throw new Exception("A generated id shouldn't be available");
            }

            String id2 = RandomIdGenerator.generateId(13);

            if (id2.length() != 13) {
                throw new Exception("Wrong length");
            }

        } catch (Exception e) {
            System.out.println("Something is wrong :(");
            System.out.println(e.toString());
        }

    }
}
