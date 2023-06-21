package co.zsmb.verbalexpressions

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MatcherTests {

    @Test
    fun matches() {
        val verex = VerEx()
            .startOfLine()
            .then("a")

        assertTrue("apple" matches verex)
        assertTrue("art" matches verex)

        assertFalse("banana" matches verex)
    }

    @Test
    fun matchesExact() {
        val verex = VerEx()
            .startOfLine()
            .then("a")

        assertTrue("a" matchesExact verex)

        assertFalse("apple" matchesExact verex)
        assertFalse("art" matchesExact verex)
        assertFalse("banana" matchesExact verex)
    }

    @Test
    fun test() {
        val verex = VerEx()
            .startOfLine()
            .then("a")

        assertTrue(verex.test("apple"))
        assertTrue(verex.test("art"))

        assertFalse(verex.test("banana"))
    }

    @Test
    fun testExact() {
        val verex = VerEx()
            .startOfLine()
            .then("a")

        assertTrue(verex.testExact("a"))

        assertFalse(verex.testExact("apple"))
        assertFalse(verex.testExact("art"))
        assertFalse(verex.testExact("banana"))
    }

    @Test
    fun `empty verex matches anything except null`() {
        val verex = VerEx()

        assertTrue("" matches verex)
        assertTrue("abc" matches verex)
        assertTrue("123" matches verex)

        assertFalse(null matches verex)
    }

    @Test
    fun `empty verex mathes empty string exactly`() {
        val verex = VerEx()

        assertTrue("" matchesExact verex)
    }

}
