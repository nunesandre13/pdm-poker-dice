package pt.isel.pdm.domain

data class Email(val email: String){
    override fun toString(): String {
        return email
    }
}