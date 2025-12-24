package pt.isel.pdm.domain.user

data class Email(val email: String){
    override fun toString(): String {
        return email
    }
    init {
        require(email.isNotBlank() && email.contains("@")) {
            "Email is not a valid email format"
        }
    }
}