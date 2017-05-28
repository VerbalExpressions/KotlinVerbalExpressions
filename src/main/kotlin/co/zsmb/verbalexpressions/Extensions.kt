package co.zsmb.verbalexpressions

infix fun String?.matches(verex: VerEx) = verex.test(this)

infix fun String?.matchesExact(verex: VerEx) = verex.testExact(this)
