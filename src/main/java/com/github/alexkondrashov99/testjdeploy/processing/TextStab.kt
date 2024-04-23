package com.github.alexkondrashov99.testjdeploy.processing

sealed class TextStab {
    data class Text(val text: String): TextStab() {
        override val string: String
            get() = text
    }
    data class Stab(var stab: String): TextStab() {
        override val string: String
            get() = stab
    }

    abstract val string: String

    companion object {

        fun getTextStabs(text: String, template: String): MutableList<TextStab> {
            val templateFirstChar = template.first()
            val textStabs = mutableListOf<TextStab>()
            var startIndex = 0
            text.forEachIndexed { index, c ->
                if (c == templateFirstChar) {
                    textStabs.add(TextStab.Text(text.substring(startIndex, index)))
                    textStabs.add(TextStab.Stab(""))
                    startIndex = index + template.length
                }
            }
            val endText = text.substring(startIndex, text.length)
            if (endText.isNotEmpty()) {
                textStabs.add(TextStab.Text(endText))
            }
            return textStabs
        }
    }
}