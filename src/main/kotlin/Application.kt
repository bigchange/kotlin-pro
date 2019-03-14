import kotlin.properties.Delegates
import kotlin.reflect.KProperty

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
        extensionFunctions()
        result()
        applyAndAlso()
        LazySample().ifChange()
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

    val sophia = Person1("Sophia")
    val claudia = Person1("Claudia")
    sophia likes claudia                                       // 5
}

class Person1(val name: String) {
    val likedPeople = mutableListOf<Person1>()
    infix fun likes(other: Person1) { likedPeople.add(other) }  // 6
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
    return ::square
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

fun lambda() {
    // All examples create a function object that performs upper-casing.
    // So it's a function from String to String

    val upperCase1: (String) -> String = { str: String -> str.toUpperCase() } // 1

    val upperCase2: (String) -> String = { str -> str.toUpperCase() }         // 2

    val upperCase3 = { str: String -> str.toUpperCase() }                     // 3

    // val upperCase4 = { str -> str.toUpperCase() }     // wrong             // 4

    val upperCase5: (String) -> String = { it.toUpperCase() }                 // 5

    val upperCase6: (String) -> String = String::toUpperCase                 // 6

    println(upperCase2("hello"))
    println(upperCase1("hello"))
    println(upperCase3("hello"))
    println(upperCase5("hello"))
    println(upperCase6("hello"))
}

// extension functions

data class Item(val name: String, val price: Float)                                   // 1

data class Order(val items: Collection<Item>) {
    fun maxPricedItemValue() :Float {
        return items.maxBy { it.price }?.price?:0F
    }
}

// fun Order.maxPricedItemValue(): Float = this.items.maxBy { it.price }?.price ?: 0F    // 2
fun Order.maxPricedItemName() = this.items.maxBy { it.price }?.name ?: "NO_PRODUCTS"

// extension properties
val Order.commaDelimitedItemNames: String
    get() = this.items.map { it.name }.joinToString()

fun extensionFunctions() {
    val order = Order(listOf(Item("Bread", 25.0F), Item("Wine", 29.0F), Item("Water", 12.0F)))

    println("Max priced item name: ${order.maxPricedItemName()}")                     // 4
    println("Max priced item value: ${order.maxPricedItemValue()}")
    println("Items: ${order.commaDelimitedItemNames}")                                // 5

}

// check null code
fun <T> T?.nullSafeToString() = this?.toString() ?: "NULL"

// Collections
// List
val systemUsers: MutableList<Int> = mutableListOf(1, 2, 3)  // mutable
val sudoers: List<Int> = systemUsers   // read-only

// Set
// Map
// 1 -> 100, 2 -> 100, 3 -> 100
val EZPassAccounts: MutableMap<Int, Int> = mutableMapOf(1 to 100, 2 to 100, 3 to 100)
val EZPassReport: Map<Int, Int> = EZPassAccounts

// filter, map

// any, all, none

val numbers = listOf(1, -2, 3, -4, 5, -6)            // 1

val anyNegative = numbers.any { it < 0 }             // 2

val anyGT6 = numbers.any { it > 6 }                  // 3


// find, findLast， first, last, firstOrNull, lastOrNull

// count

// associateBy, groupBy - keySelector, valueSelector

// The difference between associateBy and groupBy is how they process objects with the same key:
// associateBy uses the last suitable element as the value. groupBy builds a list of all suitable
// elements and puts it in the value.

data class Person(val name: String, val city: String, val phone: String) // 1

val people = listOf(                                                     // 2
        Person("John", "Boston", "+1-888-123456"),
        Person("Sarah", "Munich", "+49-777-789123"),
        Person("Svyatoslav", "Saint-Petersburg", "+7-999-456789"),
        Person("Vasilisa", "Saint-Petersburg", "+7-999-456789"))

val phoneBook = people.associateBy { it.phone }
// keySelector - phone, valueSelector - city
val cityBook = people.associateBy(Person::phone, Person::city)           // 4
val peopleCities = people.groupBy(Person::city, Person::name)

fun result() {
    println("People: $people")
    println("Phone book: $phoneBook")
    println("City book: $cityBook")
    println("People living in each city: $peopleCities")

}


// partition

// flatMap, min, max, sorted , sortedBy


// map element access

fun accessMap() {val map = mapOf("key" to 42)

    val value1 = map["key"]                                     // 1
    val value2 = map["key2"]                                    // 2

    val value3: Int = map.getValue("key")                       // 1

    val mapWithDefault = map.withDefault { k -> k.length }
    val value4 = mapWithDefault.getValue("key2")                // 3

    try {
        map.getValue("anotherKey")                              // 4
    }
    catch (e: NoSuchElementException) {
        println("Message: $e")
    }
}

// zip: zip function merges two given collections into a new collection. By default, the result collection contains Pairs of source collection elements with the same index

// getOrElse

fun getOrElse() {
    val list = listOf(0, 10, 20)
    println(list.getOrElse(1) { 42 })
    println(list.getOrElse(10) { 42 })

    val map = mutableMapOf<String, Int?>()
    println(map.getOrElse("x") { 1 })    // 1

    map["x"] = 3
    println(map.getOrElse("x") { 1 })     // 3

    map["x"] = null
    println(map.getOrElse("x") { 1 })   // 1
}


// scope function
// let, run , with , apply, also

fun printNonNull(str: String?) {
    println("Printing \"$str\":")

    str?.let {                         // 4
        println("str is not null = ${it.length}")
    }
}

// The difference between let is that inside run the object is accessed by this not it
fun getNullableLength(ns: String?) {
    println("for \"$ns\":")
    ns?.run {                                                  // 1
        println("\tis empty? " + isEmpty())                    // 2
        println("\tlength = ${this.length}")
        length                                                 // 3
    }
}

class Configuration(var host: String, var port: Int)

fun withFunction() {
    val configuration = Configuration(host = "127.0.0.1", port = 9000)

    with(configuration) {
        println("$host:$port")
    }

    // instead of:
    println("${configuration.host}:${configuration.port}")
}

data class People(var name: String, var age: Int, var about: String) {
    constructor() : this("", 0, "")
}

fun applyAndAlso() {
    val jake = People()                                     // 1
    val stringDescription = jake.apply {                    // 2
        this.name = "Jake"                                       // 3
        age = 30
        about = "Android developer"
    }.toString()                                            // 4
    println(stringDescription)

    jake.also {
        it.name = "jake-new"
    }
    println(jake.toString())
}


// Delegation Pattern (委派模式)
interface SoundBehavior {                                                          // 1
    fun makeSound()
}

class ScreamBehavior(val n:String): SoundBehavior {                                // 2
    override fun makeSound() = println("${n.toUpperCase()} !!!")
}

class RockAndRollBehavior(val n:String): SoundBehavior {                           // 2
    override fun makeSound() = println("I'm The King of Rock 'N' Roll: $n")
}

// Tom Araya is the "singer" of Slayer
class TomAraya(n:String): SoundBehavior by ScreamBehavior(n)                       // 3

// You should know ;)
class ElvisPresley(n:String): SoundBehavior by RockAndRollBehavior(n)              // 3


//Delegated Properties
class Example {
    var p: String by Delegate()                                               // 1

