/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tveki.dictionary.gui;

import java.util.List;
import org.tveki.dictionary.api.TranslateRequest;
import org.tveki.dictionary.api.TranslationTask;

/**
 *
 * @author tveki
 */
public class TranslationCache {
    
    private TranslateRequest task;
    
    private List<String> translations;
    private List<String> meanings;

    public TranslationTask getTask() {
        return task;
    }

    public void setTask(TranslateRequest task) {
        this.task = task;
    }

    public List<String> getTranslations() {
        return translations;
    }

    public void setTranslations(List<String> translations) {
        this.translations = translations;
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<String> meanings) {
        this.meanings = meanings;
    }
    
}
