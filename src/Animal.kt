data class Animal(
    var name: String? = null,
    var evolution: Boolean? = false,
    var dangerousDegree: Int? = 0,
    var yearOld: Int? = 0,
    var isPoisonous: Boolean? = false,
) {
    override fun toString(): String {
        return "Animal(name='$name', evolution=$evolution, dangerousDegree=$dangerousDegree, yearOld=$yearOld, isPoisonous=$isPoisonous)"
    }

    fun died() {
        println("$name died")
    }

    fun eat(eatenAnimal: String) {
        println("$name eat $eatenAnimal")
    }

    fun evolution() {
        println("$name is evolving")
    }
}