    override fun toString() = "Example Class"
}

class Delegate() {
    operator fun getValue(thisRef: Any?, prop: KProperty<*>): String {        // 2
        return "$thisRef, thank you for delegating '${prop.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, prop: KProperty<*>, value: String) { // 2
        println("$value has been assigned to ${prop.name} in $thisRef")
    }
}

// lazy observable
class LazySample {
    init {
        println("created!")            // 1
    }

    val lazyStr: String by lazy {
        println("computed!")          // 2
        "my lazy"
    }
    var observed = false
    var watch:String by Delegates.observable("you", { p, ol, nv ->
        observed = true
    })

    fun ifChange() {
        println(watch) // ""
        println("observed is ${observed}") // false
        watch = "i am changed"
        println(watch) // i am changed
        println("observed is ${observed}") // true
    }

}

// Productivity Boosters

// Named Arguments, String Templates,  Destructuring Declarations
fun format(userName: String, domain: String) = "$userName@$domain"

fun poductivityBoosters() {
    // Named Arguments
    println(format(userName = "foo", domain = "bar.com"))
    println(format(domain = "frog.com", userName = "pepe"))
    // String Templates
    val greeting = "Kotliner"
    println("Hello $greeting")                  // 1
    println("Hello ${greeting.toUpperCase()}")  // 2
}


data class UserClass(val username: String, val email: String)    // 1

fun getUser() = UserClass("Mary", "mary@somewhere.com")

fun getUserClass() {
    val user = getUser()
    val (username, email) = user                            // 2
    println(username == user.component1())                  // 3
    val (_, emailAddress) = getUser()                       // 4

}

class Pair<K, V>(val first: K, val second: V) {  // 1
    operator fun component1(): K {
        return first
    }
    operator fun component2(): V {
        return second
    }
}

fun pair() {
    val (num, name) = Pair(1, "one")             // 2

    println("num = $num, name = $name")
}

// finished guide of kotlin
