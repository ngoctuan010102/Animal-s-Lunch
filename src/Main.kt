import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    val listAnimal = fakeDate()
    var check = 0
    //validate input
    var dataInput: String? = null
    do {
        dataInput = readlnOrNull()
        dataInput?.let {
            if (isValidInput(dataInput)) {
                check = 1
            }
        } ?: -1
    } while (check != 1)
    // get List of the input
    val list = getListString(dataInput)

    // divide the animal into 2 group
    val listAAnimal = mutableListOf<Animal>()
    val listBAnimal = mutableListOf<Animal>()

    divideAnimal(list, listAnimal) { listA, listB ->
        listAAnimal.addAll(listA)
        listBAnimal.addAll(listB)
    }
    // have lunch
    haveLunch(listAAnimal, listBAnimal)
    // after lunch
    afterLunch(listAAnimal, listBAnimal)
}

fun afterLunch(listAAnimal: MutableList<Animal>, listBAnimal: MutableList<Animal>) {
    println("======After Lunch======")
    // 2 list to store 2 group
    val listSortA = sortDes(listAAnimal).toMutableList()
    val listSortB = sortAscending(listBAnimal).toMutableList()
    // filter the list based on the year old of the animal
    // if year old == 0, -> animal died
    // if not, sum the degree to the animal in A
    // if the animal eaten poisonous animal, it will die too, and set the year old to 0
    for (i in listSortA.indices) {
        var count = 0
        for (j in listSortB.indices) {
            if (listSortB[j].yearOld != 0) {
                listSortA[i].dangerousDegree = listSortA[i].dangerousDegree?.plus(listSortB[j].dangerousDegree!!)
                listSortB[j].yearOld = 0
                if (listSortB[j].isPoisonous!!)
                    listSortA[i].yearOld = 0
                count++
            }
            if (count == 2) {
                break
            }
        }
    }
    //animal evolution and the highest
    var highestAnimal: Animal? = null
    val highestDegree: Int = 0
    for (i in listSortA.indices) {
        if (listSortA[i].yearOld != 0 && listSortA[i].dangerousDegree!! > highestDegree) {
            highestAnimal = listSortA[i]
            if (listSortA[i].dangerousDegree!! > 15)
                listSortA[i].evolution()
        }
    }
    //set null for died animal
    for (i in listSortA.indices) {
        if (listSortA[i].yearOld == 0) {
            listSortA[i].died()
            listSortA[i] = setDied(listSortA[i])
        }
    }
    for (i in listSortB.indices) {
        if (listSortB[i].yearOld == 0)
            listSortB[i] = setDied(listSortB[i])
    }
    //highest animal
    highestAnimal?.let {
        println("$highestAnimal with highest degree")
    } ?: println("No animal with highest degree")

}
//set null for
fun setDied(animal: Animal): Animal {
    return animal.apply {
        name = null
        yearOld = null
        dangerousDegree = null
        evolution = null
        isPoisonous = null
    }
}

// when animals have lunch
fun haveLunch(listAAnimal: MutableList<Animal>, listBAnimal: MutableList<Animal>) {
    println("======Have Lunch======")
    //sort based on the degree
    val listSortA = sortDes(listAAnimal)
    val listSortB = sortAscending(listBAnimal).toMutableList()

    for (i in listSortA.indices) {
        if (i != 3) {
            for (j in 0..1) {
                listSortB[j].name?.let {
                    listSortA[i].eat(it)
                    //notify the animal died
                    listSortB[j].died()
                }
            }
            // if A eaten B, remove the element out of listB
            listSortB.removeAt(0)
            listSortB.removeAt(0)
        }
    }
}

// sort animal des based on dangerousDegree
fun sortDes(list: MutableList<Animal>): List<Animal> {
    for (i in list.indices) {
        for (j in i + 1..<list.size) {
            if (list[i].dangerousDegree!! < list[j].dangerousDegree!!) {
                val pos = list[i]
                list[i] = list[j]
                list[j] = pos
            }
        }
    }
    return list
}

// sort animal ascending based on dangerousDegree
fun sortAscending(list: MutableList<Animal>): List<Animal> {
    for (i in list.indices) {
        for (j in i + 1..<list.size) {
            if (list[i].dangerousDegree!! > list[j].dangerousDegree!!) {
                val pos = list[i]
                list[i] = list[j]
                list[j] = pos
            }
        }
    }
    return list
}

// divide animal into 2 group A & B
fun divideAnimal(list: List<Int>?, listAnimal: List<Animal>, func: (List<Animal>, List<Animal>) -> Unit) {
    //list A & B to store the group
    val listAAnimal = mutableListOf<Animal>()
    val listBAnimal = listAnimal.toMutableList()
    for (i in list?.indices!!) {
        if (!listAAnimal.contains(listAnimal[list[i]])) {
            listAAnimal.add(listAnimal[list[i]])
            listBAnimal.remove(listAnimal[list[i]])
        }
    }
    func(listAAnimal, listBAnimal)
}
//convert input string to right format
fun getListString(dataInput: String?): List<Int>? {
    return dataInput?.let {
        val list = listOf(
            "${dataInput[0]}".toInt(), "${dataInput[1]}".toInt(),
            "${dataInput[3]}".toInt(), "${dataInput[4]}".toInt()
        )
        list
    }
}
// check if input is valid
fun isValidInput(input: String): Boolean {
    val inputAdd = "$input/2024"
    return try {
        val date = LocalDate.parse(inputAdd, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val textDate = "${input[0]}${input[1]}"
        textDate.toInt() == date.dayOfMonth
    } catch (e: DateTimeParseException) {
        false
    }
}

fun fakeDate(): List<Animal> {
    val animal = mutableListOf<Animal>()
    animal.add(Animal("Snake", false, 1, 15, true))
    animal.add(Animal("Elephant", false, 2, 4, true))
    animal.add(Animal("Jaguar", false, 3, 2, false))
    animal.add(Animal("Hyenas", false, 4, 3, false))
    animal.add(Animal("Whale", false, 5, 7, true))
    animal.add(Animal("Carp", false, 6, 23, false))
    animal.add(Animal("Ape", false, 7, 26, true))
    animal.add(Animal("Tiger", false, 8, 6, true))
    animal.add(Animal("Lion", false, 9, 12, true))
    animal.add(Animal("Dinosaur", false, 10, 5, false))
    return animal
}