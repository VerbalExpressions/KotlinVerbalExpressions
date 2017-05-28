package co.zsmb.verbalexpressions

import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.regex.PatternSyntaxException

class ExceptionTests {

    @Test(expected = IllegalArgumentException::class)
    fun `times called with larger min value than max`() {
        val verex = VerEx()
                .then("a")
                .times(2, 1)
    }

    @Test(expected = PatternSyntaxException::class)
    fun `non matching number of begin and end capture calls 1`() {
        val verex = VerEx()
                .beginCapture()
                .then("a")

        "a" matches verex
    }

    @Test(expected = PatternSyntaxException::class)
    fun `non matching number of begin and end capture calls 2`() {
        val verex = VerEx()
                .beginCapture()
                .then("a")
                .endCapture()
                .beginCapture()

        "a" matches verex
    }

    @Test(expected = PatternSyntaxException::class)
    fun `invalid nesting of capture parentheses`() {
        val verex = VerEx()
                .beginCapture()
                .endCapture()
                .endCapture()
                .beginCapture()

        "a" matches verex
    }

    @Test(expected = IllegalArgumentException::class)
    fun `attempting to add modifier of zero length`() {
        val verex = VerEx()
                .addModifier("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `attempting to add modifier longer than one character`() {
        val verex = VerEx()
                .addModifier("aa")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `attempting to remove modifier of zero length`() {
        val verex = VerEx()
                .removeModifier("")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `attempting to remove modifier longer than one character`() {
        val verex = VerEx()
                .removeModifier("aa")
    }

}

