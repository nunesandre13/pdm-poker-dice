package pt.isel.pdm.domain.inputs

import pt.isel.pdm.domain.Name

@JvmInline
value class NameInput (val name : String) {
    fun toName() : Name = Name(name)
}