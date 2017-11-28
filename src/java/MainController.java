import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    public ComboBox rawWords;
    public ComboBox cipheredWords;
    public TextArea inputText;
    public TextArea outputText;
    public TextArea keysText;
    public GridPane gridPane;

    public TextArea encryptedText;
    public TextArea decryptedText;
    public Button loadBtn;

    private TextManager textManager;

    private ObservableList<String> rawWordsList = FXCollections.observableArrayList();
    private ObservableList<String> cipheredKeysList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textManager = new TextManager();

        loadBtn.setTooltip(new Tooltip("File format:\n<word>(<key>)<space>..."));
        encryptedText.setPromptText("<word>(<key>)<space>");

        //System.out.println(textManager.processPlaintText("Ahoj! Jak se mas?"));

        inputText.setPrefRowCount(3);
        outputText.setPrefRowCount(3);
        keysText.setPrefRowCount(2);

        encryptedText.setPrefRowCount(3);
        decryptedText.setPrefRowCount(3);

        rawWords.getSelectionModel().select(0);
        cipheredWords.getSelectionModel().select(0);

        rawWords.getSelectionModel().selectedIndexProperty().addListener((Observable observable) -> {
            int wordIndex = rawWords.getSelectionModel().getSelectedIndex();
            System.out.println(wordIndex);
            if (wordIndex == -1)
                return;

            cipheredWords.setItems(textManager.getEncryptedForms(wordIndex));

            outputText.clear();
            keysText.clear();

            outputText.setText(textManager.getEncryptedText());

            List<Integer> keys = textManager.getKeys();
            StringBuilder keysTex = new StringBuilder();
            for (int key : keys)
                keysTex.append(key + " - ");

            String keysString = keysTex.toString();

            keysText.setText(keysString.substring(0, keysString.length() - 3));
            
            cipheredWords.getSelectionModel().select(textManager.getWordKey(wordIndex));
        });

        // sifrovane formy slova
        cipheredWords.getSelectionModel().selectedIndexProperty().addListener(observable -> {
            int newKey = cipheredWords.getSelectionModel().getSelectedIndex();
            int wordIndex = rawWords.getSelectionModel().getSelectedIndex();
            System.out.println(wordIndex + " - " + newKey);
            if (newKey == -1 || wordIndex == -1)
                return;

            textManager.encryptWord(wordIndex, newKey);
            outputText.clear();
            keysText.clear();

            outputText.setText(textManager.getEncryptedText());

            List<Integer> keys = textManager.getKeys();
            StringBuilder keysTex = new StringBuilder();
            for (int key : keys)
                keysTex.append(key + " - ");

            String keysString = keysTex.toString();

            keysText.setText(keysString.substring(0, keysString.length() - 3));
        });

        //inputText.setText("Ahoj!   tri mezery");
//        inputText.textProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
//
//            }
//        });
    }

    public void processText(MouseEvent mouseEvent) {
        textManager.processPlaintText(inputText.getText());

        outputText.clear();
        keysText.clear();

        this.rawWordsList = textManager.getDecryptedWords();
        if (rawWordsList.size() == 0) {
            outputText.setText("Input Error");
            rawWords.setItems(null);
            cipheredWords.setItems(null);
            return;
        }
        this.cipheredKeysList = textManager.getEncryptedForms(0);

        outputText.setText(textManager.getEncryptedText());
        rawWords.setItems(rawWordsList);
        cipheredWords.setItems(cipheredKeysList);

        List<Integer> keys = textManager.getKeys();
        StringBuilder keysTex = new StringBuilder();
        for (int key : keys)
            keysTex.append(key + " - ");

        String keysString = keysTex.toString();

        keysText.setText(keysString.substring(0, keysString.length() - 3));

        rawWords.getSelectionModel().select(0);
        cipheredWords.getSelectionModel().select(0);
    }

    /**
     * Reakce na stisk tlačítka "Encode"
     * Zavolá funkci pro zašifrování a uložení textu v TextArea "Input text"
     *
     * @param mouseEvent
     */
    public void encodeText(MouseEvent mouseEvent) {

    }

    public void loadText(MouseEvent mouseEvent) {

    }

    public void decryptText(MouseEvent mouseEvent) {
        decryptedText.clear();
        decryptedText.setText(textManager.decryptText(encryptedText.getText()));
    }
}
