/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tveki.dictionary.gui;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.tveki.dictionary.api.Dictionary;
import org.tveki.dictionary.api.Language;
import org.tveki.dictionary.api.TranslateRequest;
import org.tveki.dictionary.api.TranslateResponse;
import org.tveki.glosbe.dictionary.GlosbeDictionary;

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
    private Button meaningButton;
    private ListView<String> translationList;
    
    private Dictionary dictionary = new GlosbeDictionary();
    
    private TranslationCache cache;

    private static final Language[] LANGUAGES = {
        Language.ENGLISH,
        Language.GERMAN,
        Language.HUNGARIAN
    };

    public Scene compose() {
        VBox pane = new VBox();
        
        cache = new TranslationCache();

        initFromLanguageChooser();
        initToLanguageChooser();
        
        phraseField = new TextField();
        
        translateButton = new Button("Translate");
        translateButton.setOnAction(this::onTranslateClick);
        
        meaningButton = new Button("Meaning");
        meaningButton.setOnAction(this::onMeaningClick);
        
        translationList = new ListView<>();
        translationList.setPrefHeight(CanvasProps.CANVAS_HEIGHT);
      
        pane.getChildren().add(fromLanguageChooser);
        pane.getChildren().add(toLanguageChooser);
        pane.getChildren().add(phraseField);
        pane.getChildren().add(translateButton);
        pane.getChildren().add(meaningButton);
        pane.getChildren().add(translationList);

        return new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
    }
    
    private void onTranslateClick(ActionEvent event) {
        showTranslations();
    }
    
    private void onMeaningClick(ActionEvent event) {
        showMeanings();
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
    
    private void showTranslations() {
        showResult(cache::getTranslations);
    }
    
    private void showMeanings() {
        showResult(cache::getMeanings);
    }
    
    private void showResult(Supplier<List<String>> resultSupplier) {
        String phrase = phraseField.getText();
        
        if (phrase == null || phrase.trim().isEmpty()) {
            refreshTranslationListView(() -> Collections.EMPTY_LIST);
            return;
        }
        
        TranslateRequest request = createRequest();
        
        if (!request.equals(cache.getTask())) {
            updateTranslationCache(request);
        }    
        
        refreshTranslationListView(resultSupplier);
    }

    private void refreshTranslationListView(Supplier<List<String>> resultSupplier) {
        translationList.setItems(FXCollections.observableArrayList(resultSupplier.get()));
    }

    private void updateTranslationCache(TranslateRequest request) {
        TranslateResponse response = dictionary.translate(request);
        
        cache.setTask(request);
        cache.setTranslations(response.getTranslations());
        cache.setMeanings(response.getMeanings());
    }

    private TranslateRequest createRequest() {
        TranslateRequest request = new TranslateRequest();
        request.setFrom(fromLanguageChooser.getValue());
        request.setTo(toLanguageChooser.getValue());
        request.setPhrase(phraseField.getText());
        return request;
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
