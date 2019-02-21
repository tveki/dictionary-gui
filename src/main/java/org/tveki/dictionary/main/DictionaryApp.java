/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tveki.dictionary.main;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tveki.dictionary.gui.DictionarySceneComposer;

/**
 *
 * @author tveki
 */
public class DictionaryApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        DictionarySceneComposer sceneComposer = new DictionarySceneComposer();
        Scene scene = sceneComposer.compose();
        primaryStage.setTitle("Dictionary");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
