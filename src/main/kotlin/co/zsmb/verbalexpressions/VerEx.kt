package co.zsmb.verbalexpressions

import java.util.regex.Pattern

class VerEx {

    companion object {

        private val symbols = mapOf(
                'd' to Pattern.UNIX_LINES,
                'i' to Pattern.CASE_INSENSITIVE,
                'x' to Pattern.COMMENTS,
                'm' to Pattern.MULTILINE,
                's' to Pattern.DOTALL,
                'u' to Pattern.UNICODE_CASE,
                'U' to Pattern.UNICODE_CHARACTER_CLASS
        )

    }

    val pattern: Pattern
        get() = Pattern.compile("$prefixes$source$suffixes", modifiers)

    private var prefixes = StringBuilder()
    private var source = StringBuilder()
    private var suffixes = StringBuilder()
    private var modifiers = Pattern.MULTILINE

    //// TESTS ////

    fun testExact(toTest: String?) = if (toTest == null) false else pattern.matcher(toTest).find()

    //// COMPOSITION ////

    fun startOfLine(enabled: Boolean = true): VerEx {
        prefixes = StringBuilder(if (enabled) "^" else "")
        return this
    }

    fun endOfLine(enabled: Boolean = true): VerEx {
        suffixes = StringBuilder(if (enabled) "$" else "")
        return this
    }

    fun find(str: String) = then(str)

    fun then(str: String) = add("(?:${sanitize(str)})")

    fun maybe(str: String) = add("(?:${sanitize(str)})?")

    fun anything() = add("(?:.*)")

    fun anythingBut(str: String) = add("(?:[^${sanitize(str)}]*)")

    fun something() = add("(?:.+)")

    fun somethingBut(str: String) = add("(?:[^${sanitize(str)}]+)")

    fun lineBreak() = add("""(?:(?:\n)|(?:\r\n))""")

    fun br() = lineBreak()

    fun tab() = add("""\t""")

    fun word() = add("""\w+""")

    fun anyOf(str: String) = add("(?:[${sanitize(str)}])")

    fun any(str: String) = anyOf(str)

    fun withAnyCase(enabled: Boolean = true) = updateModifier('i', enabled)

    fun searchOneLine(enabled: Boolean = true) = updateModifier('m', !enabled)

    fun or(str: String): VerEx {
        prefixes.append("(")
        source.append(")|(").append(str).append(")").append(suffixes)
        suffixes = StringBuilder()

        return this
    }

    fun multiple(str: String, min: Int? = null, max: Int? = null): VerEx {
        then(str)
        return times(min ?: 1, max)
    }

    fun times(min: Int, max: Int? = null): VerEx {
        if (max != null && min > max) {
            throw IllegalArgumentException("Min count ($min) can't be less than max count ($max).")
        }
        return add("{$min,${max ?: ""}}")
    }

    fun exactly(count: Int) = times(count, count)

    fun atLeast(min: Int) = times(min)

    fun replace(source: String, replacement: String): String = pattern.matcher(source).replaceAll(replacement)

    fun range(vararg args: Pair<Any, Any>) =
            add(args.joinToString(prefix = "[", postfix = "]", separator = "") { "${it.first}-${it.second}" })

    fun beginCapture() = add("(")

    fun endCapture() = add(")")

    fun whiteSpace() = add("""\s""")

    fun oneOrMore() = add("+")

    fun zeroOrMore() = add("*")

    fun addModifier(modifier: Char): VerEx {
        VerEx.Companion.symbols[modifier]?.let {
            modifiers = modifiers or it
        }
        return this
    }

    fun removeModifier(modifier: Char): VerEx {
        VerEx.Companion.symbols[modifier]?.let {
            modifiers = modifiers and it.inv()
        }
        return this
    }

    fun addModifier(modifier: String): VerEx {
        if (modifier.length != 1) {
            throw IllegalArgumentException("Modifier has to be a single character")
        }
        return addModifier(modifier[0])
    }

    fun removeModifier(modifier: String): VerEx {
        if (modifier.length != 1) {
            throw IllegalArgumentException("Modifier has to be a single character")
        }
        return removeModifier(modifier[0])
    }

    //// PRIVATE HELPERS ////

    private fun add(str: String): VerEx {
        source.append(str)
        return this
    }

    private fun sanitize(str: String) = str.replace("[\\W]".toRegex(), "\\\\$0")

    private fun updateModifier(modifier: Char, enabled: Boolean) =
            if (enabled) addModifier(modifier)
            else removeModifier(modifier)

}
