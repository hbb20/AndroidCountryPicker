fun generateToDo(postfix: String): String{
    val keywords = mutableListOf("CP", "TODO")
    keywords.addAll(postfix.split(" "))
    return keywords.joinToString("_")
}