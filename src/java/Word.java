import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Word {
    /**Hodnota klíče pro rozšifrovaný / nezašifrované slovo*/
    private static final int DEF_KEY = 0;
    /**Algoritmus pro šifrování*/
    private static final ModCaesarCipher modCaesarCipher = new ModCaesarCipher();

    /**Dešifrované slovo*/
    private String decryptedWord;
    /**Zašifrované slovo*/
    private String encryptedWord;

    /**Možné šifrování slova*/
    private ObservableList<String> possibilities = FXCollections.observableArrayList();

    /**Klíš  k zašifrování a dešifrování*/
    private int key;

//    /**Je slovo při vytváření jeho instance šifrované*/
//    private final boolean isCryptical;

    /**
     * Konstruktor pro nezašifrované slovo
     *
     * @param word slovo
     */
    public Word(String word) {
        this(word, DEF_KEY);
    }

    /**
     * Konstruktor pro šifrované slovo
     *
     * @param word šifrované slovo
     * @param key klíč
     */
    public Word(String word, int key) {
        this.key = key;

        if (key == DEF_KEY)
            this.decryptedWord = word;

        this.encryptedWord = word;

        //setPossibleForms();
    }

    /**
     * Uloží veškeré možné šifrování tohoto slova
     */
    public void setPossibleForms() {
        if (possibilities.size() > 0)
            return;

        this.possibilities = modCaesarCipher.encryptWord(this);
    }

    /**
     * Zašifruje slovo podle vloženého klíče a nastaví klíč
     *
     * @param key klíč k šifrování
     */
    public void encryptByKey(int key) {
        this.key = key;
        this.encryptedWord = possibilities.get(key);
    }

    /**
     * Nastaví klíč k rozšifrování slova uloženého jako encryptedWord
     *
     * @param key klíč k rozšifrování
     */
    public void setKey(int key) {
        this.key = key;
    }

    public void setDecryptedWord(String decryptedWord) {
        this.decryptedWord = decryptedWord;
    }

    public void setEncryptedWord(String encryptedWord) {
        this.encryptedWord = encryptedWord;
    }

    /**
     * Vrátí slovo k zašifrování
     *
     * @return slovo k zašifrování
     */
    public String getDecryptedWord() {
        if (decryptedWord != null)
            return decryptedWord;

        setPossibleForms();
        setDecryptedWord(possibilities.get(0));

        return this.decryptedWord;
//        if (possibilities.size() > 0) {
//            this.decryptedWord = possibilities.get(0);
//            return decryptedWord;
//        }
//
//        this.possibilities = modCaesarCipher.encryptWord(this);
//        this.decryptedWord = possibilities.get(0);
//
//        return decryptedWord;
    }

    /**
     * Vrátí zašifrované slovo
     *
     * @return zašifrované slovo
     */
    public String getEncryptedWord() {
        if (encryptedWord != null)
            return encryptedWord;

        return "-ERROR-";
//        if (possibilities.size() > 0) {
//            this.encryptedWord = possibilities.get(key);
//            return encryptedWord;
//        }
//
//        this.possibilities = modCaesarCipher.encryptWord(this);
//        this.encryptedWord = possibilities.get(key);
//
//        return encryptedWord;
    }

    /**
     * Vrátí klíč pro zašifrované slovo
     *
     * @return klíč pro zašifrované slovo
     */
    public int getKey() {
        return key;
    }

    /**
     * Vrátí list možných šifrování slova
     *
     * @return list možných šifrování slova
     */
    public ObservableList<String> getPossibileForms() {
        if (possibilities.size() > 0)
            return possibilities;

        setPossibleForms();

        return possibilities;
    }

    @Override
    public String toString() {
        return String.format("%s-%s[%d]", decryptedWord, encryptedWord, key);
    }

    public String getWord() {
        if (key == 0)
            return decryptedWord;

        return encryptedWord;
    }

    public boolean decrypt() {
        if (key < 0)
            return false;

        decryptedWord = modCaesarCipher.decryptWord(encryptedWord, key);

        return true;
    }
}