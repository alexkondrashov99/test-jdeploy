package com.github.alexkondrashov99.testjdeploy

import javafx.fxml.FXMLLoader
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

object FileOpenHelper {
    fun openFile(stage: Stage, fxmlLoader: FXMLLoader) {
        val fileChooser = FileChooser()
        fileChooser.title = "Open Resource File"
        val file = fileChooser.showOpenDialog(stage)
        file?.let {
            val str = Files.readString(Path.of(file.toURI()))
            fxmlLoader.getController<HelloController>().welcomeText.text = str

            try {
                Desktop.getDesktop().open(file)
            } catch (ex: IOException) {

            }
        }
    }
}