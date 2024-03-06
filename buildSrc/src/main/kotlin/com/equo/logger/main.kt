package com.equo.logger

object Logger {
    private const val ANSI_ESCAPE = "\u001b"
    private const val RESET = "$ANSI_ESCAPE[0m"

    private fun print(text: String, vararg codes: Int) {
        // https://upload.wikimedia.org/wikipedia/commons/3/34/ANSI_sample_program_output.png
        val format = "$ANSI_ESCAPE[${codes.joinToString(";")}m"
        println("$format $text $RESET")
    }

    fun info(text: String) {
        print(text, 34, 1)
    }

    fun code(text: String) {
        print("$ $text", 32, 1)
    }

    fun error(text: String) {
        print("[ERROR] $text", 31, 1)
    }
}
