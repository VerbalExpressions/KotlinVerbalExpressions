package co.zsmb.verbalexpressions

import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleTest {

    @Test
    fun testUrls() {
        val verex = VerEx()
                .startOfLine()
                .then("http")
                .maybe("s")
                .then("://")
                .maybe("www")
                .anythingBut(" ")
                .endOfLine()

        assertTrue("https://www.google.com" matches verex)
        assertTrue("http://zsmb.co" matches verex)
        assertTrue("https://www.wikipedia.org/" matches verex)
    }

}
