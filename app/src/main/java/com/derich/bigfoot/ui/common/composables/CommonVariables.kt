package com.derich.bigfoot.ui.common.composables

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.ui.common.composables.CommonVariables.CURRENT_CONTRIBUTION_AMOUNT
import com.derich.bigfoot.ui.common.composables.CommonVariables.GROUP_START_DATE
import com.derich.bigfoot.ui.common.composables.CommonVariables.NEW_CONTRIBUTION_START_DATE
import com.derich.bigfoot.ui.common.composables.CommonVariables.PREVIOUS_CONTRIBUTION_AMOUNT
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object CommonVariables {
    val CURRENT_USER: String? = null
    const val PREVIOUS_CONTRIBUTION_AMOUNT: Int=20
    const val CURRENT_CONTRIBUTION_AMOUNT: Int=35
    const val CURRENCY: String="KSH"
    const val GROUP_START_DATE: String = "31/12/2019"
    const val NEW_CONTRIBUTION_START_DATE: String = "29/02/2024"
}
enum class MemberRole {
    ADMIN,
    MEMBER
}
//create an extension function to display Toast message
fun Context.showMessage(message:String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

//calculate the resulting date
fun calculateResultingDate(newUserAmount: Int): String {
    //do this if the user has contributed less than 30440 which is 29/02/2024
    var sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    val c: Calendar = Calendar.getInstance()
    if (newUserAmount <30440){
        val totalDays: Int = (newUserAmount / PREVIOUS_CONTRIBUTION_AMOUNT)
        //add number of days to the start date
        c.time = sdf.parse(GROUP_START_DATE)!!
        c.add(Calendar.DATE, totalDays)
        sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return sdf.format(Date(c.timeInMillis))
    }
    else{
        //do this if the user has contributed more than 30440
        val totalDays: Int = ((newUserAmount-30420) / CURRENT_CONTRIBUTION_AMOUNT)
        //add number of days to the start date
        c.time = sdf.parse(NEW_CONTRIBUTION_START_DATE)!!
        c.add(Calendar.DATE, totalDays)
        sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return sdf.format(Date(c.timeInMillis))
    }
}
//calculate the total member contributions to display on the home screen
fun calculateTotalContributions(allMemberInfo: State<List<MemberDetails>>): Int {
    var totalAmount =0
    allMemberInfo.value.forEach {
        totalAmount+=it.totalAmount.toInt()
    }
    return totalAmount
}

//this function calculates the difference in amount from the expected amount and current amount
fun calculateContributionsDifference(totalAmount: Int) : Int {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    if (totalAmount<30440){
        //this formula is the old version which was being used to calculate the contributions when the contributions
        //we're ksh 20 per day
        val currDate = sdf.parse(NEW_CONTRIBUTION_START_DATE)
        val startDate = sdf.parse("31/12/2019")
        val diff: Long = currDate!!.time - startDate!!.time
        val daysRemaining: Long = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        return (totalAmount - (daysRemaining.toInt() * PREVIOUS_CONTRIBUTION_AMOUNT))
    }
    else{
        //this new formula calculates contributions from 1/3/2024 when the transactions changed to ksh 35 per day
        //totalAmount is now the total amount less the 30420
        val newTotalAmount = totalAmount- 30420
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val currentDate = sdf.format(Date())
        val currDate = sdf.parse(currentDate)
        val startDate = sdf.parse("29/02/2024")
        val diff: Long = currDate!!.time - startDate!!.time
        val daysRemaining: Long = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        return (newTotalAmount - (daysRemaining.toInt() * CURRENT_CONTRIBUTION_AMOUNT))

    }

}