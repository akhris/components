package strings

import org.junit.jupiter.api.Test

internal class StringsIDsTest {

    sealed class stringID() {
        object a : stringID()
        object b : stringID()
        object hello : stringID()
        sealed class wow : stringID(){
            object a_wow : wow()
            object b_wow : wow()
        }
    }

    @Test
    fun objectToString() {
        stringID::class.sealedSubclasses.forEach {
            println(it.simpleName)
        }
        val a: stringID

    }
}