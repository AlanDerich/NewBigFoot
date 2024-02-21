package com.derich.bigfoot.ui.screens.account

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.derich.bigfoot.allLoans
import com.derich.bigfoot.allMemberInformation
import com.derich.bigfoot.allTransactions
import com.derich.bigfoot.model.Loan
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.Transactions
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException

private const val FILE_PROVIDER_AUTHORITY = "com.derich.bigfoot.fileprovider"
class ExportDataActivity(): ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MyScreenContent(createFile())
                Toast.makeText(LocalContext.current, "${ allMemberInformation.value.size }", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val createFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    try {
                        val inputStream = contentResolver.openInputStream(uri)

                        // Check if inputStream is null
                        if (inputStream != null) {
                            val workbook = XSSFWorkbook(inputStream)

                            // Modify or add data to the workbook
                            exportToExcel(allMemberInformation.value, allTransactions.value, allLoans.value, workbook)

                            // Save the modified workbook back to the file
                            saveWorkbookToFile(uri, workbook)
                        } else {
                            Toast.makeText(this, "Error opening file", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Error processing file", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    private fun createFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/vnd.ms-excel"
            putExtra(Intent.EXTRA_TITLE, "BigfutData")
        }
        createFileLauncher.launch(intent)
    }

    private fun saveWorkbookToFile(uri: Uri, workbook: XSSFWorkbook) {
        try {
            val outputStream = contentResolver.openOutputStream(uri)
            workbook.write(outputStream)
            outputStream?.close()
            workbook.close()
            Toast.makeText(this, "File saved successfully", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        Surface {
            content()
        }
    }
}

@Composable
fun MyScreenContent(exportListLauncher: Unit) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Button(onClick = {
            exportListLauncher }
        ) {
            Text("Export to Excel")
        }
    }
}


fun exportToExcel(memberDetailsList: List<MemberDetails>,
                  transactionsList: List<Transactions>,
                  loansList: List<Loan>,
                  workbook:XSSFWorkbook) {

    // Create sheet for members
    val membersSheet = workbook.createSheet("Member Details")

    // Create header row for members
    val headerSheetRow = membersSheet.createRow(0)
    headerSheetRow.createCell(0).setCellValue("Phone Number")
    headerSheetRow.createCell(1).setCellValue("First Name")
    headerSheetRow.createCell(2).setCellValue("Second Name")
    headerSheetRow.createCell(3).setCellValue("Surname")
    headerSheetRow.createCell(4).setCellValue("Full Names")
    headerSheetRow.createCell(5).setCellValue("Total Amount")
    headerSheetRow.createCell(6).setCellValue("Contributions Date")
    headerSheetRow.createCell(7).setCellValue("Profile Picture URL")

    // Fill data rows for members
    for ((index, individualMemberDets) in memberDetailsList.withIndex()) {
        val row = membersSheet.createRow(index + 1)
        row.createCell(0).setCellValue(individualMemberDets.phoneNumber)
        row.createCell(1).setCellValue(individualMemberDets.firstName)
        row.createCell(2).setCellValue(individualMemberDets.secondName)
        row.createCell(3).setCellValue(individualMemberDets.surname)
        row.createCell(4).setCellValue(individualMemberDets.fullNames)
        row.createCell(5).setCellValue(individualMemberDets.totalAmount)
        row.createCell(6).setCellValue(individualMemberDets.contributionsDate)
        row.createCell(7).setCellValue(individualMemberDets.profPicUrl)
    }

    // Create sheet for transactions
    val transactionsSheet = workbook.createSheet("Transactions Details")

    // Create header row for transactions
    val transactionsHeaderRow = transactionsSheet.createRow(0)
    transactionsHeaderRow.createCell(0).setCellValue("Transaction Date")
    transactionsHeaderRow.createCell(1).setCellValue("Deposit for")
    transactionsHeaderRow.createCell(2).setCellValue("Deposited by")
    transactionsHeaderRow.createCell(3).setCellValue("Amount")
    transactionsHeaderRow.createCell(4).setCellValue("Confirmation Message")
    transactionsHeaderRow.createCell(5).setCellValue("Saved by:")

    // Fill data rows for transactions
    for ((index, transactionDetail) in transactionsList.withIndex()) {
        val row = transactionsSheet.createRow(index + 1)
        row.createCell(0).setCellValue(transactionDetail.transactionDate)
        row.createCell(1).setCellValue(transactionDetail.depositFor)
        row.createCell(2).setCellValue(transactionDetail.depositBy)
        row.createCell(3).setCellValue(transactionDetail.transactionAmount.toString())
        row.createCell(4).setCellValue(transactionDetail.transactionConfirmation)
        row.createCell(5).setCellValue(transactionDetail.savedBy)
    }

    // Create sheet for loans details
    val loanDetailsSheet = workbook.createSheet("Loans Details")

    // Create header row for Company Details
    val loansHeaderRow = loanDetailsSheet.createRow(0)
    loansHeaderRow.createCell(0).setCellValue("Username")
    loansHeaderRow.createCell(1).setCellValue("Amount Loaned")
    loansHeaderRow.createCell(2).setCellValue("Date Loaned")
    loansHeaderRow.createCell(3).setCellValue("Status")
    loansHeaderRow.createCell(4).setCellValue("Transaction fee")
    loansHeaderRow.createCell(5).setCellValue("Date Repaid")
    loansHeaderRow.createCell(6).setCellValue("Amount Repaid")

    // Fill data rows for Company Details
    for ((index, loanDetail) in loansList.withIndex()) {
        val row = loanDetailsSheet.createRow(index + 1)
        row.createCell(0).setCellValue(loanDetail.username)
        row.createCell(1).setCellValue(loanDetail.amountLoaned.toString())
        row.createCell(2).setCellValue(loanDetail.dateLoaned)
        row.createCell(3).setCellValue(loanDetail.status)
        row.createCell(4).setCellValue(loanDetail.transactionCharges.toString())
        row.createCell(5).setCellValue(loanDetail.dateRepaid)
        row.createCell(6).setCellValue(loanDetail.amountRepaid.toString())
    }
}