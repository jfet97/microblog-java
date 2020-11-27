package Tests;

import java.util.Optional;

import MicroBlog.Utils.StringMin1Max140;

public class StringMin1Max140_TEST {
    public static void main(String[] args) {

        try {

            // creo tre stringhe di cui solo una è della lunghezza corretta
            // 0
            String emptyString = "";
            // 141
            String tooLongString = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa cum sociis natoque penatibus et dis.";
            // [1, 140]
            String okString = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit.";

            // tento di creare tre istanze di StringMin1Max140
            Optional<StringMin1Max140> maybeString1 = StringMin1Max140.create(emptyString);
            Optional<StringMin1Max140> maybeString2 = StringMin1Max140.create(tooLongString);
            Optional<StringMin1Max140> maybeString3 = StringMin1Max140.create(okString);

            // la prima stringa non ha i requisiti necessari e non deve essere mappata in una StringMin1Max140
            if(maybeString1.isPresent()) {
                throw new Exception("A string with less that 1 characters should not be mapped into a StringMin1Max140");
            }

            // la seconda stringa non ha i requisiti necessari e non deve essere mappata in una StringMin1Max140
            if(maybeString2.isPresent()) {
                throw new Exception("A string with more that 140 characters should not be mapped into a StringMin1Max140");
            }

            // la terza stringa ha i requisiti necessari e deve essere mappata in una StringMin1Max140
            if(!maybeString3.isPresent()) {
                throw new Exception("A string with more that 0 characters and less tahn 141 should be mapped into a StringMin1Max140");
            }

            StringMin1Max140 string3 = maybeString3.get();

            // controllo inizializzazione StringMin1Max140
            if(!string3.equals(string3)) {
                throw new Exception("equals() does not work properly");
            }
            if(!string3.read().equals(okString)) {
                throw new Exception("read() does not work properly");
            }

            System.out.println("StringMin1Max140: OK");


        } catch (Exception e) {
            System.out.println("Something is wrong :(");
            System.out.println(e.toString());
        }

    }
}
