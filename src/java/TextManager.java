import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class TextManager {

    /**Oddělovací znak*/
    private String delimiter = " ";
    /**List slov k zašifrování*/
    private List<Word> wordsToEncrypt;
    /**List slov k dešifrování*/
    private List<Word> wordsToDecrypt;

//    /**Modifikovaná Caesarova šifra*/
//    private static final ModCaesarCipher modCaesarCipher = new ModCaesarCipher();

    /**
     * Zpracuje výchozí nezašifrovaný text
     *
     * @param plaintText nezašifrovaný text
     * @return text připravený k zašifrování
     * */
    public String processPlaintText(String plaintText) {
        wordsToEncrypt = new ArrayList<>();
        System.out.println("---");

        plaintText = removeDelimitersBeforeText(plaintText.toUpperCase().replaceAll("[^A-Z" + delimiter + "]", "")
                .replaceAll(delimiter + "+", delimiter));
//        plaintText = removeDelimitersBeforeText(plaintText.toUpperCase().replaceAll("[^A-Z][^ \\t\\n\\x0B\\f\\r]", "")
//                .replaceAll("[ \t\n\f\r]+", delimiter));

        if (plaintText.length() == 0)
            return "Input Error";

        String[] splitedWords = plaintText.split(delimiter);
        for (String splitedWord : splitedWords) {
            Word newWord = new Word(splitedWord);

            this.wordsToEncrypt.add(newWord);
            System.out.println("- " + splitedWord);
            newWord.setPossibleForms();
        }

        return getPlaintText();
    }

    public String decryptText(String encryptedText) {
        if (encryptedText.length() == 0)
            return "";

        wordsToDecrypt = new ArrayList<>();
        //System.out.println(encryptedText);

        encryptedText = removeDelimitersBeforeText(encryptedText.toUpperCase().replaceAll("\\)", "").replaceAll(delimiter + "+", delimiter));
        //encryptedText = removeDelimitersBeforeText(encryptedText.toUpperCase().replaceAll("\\)", "") .replaceAll("[ \t\n\f\r]+", delimiter));

        if (encryptedText.length() == 0 || !isTextFormatDecryptable(encryptedText))
            return "Input Error";

        String[] splitedWords = encryptedText.split(delimiter);
        for (String splitedWord : splitedWords) {
            String[] word = splitedWord.split("\\(");
            Word newWord = new Word(word[0], Integer.parseInt(word[1]));
            wordsToDecrypt.add(newWord);
            newWord.decrypt();
        }

        return getDecryptedText();
//        return temp;
    }

    public boolean isTextFormatDecryptable(String text) {
        text = text.replaceAll(" +", " ");
        int length = text.length();

        int i = 0;
        int counter;
        while (i < length) {
            counter = 0;
            while (i < length && Character.toString(text.charAt(i)).matches("[A-Z]")) {
                counter++;
                i++;
            }

            if (counter == 0)
                return false;

            if (i >= length || text.charAt(i) != '(')
                return false;
            i++;

            counter = 0;
            while (i < length && Character.toString(text.charAt(i)).matches("[0-9]")) {
                counter++;
                i++;
            }

            if (counter == 0)
                return false;

            if (i == length)
                return true;

//            while (i < length && text.charAt(i) == ' ')
            while (i < length && Character.toString(text.charAt(i)).equals("[ \t\n\f\r]+"))
                i++;
        }

        return true;
    }

    private String removeDelimitersBeforeText(String text) {
        int length = text.length();
        while (delimiter.equals(Character.toString(text.charAt(0)))) {
        //while (Character.toString(text.charAt(0)).equals("[ \t\n\f\r]+")) {
            text = text.substring(1, length);
            //System.out.println(text);

            length = text.length();
            if (length == 0) {
                return "";
            }
        }

        return text;
    }

    public void encryptWord(int wordId, int key) {
        wordsToEncrypt.get(wordId).encryptByKey(key);
    }

    /**
     * Vrací nezašifrovaný text, připravený k zašifrování
     *
     * @return text přiravený k zašifrování
     */
    public String getPlaintText() {
        StringBuilder text = new StringBuilder();

        for (Word word : this.wordsToEncrypt)
            text.append(word.getDecryptedWord()).append(" ");

        return text.substring(0, text.length() - 1);
    }

    public String getDecryptedText() {
        StringBuilder text = new StringBuilder();

        for (Word word : this.wordsToDecrypt)
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

        for (Word word : this.wordsToEncrypt)
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
        for (Word word : this.wordsToEncrypt)
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
        return wordsToEncrypt.get(id).getPossibileForms();
    }

    public List<Integer> getKeys() {
        List<Integer> keys = new ArrayList<>();

        for (Word word : wordsToEncrypt)
            keys.add(word.getKey());

        return keys;
    }

    public int getWordKey(int id) {
        return wordsToEncrypt.get(id).getKey();
    }

    /**
     * Vrací list slov
     *
     * @return list slov
     */
    public List<Word> getWordsToEncrypt() {
        return wordsToEncrypt;
    }
}
