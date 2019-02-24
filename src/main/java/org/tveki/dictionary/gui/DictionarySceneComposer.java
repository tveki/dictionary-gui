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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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

    HBox languageChooserLayout;
    HBox buttonsLayout;

    private TextField phraseField;
    private Button translateButton;
    private Button meaningButton;
    private ListView<String> translationList;

    private final Dictionary dictionary = new GlosbeDictionary();

    private TranslationCache cache;

    private static final Language[] LANGUAGES = {
        Language.ENGLISH,
        Language.GERMAN,
        Language.HUNGARIAN
    };

    public Scene compose() {
        VBox pane = new VBox();

        cache = new TranslationCache();

        initLanguageChooserLayout();

        phraseField = new TextField();

        translateButton = new Button("Translate");
        translateButton.setOnAction(this::onTranslateClick);

        meaningButton = new Button("Meaning");
        meaningButton.setOnAction(this::onMeaningClick);

        buttonsLayout = new HBox(translateButton, meaningButton);
        buttonsLayout.setPadding(new Insets(15, 12, 15, 12));
        buttonsLayout.setSpacing(10);

        translationList = new ListView<>();
        translationList.setPrefHeight(CanvasProps.CANVAS_HEIGHT);

        pane.getChildren().add(languageChooserLayout);
        pane.getChildren().add(phraseField);
        pane.getChildren().add(buttonsLayout);
        pane.getChildren().add(translationList);

        return new Scene(pane, SCENE_WIDTH, SCENE_HEIGHT);
    }

    private void onTranslateClick(ActionEvent event) {
        showTranslations();
    }

    private void onMeaningClick(ActionEvent event) {
        showMeanings();
    }

    private void initLanguageChooserLayout() {
        initFromLanguageChooser();
        initToLanguageChooser();

        languageChooserLayout = new HBox();
        languageChooserLayout.setPadding(new Insets(15, 12, 15, 12));
        languageChooserLayout.setSpacing(10);
        
        languageChooserLayout.getChildren().add(new Label("From:"));
        languageChooserLayout.getChildren().add(fromLanguageChooser);
        languageChooserLayout.getChildren().add(new Label("To:"));
        languageChooserLayout.getChildren().add(toLanguageChooser);
    }

    private void initFromLanguageChooser() {
        fromLanguageChooser = new ComboBox<>();
        fromLanguageChooser.setConverter(new LanguageConverter());
        fromLanguageChooser.getItems().addAll(LANGUAGES);
        fromLanguageChooser.setValue(Language.ENGLISH);
    }

    private void initToLanguageChooser() {
        toLanguageChooser = new ComboBox<>();
        toLanguageChooser.setConverter(new LanguageConverter());
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

}
