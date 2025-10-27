package pt.isel.pdm.domain

data class Name(val name: String){
    init {
        require(name.isNotBlank()) { "Name is not valid"}
    }
}
