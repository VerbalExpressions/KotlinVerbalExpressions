package co.zsmb.verbalexpressions

import org.junit.Assert.*
import org.junit.Test

class SimpleTests {

    @Test
    fun startOfLine() {
        val verex = VerEx()
                .startOfLine()
                .then("a")

        assertTrue("apple" matches verex)
        assertFalse("banana" matches verex)
    }

    @Test
    fun endOfLine() {
        val verex = VerEx()
                .then("a")
                .endOfLine()

        assertFalse("apple" matches verex)
        assertTrue("banana" matches verex)
    }

    @Test
    fun find1() {
        val verex = VerEx()
                .find("b")

        assertTrue("abc" matches verex)
    }

    @Test
    fun find2() {
        val verex = VerEx()
                .find("world")

        assertTrue("Hello world" matches verex)
    }

    @Test
    fun then1() {
        val verex = VerEx()
                .then("b")

        assertTrue("abc" matches verex)
    }

    @Test
    fun then2() {
        val verex = VerEx()
                .then("llo wor")

        assertTrue("Hello world" matches verex)
    }

    @Test
    fun maybe() {
        val verex = VerEx()
                .then("apple")
                .maybe(" tree")

        assertTrue("apple tree" matches verex)
        assertTrue("apple" matches verex)
    }

    @Test
    fun anything1() {
        val verex = VerEx()
                .anything()

        assertTrue("" matches verex)
        assertTrue("qwerty" matches verex)
        assertTrue("123456789" matches verex)
    }

    @Test
    fun anything2() {
        val verex = VerEx()
                .then("a")
                .anything()
                .then("a")

        assertTrue("aa" matches verex)
        assertTrue("aqwertya" matches verex)
        assertTrue("a123456789a" matches verex)
    }

    @Test
    fun anythingBut1() {
        val verex = VerEx()
                .anythingBut("abcd")

        assertTrue("" matches verex)
        assertTrue("eleven" matches verex)
        assertTrue("123456" matches verex)
    }

    @Test
    fun anythingBut2() {
        val verex = VerEx()
                .startOfLine()
                .anythingBut("abcd")
                .endOfLine()

        assertFalse("apple" matches verex)
        assertFalse("banana" matches verex)
    }

    @Test
    fun something1() {
        val verex = VerEx()
                .something()

        assertTrue("qwerty" matches verex)
        assertTrue("123456789" matches verex)

        assertFalse("" matches verex)
    }

    @Test
    fun something2() {
        val verex = VerEx()
                .then("a")
                .something()
                .then("a")

        assertTrue("aqwertya" matches verex)
        assertTrue("a123456789a" matches verex)

        assertFalse("aa" matches verex)
    }

    @Test
    fun somethingBut1() {
        val verex = VerEx()
                .somethingBut("abcd")

        assertTrue("eleven" matches verex)
        assertTrue("123456" matches verex)

        assertFalse("" matches verex)
    }

    @Test
    fun somethingBut2() {
        val verex = VerEx()
                .startOfLine()
                .somethingBut("abcd")
                .endOfLine()

        assertFalse("apple" matches verex)
        assertFalse("banana" matches verex)
    }

    @Test
    fun lineBreak() {
        val verex = VerEx()
                .then("a")
                .lineBreak()
                .then("b")

        assertTrue("a\nb" matches verex)
    }

    @Test
    fun br() {
        val verex = VerEx()
                .then("a")
                .br()
                .then("b")

        assertTrue("a\nb" matches verex)
    }

    @Test
    fun tab() {
        val verex = VerEx()
                .then("a")
                .tab()
                .then("b")

        assertTrue("a\tb" matches verex)
    }

    @Test
    fun word() {
        val verex = VerEx()
                .then("Hello ")
                .word()

        assertTrue("Hello world" matches verex)
        assertTrue("Hello Jane" matches verex)
        assertTrue("Hello 123" matches verex)
        assertTrue("Hello 1_2_3_a_b_c" matches verex)

        assertFalse("Hello " matches verex)
        assertFalse("Hello +++" matches verex)
        assertFalse("Hello ***" matches verex)
    }

    @Test
    fun anyOf() {
        val verex = VerEx()
                .anyOf("abcd")

        assertTrue("a" matches verex)
        assertTrue("d" matches verex)

        assertFalse("hello" matches verex)
        assertFalse("12345" matches verex)
    }

    @Test
    fun any() {
        val verex = VerEx()
                .any("abcd")

        assertTrue("a" matches verex)
        assertTrue("d" matches verex)

        assertFalse("hello" matches verex)
        assertFalse("12345" matches verex)
    }

    @Test
    fun withAnyCase1() {
        val verex = VerEx()
                .then("apple")
                .withAnyCase()

        assertTrue("apple" matches verex)
        assertTrue("Apple" matches verex)
        assertTrue("aPpLe" matches verex)
    }

    @Test
    fun withAnyCase2() {
        val verex = VerEx()
                .then("apple")
                .withAnyCase(false)

        assertTrue("apple" matches verex)

        assertFalse("Apple" matches verex)
        assertFalse("aPpLe" matches verex)
    }

    @Test
    fun searchOneLine1() {
        val verex = VerEx()
                .startOfLine()
                .anything()
                .then("b")
                .anything()
                .endOfLine()
                .searchOneLine()

        assertFalse("a\nb" matches verex)
    }

    @Test
    fun searchOneLine2() {
        val verex = VerEx()
                .startOfLine()
                .anything()
                .then("b")
                .anything()
                .endOfLine()
                .searchOneLine(false)

        assertTrue("a\nb" matches verex)
    }

