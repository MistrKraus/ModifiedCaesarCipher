import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class TextManager {

    /**Oddělovací znak*/
    private String delimiter = " ";
    /**List slov na k zašifrování*/
    private List<Word> words;

//    /**Modifikovaná Caesarova šifra*/
//    private static final ModCaesarCipher modCaesarCipher = new ModCaesarCipher();

    /**
     * Zpracuje výchozí nezašifrovaný text
     *
     * @param plaintText nezašifrovaný text
     * @return text připravený k zašifrování
     * */
    public String processPlaintText(String plaintText) {
        words = new ArrayList<>();
        System.out.println("---");

        String[] splitedWords = plaintText.toUpperCase().replaceAll("[^A-Z" + delimiter + "]", "")
                .replaceAll(delimiter + "+", delimiter).split(delimiter);

        for (String splitedWord : splitedWords) {
            Word newWord = new Word(splitedWord);

            this.words.add(newWord);
            System.out.println("- " + splitedWord);
            newWord.setPossibleForms();
        }

        return getDecryptedText();
    }

    public void encryptWord(int wordId, int key) {
        words.get(wordId).encryptByKey(key);
    }

    /**
     * Vrací nezašifrovaný text, připravený k zašifrování
     *
     * @return text přiravený k zašifrování
     */
    public String getDecryptedText() {
        StringBuilder text = new StringBuilder();

        for (Word word : this.words)
            text.append(word.getDecryptedWord()).append(" ");

        return text.substring(0, text.length() - 1);
    }

    /**
     * Vrací zašifrovaný text
     *
     * @return zašifrovaný text
     */
    public String getEncryptedText() {
        StringBuilder text = new StringBuilder();

        for (Word word : this.words)
            text.append(word.getEncryptedWord()).append(" ");

        return text.substring(0, text.length() - 1);
    }


    /**
     * Vrací list nezašifrovaných slov
     *
     * @return list nezašifrovaných slov
     */
    public ObservableList<String> getDecryptedWords() {
        ObservableList<String> output = FXCollections.observableArrayList();
        for (Word word : this.words)
            output.add(word.getDecryptedWord());

        return output;
    }

    /**
     * Vrací veškeré možnosti zašifrování daného slova
     *
     * @param word slovo, jehož šifrované verze vrátí
     * @return veškeré možnosti zašifrování daného slova
     */
    public ObservableList<String> getEncryptedForms(Word word) {
        return word.getPossibileForms();
    }

    /**
     * Vrací veškeré možnosti zašifrování daného slova
     *
     * @param id index slova v listu
     * @return veškeré možnosti zašifrování daného slova
     */
    public ObservableList<String> getEncryptedForms(int id) {
        return words.get(id).getPossibileForms();
    }

    public List<Integer> getKeys() {
        List<Integer> keys = new ArrayList<>();

        for (Word word : words)
            keys.add(word.getKey());

        return keys;
    }

    public int getWordKey(int id) {
        return words.get(id).getKey();
    }

    /**
     * Vrací list slov
     *
     * @return list slov
     */
    public List<Word> getWords() {
        return words;
    }
}
