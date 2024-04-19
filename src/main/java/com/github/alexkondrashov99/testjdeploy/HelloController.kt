package com.github.alexkondrashov99.testjdeploy

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.stage.FileChooser
import javafx.stage.Stage


class HelloController {
    companion object {
        private var counter = 0
    }

    init {

    }


    @FXML
    lateinit var welcomeText: Label

    @FXML
    lateinit var btFile: Button
}