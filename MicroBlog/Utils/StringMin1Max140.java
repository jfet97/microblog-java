package MicroBlog.Utils;

import java.util.Optional;

public class StringMin1Max140 {
    // AF = <str[0], ..., str[str.length - 1]>
    // IR = str != null && 0 < str.length <= 140


    // la stringa lunga almeno un carattere e al più 140
    private String str;


    /*
     * MODIFIES: this
     * EFFECTS: inizializza la variabile d'istanza str
     * NOTE: un generico StringMin1Max140 non è direttamente istanziabile (costruttore privato)
     */
    private StringMin1Max140(String str) {
        this.str = str;
    }


    /*
     * REQUIRES: str != NULL && 0 < str.length() <= 140
     * EFFECTS: se la precondizione è rispettata crea un oggetto di tipo StringMin1Max140
     * NOTE: questo metodo statico permette di generare oggetti di tipo StringMin1Max140.
     *       A differenza del costruttore che, se reso pubblico, costringerebbe a lanciare delle eccezioni
     *       nel caso in cui la precondizione non fosse rispettata dato che il tipo di ritorno non è modificabile
     *       (essendo by default un'entità di tipo StringMin1Max140), un metodo statico permette di scegliere anche il tipo
     *       di ritorno, scelta che ricade ovviamente su un Optional<StringMin1Max140>.
     *       Possiamo vedere il metodo statico create come una mappa dal dominio delle String a quello degli Optional<StringMin1Max140>:
     *       - se le precondizioni sono soddisfatte, una String str è correttamente mappata in un Optional<StringMin1Max140> poiché esiste una StringMin1Max140
     *         avente lo stesso contenuto della str di partenza
     *       - se le precondizioni non sono soddisfatte, una String str è mappata nell'Optional.empty()
     *         poiché non possiede alcuna StringMin1Max140 corrispondente
     */
    public static Optional<StringMin1Max140> create(String str) {
        if (str == null || str.length() < 1 || str.length() > 140)
            return Optional.empty();
        else
            return Optional.of(new StringMin1Max140(str));
    }


    /*
     * EFFECTS: restituisce la variabile d'istanza str
     */
    public String read() {
        return this.str;
    }


    /*
     * EFFECTS: restituisce la variabile d'istanza str
     */
    public String toString() {
        return this.read();
    }


    /*
     * EFFECTS: ritorna true se o è di tipo StringMin1Max140 e possiede la stessa str del this, altrimenti ritorna false
     */
    public boolean equals(Object o) {
        if(o == null || !(o instanceof StringMin1Max140)) {
            return false;
        } else {
            return this.str.equals(((StringMin1Max140) o).str);
        }
    }

}
