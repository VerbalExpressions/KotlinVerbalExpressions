package verbalexpressions

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

    private val pattern: Pattern
        get() {
            val compile = Pattern.compile("$prefixes$source$suffixes", modifiers)
            println(compile)
            return compile
        }

    private var prefixes = StringBuilder()
    private var source = StringBuilder()
    private var suffixes = StringBuilder()
    private var modifiers = Pattern.MULTILINE

    //// TESTS ////

    fun testExact(toTest: String) = pattern.matcher(toTest).find()

    //// COMPOSITION ////

    fun add(str: String): VerEx {
        source.append(str)
        return this
    }

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

    fun lineBreak() = add("""(?:(?:\n)|(?:\r\n)""")

    fun br() = lineBreak()

    fun tab() = add("""\t""")

    fun word() = add("""\w+""")

    fun anyOf(str: String) = add("(?:[${sanitize(str)}])")

    fun any(str: String) = anyOf(str)

    fun withAnyCase(enabled: Boolean = true) = updateModifier('i', enabled)

    fun searchOneLine(enabled: Boolean = true) = updateModifier('m', !enabled)

    fun or(str: String): VerEx {
        prefixes = StringBuilder().append("(").append(prefixes)
        source.append(")|(").append(str).append(")").append(suffixes)
        suffixes = StringBuilder()

        return this
    }

    fun multiple(str: String, min: Int? = null, max: Int? = null): VerEx {
        then(str)
        return count(min, max)
    }

    fun count(min: Int? = null, max: Int? = null): VerEx {
        if(min != null && max != null && min > max) {
            throw IllegalArgumentException("Min count ($min) can't be less than max count ($max).")
        }
        return add("{${min ?: "1"},${max ?: ""}}")
    }

    fun atLeast(min: Int) = count(min)

    fun replace(source: String, replacement: String): String {
        return pattern.matcher(source).replaceAll(replacement)
    }

    fun range(vararg args: Pair<Any, Any>): VerEx {
        return add(args.joinToString(prefix = "[", postfix = "]", separator = "") { "${it.first}-${it.second}" })
    }

    fun beginCapture() = add("(")

    fun endCapture() = add(")")

    fun whiteSpace() = add("""\s""")

    fun oneOrMore(str: String) = add("${sanitize(str)}+")

    fun zeroOrMore(str: String) = add("${sanitize(str)}*")

    //// HELPERS ////

    private fun sanitize(str: String): String {
        return str.replace("[\\W]".toRegex(), "\\\\$0")
    }

    private fun updateModifier(modifier: Char, enabled: Boolean) =
            if (enabled) addModifier(modifier)
            else removeModifier(modifier)

    private fun addModifier(modifier: Char): VerEx {
        symbols[modifier]?.let {
            modifiers = modifiers or it
        }
        return this
    }

    private fun removeModifier(modifier: Char): VerEx {
        symbols[modifier]?.let {
            modifiers = modifiers and it.inv()
        }
        return this
    }


}
