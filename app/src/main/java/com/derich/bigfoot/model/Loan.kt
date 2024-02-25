package com.derich.bigfoot.model

data class Loan(
    var username: String = "",
    var amountLoaned: Int = 0,
    var dateLoaned: String = "",
    var status: Boolean = false,
    var transactionCharges: Int = 0,
    var type: LoanType = LoanType.PERSONAL_LOAN,
    var dateRepaid: String? = null,
    var amountRepaid: Int? = null
)
enum class LoanType {
    PERSONAL_LOAN,
    EXPENSE,
    RECHARGE
}