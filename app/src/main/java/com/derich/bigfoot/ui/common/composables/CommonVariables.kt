package com.derich.bigfoot.ui.common.composables

import android.content.Context
import android.widget.Toast
import com.derich.bigfoot.model.MemberDetails
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object CommonVariables {
    lateinit var CURRENT_USER_DETAILS: MemberDetails
    private const val PREVIOUS_CONTRIBUTION_AMOUNT: Int=20
    private const val CURRENT_CONTRIBUTION_AMOUNT: Int=35
    const val CURRENCY: String="KSH"
    private const val GROUP_START_DATE: String = "31/12/2019"
    private const val NEW_CONTRIBUTION_START_DATE: String = "29/02/2024"

    enum class MemberRole {
        ADMIN,
        MEMBER
    }
    //create an extension function to display Toast message
    fun Context.showMessage(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    fun String.toIntOrZero(): Int {
        return try {
            this.toInt()
        } catch (e: NumberFormatException) {
            0
        }
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

    //this function calculates the difference in amount from the expected amount and current amount
    fun calculateContributionsDifference(totalAmount: Int) : Int {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        val previousBalance: Int
        //first calculate the first contribution up-to 29th feb 2024
        //then calculate from then up-to date using 35ksh
        //this formula is the old version which was being used to calculate the contributions when the contributions
        //we're ksh 20 per day
        val prevDate = sdf.parse(NEW_CONTRIBUTION_START_DATE)
        val prevStartDate = sdf.parse(GROUP_START_DATE)
        val diff: Long = prevDate!!.time - prevStartDate!!.time
        val daysRemainingInPrevContribution: Long = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        previousBalance= totalAmount - (daysRemainingInPrevContribution.toInt() * PREVIOUS_CONTRIBUTION_AMOUNT)

        //this new formula calculates contributions from 1/3/2024 when the transactions changed to ksh 35 per day
        //totalAmount is now the total amount less the 30420
        val currentDate = sdf.format(Date())
        val currDate = sdf.parse(currentDate)
        val newStartDate = sdf.parse(NEW_CONTRIBUTION_START_DATE)
        val diffInNewDates: Long = currDate!!.time - newStartDate!!.time
        val daysRemaining: Long = TimeUnit.DAYS.convert(diffInNewDates, TimeUnit.MILLISECONDS)
        return (previousBalance - (daysRemaining.toInt() * CURRENT_CONTRIBUTION_AMOUNT))


    }
}