package com.derich.bigfoot

interface Destinations{
    val route: String
}
object Home:Destinations{
    override val route: String = "Home"
}
object TransactionsList:Destinations{
    override val route: String= "TransactionsList"
}