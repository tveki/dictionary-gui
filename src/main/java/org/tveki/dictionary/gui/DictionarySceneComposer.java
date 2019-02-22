/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tveki.dictionary.gui;

import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.tveki.dictionary.api.Language;

/**
 *
 * @author tveki
 */
public class DictionarySceneComposer {

    public static final int SCENE_WIDTH = CanvasProps.CANVAS_WIDTH;
    public static final int SCENE_HEIGHT = CanvasProps.CANVAS_HEIGHT + 100;

    private ComboBox<Language> fromLanguageChooser;
    private ComboBox<Language> toLanguageChooser;
    private TextField phraseField;
    private Button translateButton;
    private ListView<String> translationList;

    private static final Language[] LANGUAGES = {
        Language.ENGLISH,
        Language.GERMAN,
        Language.HUNGARIAN
    };

    public Scene compose() {
        VBox pane = new VBox();

        initFromLanguageChooser();
        initToLanguageChooser();
        
        phraseField = new TextField();
        translateButton = new Button("Translate");
        translateButton.setOnAction(this::onTranslateClick);
        
        ObservableList<String> translations = FXCollections.observableArrayList();
        translations.add("kutya");
        translations.add("eb");
        
        translationList = new ListView<>();
        translationList.setPrefSize(200, 250);
        translationList.setItems(translations);
      
        pane.getChildren().add(fromLanguageChooser);
        pane.getChildren().add(toLanguageChooser);
        pane.getChildren().add(phraseField);
        pane.getChildren().add(translateButton);
        pane.getChildren().add(translationList);

        return new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
    }
    
    private void onTranslateClick(ActionEvent event) {
        
    }

    private void initFromLanguageChooser() {
        fromLanguageChooser = new ComboBox<>();
        fromLanguageChooser.setConverter(languageConverter());
        fromLanguageChooser.getItems().addAll(LANGUAGES);
        fromLanguageChooser.setValue(Language.ENGLISH);
    }

    private void initToLanguageChooser() {
        toLanguageChooser = new ComboBox<>();
        toLanguageChooser.setConverter(languageConverter());
        toLanguageChooser.getItems().addAll(LANGUAGES);
        toLanguageChooser.setValue(Language.HUNGARIAN);
    }
    
    private StringConverter<Language> languageConverter() {
        return new StringConverter<Language>() {
            @Override
            public String toString(Language language) {
                return language.getISO2Code();
            }

            @Override
            public Language fromString(String code) {
                return Language.byISO2(code);
            }
        };
    }

}
