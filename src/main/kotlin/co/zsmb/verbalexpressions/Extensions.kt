package co.zsmb.verbalexpressions

infix fun String?.matches(verex: VerEx) = verex.testExact(this)
