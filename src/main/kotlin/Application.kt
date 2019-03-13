
object Application {
    fun run() {
        println("hello world!")
        log("hello", "word", "...")
        log(abc, abcImmutablevar)
        log(describeString(nullable))
        println(MutableStack(0.2, 0.4))
        println(mutableStackOf(1, 2,3))
        Yorkshire().sayHello()
        Yorkshire().getDogName()
        Yorkshire("Yorkshire").sayHello()
        Asiatic("Asiatic").sayHello()
        getZoo() // why can't print out
        functional()
    }
}

fun main(args: Array<String>) {
    Application.run()
}

var abc:String = "initial"

val abcImmutablevar = "initial"

var nullable:String? = "you can keep a null here"

fun infixfunctions() {

    infix fun Int.times(str: String) = str.repeat(this)        // 1
    println(2 times "Bye ")                                    // 2

    val pair = "Ferrari" to "Katrina"                          // 3
    println(pair)

    infix fun String.onto(other: String) = Pair(this, other)   // 4
    val myPair = "McLaren" onto "Lucas"
    println(myPair)

    val sophia = Person("Sophia")
    val claudia = Person("Claudia")
    sophia likes claudia                                       // 5
}

class Person(val name: String) {
    val likedPeople = mutableListOf<Person>()
    infix fun likes(other: Person) { likedPeople.add(other) }  // 6
}

fun describeString(maybeString: String?): String {
    if (maybeString != null && maybeString.isNotEmpty()) {
        return "String of length ${maybeString.length}"
    } else {
        return "Empty or null string"
    }
}

// vararg : any number of arguments by separating them with commas
fun printAll(vararg messages :String) {
    for (m in messages) println(m)
}

fun log(vararg entries: String) {
    printAll(*entries)
}

class Customer

class Contact (val id:Int, var email:String)

// generics : 泛型
class MutableStack<E>(vararg items: E) {              // 1

    private val elements = items.toMutableList()

    fun push(element: E) = elements.add(element)        // 2

    fun peek(): E = elements.last()                     // 3

    fun pop(): E = elements.removeAt(elements.size - 1)

    fun isEmpty() = elements.isEmpty()

    fun size() = elements.size

    override fun toString() = "MutableStack(${elements.joinToString()})"
}

// generics function
fun <E> mutableStackOf(vararg elements: E) = MutableStack(*elements)

// inheritance: Kotlin classes are final by default
// use open allow the class inheritance
open class Dog {
    // the open modifier allows overriding them.
    open fun sayHello() {
        println("wow wow!")
    }
    open fun getDogName():String {
        return "this is a dog"
    }
}

class Yorkshire(private val name:String) : Dog() {
    constructor() : this("i am just a dog without name")
    override fun sayHello() {   // 4
        println("a dog name:$name, say: wif wif!")
    }
    override fun getDogName() = name
}

open class Lion(private  val name: String, private val origin: String) {
    fun sayHello() {
        println("$name, the lion from $origin says: graoh!")
    }
}
class Asiatic(name: String) : Lion(name = name, origin = "India") {
    // sayHello() is not a open method that can be override
}

// control flow
// when
fun cases(obj: Any) {                                // 1
    when (obj) {
        1 -> println("One")                          // 2
        "Hello" -> println("Greeting")               // 3
        is Long -> println("Long")                   // 4
        !is String -> println("Not a string")        // 5
        else -> println("Unknown")                   // 6
    }
}

// loops: for(.. in ..), while(), do {} while()

// iterators
class Animal(val name: String)

class Zoo(private  val animals: List<Animal>) {
    operator fun iterator(): Iterator<Animal> {
        return animals.iterator()
    }
}

fun getZoo() = {
    val zoo = Zoo(listOf(Animal("zebra"), Animal("lion")))
    for (animal in zoo) {
        println("Watch out, it's a ${animal.name}")
    }
}


// ranges

