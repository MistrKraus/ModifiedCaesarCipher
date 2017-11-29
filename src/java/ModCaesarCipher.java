import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ModCaesarCipher {
    private static final short ALPHABET_LENGTH = 26;
    private static final short ASCII_A = 65;
    private static final short VOWELS_COUNT = 6;//5;
    private static final short CONSONANT_COUNT = 13;//14;//15;
    private static final short LOW_USED_COUNT = 5;
    private static final short MAX_KEY = VOWELS_COUNT * CONSONANT_COUNT;

    private static final char[][] alphabet = new char[ALPHABET_LENGTH][MAX_KEY];
    private static final char[] vowels = new char[]{ 'A', 'E', 'I', 'O', 'U', 'Y' };
    //private static final char[] vowels = new char[]{ 'A', 'E', 'I', 'O', 'U' };
    //    private static final char[] consonants = new char[] { 'B', 'C', 'D', 'H', 'J', 'K', 'L', 'M', 'N',
//            'P', 'R', 'S', 'T', 'V', 'Z' };
//    private static final char[] consonants = new char[] { 'B', 'C', 'D', 'F', 'H', 'J', 'K', 'L', 'M',
//                                                    'P', 'R', 'S', 'T', 'W' };
    private static final char[] consonants = new char[] { 'B', 'C', 'D', 'J', 'K', 'L', 'M', 'N',
                                                    'P', 'R', 'S', 'T', 'V' };
//    private static final char[] lowUsed = new char[] { 'F', 'G', 'Q', 'W', 'X' };
    private static final char[] lowUsed = new char[] { 'H', 'F', 'G', 'Q', 'X', 'W', 'Z' };

    public ModCaesarCipher() {
        //setAlphabet();
    }

    public ObservableList<String> encryptWord(Word input_word) {
        ObservableList<String> allEncryptions = FXCollections.observableArrayList();
        String word = input_word.getWord();
        int key = input_word.getKey() % MAX_KEY;

        int wordLength = word.length();

//        for (int i = 0; i < MAX_KEY; i++) {
//            StringBuilder encryptedWord = new StringBuilder();
//
//            for (int j = 0; j < wordLength; j++) {
//                char x = word.charAt(j);
//                int index = getDecrypedCharAlphabetIndex(x, key);
//
//                encryptedWord.append(alphabet[index][i]);
//            }
//            allEncryptions.add(encryptedWord.toString());
//        }
//
//        return allEncryptions;

        for (int i = 0; i < MAX_KEY; i++) {
            StringBuilder encryptedWord = new StringBuilder();
            for (int j = 0; j < wordLength; j++) {
                char x = word.charAt(j);
                int index = vowelIndex(x);

                if (index != -1) {
                    encryptedWord.append(vowels[(index + i) % VOWELS_COUNT]);

                    continue;
                }

                index = consonantIndex(x);

                if (index != -1) {
                    encryptedWord.append(consonants[(index + i) % CONSONANT_COUNT]);

                    continue;
                }

                encryptedWord.append(x);
            }
            allEncryptions.add(encryptedWord.toString());

            if (key == i)
                input_word.setEncryptedWord(encryptedWord.toString());
        }

        return allEncryptions;
    }

    public String encryptWord(Word input_word, int key) {
        key = key % MAX_KEY;

        String word = input_word.getWord();
        int currentKey = input_word.getKey();

        int wordLength = word.length();
        StringBuilder encryptedWord = new StringBuilder();
        for (int i = 0; i < wordLength; i++) {
            char x = word.charAt(i);

            for (int j = 0; j < ALPHABET_LENGTH; j++)
                if (x == alphabet[j][currentKey]) {
                    encryptedWord.append(alphabet[j][key]);
                    continue;
                }

//            if (isConsonant(x)) {
//                for (int j = 0; j < CONSONANT_COUNT; j++)
//                    if (x == alphabet[j][currentKey]) {
//                        encryptedWord.append(alphabet[i][key]);
//                        break;
//                    }
//
//                continue;
//            }
//
//            if (isVowel(x)) {
//                for (int j = 0; j < VOWELS_COUNT; j++)
//                    if (x == alphabet[j][currentKey]) {
//                        encryptedWord.append(alphabet[i][key]);
//                        break;
//                    }
//            }
        }

        input_word.setKey(key);


        return encryptedWord.toString();
    }
    
    private void setAlphabet() {
        for (int i = 0; i < ALPHABET_LENGTH; i++) {
            char current = ((char)(ASCII_A + i));
            int charIndex = getCharSubalphabetIndex(current);
            alphabet[i][0] = current;

            if (isConsonant(current)) {
                for (int j = 1; j < MAX_KEY; j++)
                    alphabet[i][j] = consonants[(charIndex + j) % CONSONANT_COUNT];
            }

            if (isVowel(current)) {
                for (int j = 1; j < MAX_KEY; j++)
                    alphabet[i][j] = vowels[(charIndex + j) % VOWELS_COUNT];
            }

            if (charIndex == -1) {
                for (int j = 1; j < MAX_KEY; j++)
                    alphabet[i][j] = current;
            }

            System.out.println(current + " done.");
        }
    }

    // TODO
    private int getDecrypedCharAlphabetIndex(char x, int key) {
        key = key % MAX_KEY;
        if (key == 0)
            return ((int) x) - ASCII_A;

        for (int i = 0; i < ALPHABET_LENGTH; i++)
            if (x == alphabet[i][key])
                return i;

        return -1;
    }

    // TODO
    private int getCharSubalphabetIndex(char x) {
        if (isConsonant(x))
            for (int i = 0; i < CONSONANT_COUNT; i++)
                if (x == consonants[i])
                    return i;

        if (isVowel(x))
            for (int i = 0; i < VOWELS_COUNT; i++)
                if (x == vowels[i])
                    return i;

        return -1;
    }

    private int vowelIndex(char x) {
        for (int i = 0; i < VOWELS_COUNT; i++) {
            if (x == vowels[i])
                return i;
        }

        return -1;
    }

    private int consonantIndex(char x) {
        for (int i = 0; i < CONSONANT_COUNT; i++) {
            if (x == consonants[i])
                return i;
        }

        return -1;
    }

    private boolean isLowUsed(char x) {
        return contains(x, lowUsed);
    }

    private boolean isVowel(char x) {
        return contains(x, vowels);
    }

    private boolean isConsonant(char x) {
        return contains(x, consonants);
    }

    private boolean contains(char x, char[] chars) {
        for (char c : chars)
            if (c == x)
                return true;
        return false;
    }

    public static short getMaxKey() {
        return MAX_KEY;
    }

    public String decryptWord(String word, int key) {
        StringBuilder decryptedWord = new StringBuilder();

//        for (int i = 0; i < word.length(); i++)
//            for (int j = 0; j < ALPHABET_LENGTH; j++)
//                if (word.charAt(i) == alphabet[j][key%MAX_KEY])
//                    decryptedWord.append(alphabet[j][0]);

        int lenght = word.length();

        for (int i = 0; i < lenght; i++) {
            char x = word.charAt(i);
            int index = vowelIndex(x);
            int id;

            if (index != -1)  {
                id = index - (key % VOWELS_COUNT);
                if (id < 0)
                    id = VOWELS_COUNT + id;

                decryptedWord.append(vowels[id]);
                continue;
            }

            index = consonantIndex(x);

            if (index != -1) {
                id = index - (key % CONSONANT_COUNT);

                if (id < 0)
                    id = CONSONANT_COUNT + id;

                decryptedWord.append(consonants[id]);

                continue;
            }

            decryptedWord.append(x);
        }

        return decryptedWord.toString();
    }
}
