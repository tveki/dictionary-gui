/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tveki.dictionary.gui;

import javafx.util.StringConverter;
import org.tveki.dictionary.api.Language;

/**
 *
 * @author tveki
 */
public class LanguageConverter extends StringConverter<Language> {

    @Override
    public String toString(Language language) {
        return language.getISO2Code();
    }

    @Override
    public Language fromString(String code) {
        return Language.byISO2(code);
    }

}
