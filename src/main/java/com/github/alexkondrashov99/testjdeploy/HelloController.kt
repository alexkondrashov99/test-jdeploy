package com.github.alexkondrashov99.testjdeploy

import com.github.alexkondrashov99.testjdeploy.processing.TextStab
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.*
import javafx.scene.control.Alert.AlertType
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.awt.Desktop
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path


class HelloController {
    companion object {
        private val TEMPLATE = "[x_x_x]"
    }


    private var stabs = mutableListOf<TextStab>()
    private var stabIndex = 0
        set(value) {
            field = value
            renderStab(stabIndex, stabs)
        }


    init {

    }


    @FXML
    lateinit var menuItemLoad: MenuItem

    @FXML
    lateinit var btnPrev: Button

    @FXML
    lateinit var btnNext: Button

    @FXML
    lateinit var btnSave: Button

    @FXML
    lateinit var labelBefore: Label

    @FXML
    lateinit var labelAfter: Label

    @FXML
    lateinit var textInput: TextArea

    fun init(stage: Stage, fxmlLoader: FXMLLoader) {
        menuItemLoad.setOnAction { loadText(stage) }
        configureButtons(stage)

    }

    private fun configureButtons(stage: Stage) {
        btnPrev.setOnAction {
            if (stabIndex - 2 >= 0) {
                saveCurrentStabState(stabIndex, stabs)
                stabIndex -= 2

            }
        }
        btnNext.setOnAction {
            if (stabIndex + 2 <= stabs.lastIndex) {
                saveCurrentStabState(stabIndex, stabs)
                stabIndex += 2
            }
        }
        btnSave.setOnAction {
            if (stabs.isNotEmpty()) {
                saveText(stage, stabs)
            }
        }
    }

    private fun saveCurrentStabState(stabIndex: Int, stabs: List<TextStab>) {
        (stabs.getOrNull(stabIndex) as? TextStab.Stab)?.let { textStab ->
            textStab.stab = textInput.text
        }
    }

    private fun renderStab(stabIndex: Int, stabs: List<TextStab>) {

        btnPrev.isDisable = stabIndex - 2 < 0
        btnNext.isDisable = stabIndex + 2 > stabs.lastIndex

        labelBefore.text = stabs.getOrNull(stabIndex - 1)?.string.orEmpty()
        textInput.text = stabs.getOrNull(stabIndex)?.string.orEmpty()
        labelAfter.text = stabs.getOrNull(stabIndex + 1)?.string.orEmpty()

    }

    private fun loadText(stage: Stage) {
        val text = loadTextFromFile(stage)
        text?.let {
            try {
                stabs = TextStab.getTextStabs(text, TEMPLATE)
            } catch (e: Exception) {
                showErrorAlert("File read error", "Stabs not found or another exception: ${e.message}")
                return
            }

            if (stabs.size > 0) {
                val firstStabIndex = stabs.indexOfFirst { it is TextStab.Stab }
                if (firstStabIndex >= 0) {
                    stabIndex = firstStabIndex
                } else {
                    showErrorAlert("File read error", "Stabs not found")
                }
            } else {
                showErrorAlert("File read error", "Stabs not found")
            }
        }
    }

    private fun loadTextFromFile(stage: Stage): String? {
        val fileChooser = FileChooser()
        fileChooser.title = "Open template file"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Text Files", "*.txt"))
        val file = fileChooser.showOpenDialog(stage)
        return file?.let {
            try {
                Files.readString(Path.of(file.toURI()))
            } catch (ex: IOException) {
                showErrorAlert("File read error", ex.message.orEmpty())
                null
            }
        }
    }

    private fun openFile(file: File) {
        Desktop.getDesktop().open(file)
    }




    private fun showErrorAlert(header: String,text: String) {
        val alert = Alert(AlertType.ERROR)
        alert.title = "Error"
        alert.headerText = header
        alert.contentText = text
        alert.showAndWait()
    }

    private fun saveText(stage: Stage, stabs: List<TextStab>) {
        val fileChooser = FileChooser().apply {
            // Задайте фільтри для типів файлів, якщо потрібно
            extensionFilters.add(FileChooser.ExtensionFilter("Text Files", "*.txt"))
            title = "Зберегти як"
        }

        // Відкрити діалог збереження файлу
        val file = fileChooser.showSaveDialog(stage)
        file?.let {
            val builder = StringBuilder().apply {
                stabs.forEach { stab ->
                    this.append(stab.string)
                }
            }
            file.writeText(builder.toString())
        }
    }
}