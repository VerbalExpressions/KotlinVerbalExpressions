package co.zsmb.verbalexpressions

import java.util.regex.PatternSyntaxException
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ExceptionTests {

    @Test
    fun `times called with larger min value than max`() {
        assertFailsWith<IllegalArgumentException> {
            VerEx()
                    .then("a")
                    .times(2, 1)
        }
    }

    @Test
    fun `non matching number of begin and end capture calls 1`() {
        val verex = VerEx()
                .beginCapture()
                .then("a")
        assertFailsWith<PatternSyntaxException> {
            "a" matches verex
        }
    }

    @Test
    fun `non matching number of begin and end capture calls 2`() {
        val verex = VerEx()
                .beginCapture()
                .then("a")
                .endCapture()
                .beginCapture()
        assertFailsWith<PatternSyntaxException> {
            "a" matches verex
        }
    }

    @Test
    fun `invalid nesting of capture parentheses`() {
        val verex = VerEx()
                .beginCapture()
                .endCapture()
                .endCapture()
                .beginCapture()
        assertFailsWith<PatternSyntaxException> {
            "a" matches verex
        }
    }

    @Test
    fun `attempting to add modifier of zero length`() {
        assertFailsWith<IllegalArgumentException> {
            VerEx().addModifier("")
        }
    }

    @Test
    fun `attempting to add modifier longer th}an one character`() {
        assertFailsWith<IllegalArgumentException> {
            VerEx().addModifier("aa")
        }
    }

    @Test
    fun `attempting to remove modifier of zer}o length`() {
        assertFailsWith<IllegalArgumentException> {
            VerEx().removeModifier("")
        }
    }

    @Test
    fun `attempting to remove modifier longer} than one character`() {
        assertFailsWith<IllegalArgumentException> {
            VerEx().removeModifier("aa")
        }
    }
}
