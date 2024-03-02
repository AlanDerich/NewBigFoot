package com.derich.bigfoot.ui.common.composables

import android.content.Context
import android.widget.Toast

object CommonVariables {
    var CURRENT_USER: String? = null
    var PreviousContribution: Int=20
    var CurrentContribution: Int=35
    var Currency: String="KSH"
}
enum class MemberRole {
    ADMIN,
    MEMBER
}
//create an extension function to display Toast message
fun Context.showMessage(message:String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}