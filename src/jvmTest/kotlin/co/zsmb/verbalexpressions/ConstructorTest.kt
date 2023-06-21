package co.zsmb.verbalexpressions

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ConstructorTest {

    @Test
    fun useConstructor() {
        val verex = VerEx {
            startOfLine()
            then("http")
            maybe("s")
            then("://")
            maybe("www")
            anythingBut(" ")
            endOfLine()
        }

        assertTrue("https://www.google.com" matches verex)
    }
}
