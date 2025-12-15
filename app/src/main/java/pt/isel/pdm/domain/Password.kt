package pt.isel.pdm.domain

data class Password(val password: String) {
    companion object {
        val specialChars = setOf(
            '!', '@', '#', '$', '%', '^', '&', '*', '(', ')',
            '-', '_', '=', '+', '[', ']', '{', '}', ';', ':',
            '\'', '"', ',', '<', '>', '.', '/', '?', '\\', '|',
            '`', '~'
        )
        const val MIN_CHARACTERS = 5
    }
    init {
//        val hasSpecial = password.any { it in specialChars }
//
//        require(password.isNotBlank() && password.length >= MIN_CHARACTERS && hasSpecial) {
//            "A password tem de ter pelo menos $MIN_CHARACTERS caracteres e conter pelo menos um carácter especial"
//        }
    }
}