fun ranges() {
    for (i in 1..3 step 1) {
        println(i)
    }
    for(i in 3 downTo 0) {
        println(i)
    }
    var x = 1
    if (x !in 1..4) {
        println(x)
    }
}


// Equality Checks: == or ===
// == for structural comparison and === for referential comparison

// special classes
data class User(val name: String, val id: Int)

fun dataClassFunctions() {
    val user = User("Alex", 1)
    println(user)

    val secondUser = User("Alex", 1)
    val thirdUser = User("Max", 2)

    println("user == secondUser: ${user == secondUser}")
    println("user == thirdUser: ${user == thirdUser}")

    println(user.hashCode())
    println(thirdUser.hashCode())

    // copy() function
    println(user.copy())
    println(user.copy("Max"))
    println(user.copy(id = 2))
    // get the values of properties in the order of declaration.
    println("name = ${user.component1()}")
    println("id = ${user.component2()}")
}


enum class State {
    IDLE, RUNNING, FINISHED
}

fun enumClass() {
    val state = State.RUNNING
    val message = when (state) {
        State.IDLE -> "It's idle"
        State.RUNNING -> "It's running"
        State.FINISHED -> "It's finished"
    }
    println(message)
}

enum class Color(private val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF),
    YELLOW(0xFFFF00);

    fun containsRed() = (this.rgb and 0xFF0000 != 0)
}

fun enumClassWithParameter() {
    val red = Color.RED
    println(red)
    println(red.containsRed())
    println(Color.BLUE.containsRed())
}

// restrict the use of inheritance: Note that all subclasses must be in the same file
sealed class Mammal(val name: String)                                                   // 1

class Cat(val catName: String) : Mammal(catName)                                        // 2
class Human(val humanName: String, val job: String) : Mammal(humanName)

fun greetMammal(mammal: Mammal): String {
    when (mammal) {                                                                     // 3
        is Human -> return "Hello ${mammal.name}; You're working as a ${mammal.job}"    // 4
        is Cat -> return "Hello ${mammal.name}" // 5
        // With a non-sealed superclass else would be required
        // else -> return ""
    }                                                                                   // 6
}

fun greet() {
    println(greetMammal(Cat("Snowy")))
}

// object keyword : object {}, object Declaration, Companion Objects
fun rentPrice(standardDays: Int, festivityDays: Int, specialDays: Int): Unit {  //1

    val dayRates = object {                                                     //2
        var standard: Int = 30 * standardDays
        var festivity: Int = 50 * festivityDays
        var special: Int = 100 * specialDays
    }

    val total = dayRates.standard + dayRates.festivity + dayRates.special       //3

    print("Total price: $$total")                                               //4

}

// you call object members using its class name as a qualifier
// consider using a package-level function instead
class BigBen {                                  //1
    companion object Bonger {                   //2
        fun getBongs(nTimes: Int) {             //3
            for (i in 1 .. nTimes) {
                print("BONG ")
            }
        }
    }
}
fun callCompanionObject() {
    BigBen.getBongs(12)                         //4
}


// functional:
// :: is the notation that references a function by name in Kotlin.

fun calculate(x: Int, y: Int, operation: (Int, Int) -> Int): Int {  // 1
    return operation(x, y)                                          // 2
}

fun sum(x: Int, y: Int) = x + y                                     // 3

fun higherOrderFunction() {
    val sumResult = calculate(4, 5, ::sum)                          // 4
    val mulResult = calculate(4, 5) { a, b -> a * b }               // 5
    println("sumResult $sumResult, mulResult $mulResult")
}

fun operation(): (Int) -> Int {                                     // 1
    return :: square
}

fun operation2(x:Int, oper:(Int) -> Int) :  Int {                                     // 1
    return oper(x)
}

fun square(x: Int) = x * x                                          // 2

fun functional() {
    val func = operation() // 3
    var func2 = operation2(2) {a -> a * 1}
    println(func(2))  // 4
    println(func2)
}