    @Test
    fun or() {
        val verex = VerEx()
                .startOfLine()
                .then("a")
                .or("b")
                .then("c")

        assertTrue("ac" matches verex)
        assertTrue("bc" matches verex)
    }

    @Test
    fun multiple1() {
        val verex = VerEx()
                .startOfLine()
                .multiple("a")
                .endOfLine()

        assertTrue("a" matches verex)
        assertTrue("aaaa" matches verex)
        assertTrue("aaaaaaa" matches verex)

        assertFalse("" matches verex)
        assertFalse("b" matches verex)
    }

    @Test
    fun multiple2() {
        val verex = VerEx()
                .startOfLine()
                .multiple("a", 3)
                .endOfLine()

        assertTrue("aaa" matches verex)
        assertTrue("aaaaa" matches verex)

        assertFalse("" matches verex)
        assertFalse("a" matches verex)
        assertFalse("b" matches verex)
    }

    @Test
    fun multiple3() {
        val verex = VerEx()
                .startOfLine()
                .multiple("a", 3, 5)
                .endOfLine()

        assertTrue("aaa" matches verex)
        assertTrue("aaaa" matches verex)
        assertTrue("aaaaa" matches verex)

        assertFalse("" matches verex)
        assertFalse("aa" matches verex)
        assertFalse("aaaaaa" matches verex)
        assertFalse("b" matches verex)
    }

    @Test
    fun count1() {
        val verex = VerEx()
                .startOfLine()
                .then("a").times()
                .endOfLine()

        assertTrue("a" matches verex)
        assertTrue("aaaa" matches verex)
        assertTrue("aaaaaaa" matches verex)

        assertFalse("" matches verex)
        assertFalse("b" matches verex)
    }

    @Test
    fun count2() {
        val verex = VerEx()
                .startOfLine()
                .then("a").times(3)
                .endOfLine()

        assertTrue("aaa" matches verex)
        assertTrue("aaaaa" matches verex)

        assertFalse("" matches verex)
        assertFalse("a" matches verex)
        assertFalse("b" matches verex)
    }

    @Test
    fun count3() {
        val verex = VerEx()
                .startOfLine()
                .then("a").times(3, 5)
                .endOfLine()

        assertTrue("aaa" matches verex)
        assertTrue("aaaa" matches verex)
        assertTrue("aaaaa" matches verex)

        assertFalse("" matches verex)
        assertFalse("aa" matches verex)
        assertFalse("aaaaaa" matches verex)
        assertFalse("b" matches verex)
    }

    @Test
    fun atLeast() {
        val verex = VerEx()
                .then("a")
                .atLeast(3)

        assertTrue("aaa" matches verex)
        assertTrue("aaaaa" matches verex)

        assertFalse("" matches verex)
        assertFalse("aa" matches verex)
    }

    @Test
    fun replace() {
        val verex = VerEx()
                .then("a").oneOrMore()

        assertEquals("hello", verex.replace("haaaaallo", "e"))
    }

    @Test
    fun range1() {
        val verex = VerEx()
                .range(1 to 5)

        assertTrue("1" matches verex)
        assertTrue("3" matches verex)
        assertTrue("5" matches verex)

        assertFalse("0" matches verex)
        assertFalse("6" matches verex)
        assertFalse("a" matches verex)
    }

    @Test
    fun range2() {
        val verex = VerEx()
                .startOfLine()
                .range(1 to 5, "a" to "d").oneOrMore()
                .endOfLine()

        assertTrue("12345" matches verex)
        assertTrue("abcd" matches verex)

        assertFalse("0" matches verex)
        assertFalse("6" matches verex)
        assertFalse("e" matches verex)
    }

    @Test
    fun beginCapture_endCapture() {
        val verex = VerEx()
                .beginCapture()
                .then("aaa")
                .endCapture()
        val matcher = verex.pattern.matcher("baaab")
        matcher.find()

        assertEquals("aaa", matcher.group())
    }

    @Test
    fun whiteSpace() {
        val verex = VerEx()
                .startOfLine()
                .whiteSpace()
                .endOfLine()

        assertTrue(" " matches verex)
        assertTrue("\t" matches verex)
        assertTrue("\n" matches verex)
        assertTrue("\n\r" matches verex)
        assertTrue("\r" matches verex)

        assertFalse("a" matches verex)
        assertFalse("1" matches verex)
    }

    @Test
    fun oneOrMore() {
        val verex = VerEx()
                .then("a")
                .then("b")
                .oneOrMore()
                .then("a")

        assertTrue("aba" matches verex)
        assertTrue("abba" matches verex)

        assertFalse("aa" matches verex)
    }

    @Test
    fun zeroOrMore() {
        val verex = VerEx()
                .then("a")
                .then("b")
                .zeroOrMore()
                .then("a")

        assertTrue("aa" matches verex)
        assertTrue("aba" matches verex)
        assertTrue("abba" matches verex)
    }

    @Test
    fun addModifier() {
        val verex = VerEx()
                .then("apple")
                .addModifier("i")
                .removeModifier("i")
                .addModifier("i")

        assertTrue("apple" matches verex)
        assertTrue("Apple" matches verex)
        assertTrue("aPpLe" matches verex)
    }

    @Test
    fun removeModifier() {
        val verex = VerEx()
                .then("apple")
                .removeModifier('i')
                .addModifier('i')
                .removeModifier('i')

        assertTrue("apple" matches verex)

        assertFalse("Apple" matches verex)
        assertFalse("aPpLe" matches verex)
    }

}
