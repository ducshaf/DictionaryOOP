package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;


public class DictionaryManagement implements Initializable {
    private Dictionary Dict;

    @FXML
    private ListView<String> listView;
    @FXML
    private TextField searchInput;
    @FXML
    private TextArea wordDetail;

    private ObservableList<String> _word = FXCollections.observableArrayList();
    private ObservableList<String> _class = FXCollections.observableArrayList();
    private ObservableList<String> _def = FXCollections.observableArrayList();

/*
 --------------------------------------------------------------------------------------------------------------
 */

    public DictionaryManagement() {
        Dict = new Dictionary();
        listView = new ListView<String>();
    }

    public Dictionary getDict() {
        return Dict;
    }

    private void getWordList() {
        for (int i = 0; i < Dict.getLength(); i++) {
            String word = Dict.getWord(i).getWord_target();
            _word.add(word);
            _class.add(Dict.getWord(i).getWord_class());
            _def.add(Dict.getWord(i).getWord_explain());
        }
    }

    public void insertFromFile(String path) throws FileNotFoundException {
        File text = new File(path);
        Scanner sc = new Scanner(text);

        try {
            String line;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                String[] parts = line.split("\t", 3);
                String word = parts[0];
                String classes = parts[1];
                String def  = parts[2];
                Dict.addWord(new Word(word, classes, def));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sc.close();
    }

    public void dictionaryLookup(String word) {
        int index = _word.indexOf(word);
        String details = _class.get(index) + "\n" + _def.get(index);
        wordDetail.setText(details);
    }

    public void addNewWord(Word x) {
        Dict.addWord(x);
    }

    public void deleteWord(Word x) {
        Dict.removeWord(x);
    }

    public int dictionarySearcher(String x) {
        for (int i = 0; i < _word.size(); i++) {
            String word = _word.get(i);
            if (word.length() < x.length()) continue;
            String subWord = word.substring(0, x.length());
            if (x.equals(subWord)) {
                listView.getSelectionModel().select(i);
                listView.scrollTo(i);
                return i;
            }
        }
        return -1;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            insertFromFile("F:\\PROJECT\\DictionaryOOP\\src\\sample\\dictionary.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        listView.setItems(_word);
        getWordList();

        /**
         * Wath for input from SearchBar and scroll to the searched word.
         */

        searchInput.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                int tmp = dictionarySearcher(t1);
            }
        });

        /**
         * If any word is selected, show details.
         */
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                dictionaryLookup(t1);
            }
        });
    }
}