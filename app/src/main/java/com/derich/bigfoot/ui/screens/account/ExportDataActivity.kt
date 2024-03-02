package com.derich.bigfoot.ui.screens.account

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.derich.bigfoot.allLoans
import com.derich.bigfoot.allMemberInformation
import com.derich.bigfoot.allTransactions
import com.derich.bigfoot.ui.common.composables.BigFutAppBar
import com.derich.bigfoot.ui.common.composables.showMessage
import com.derich.bigfoot.ui.theme.BigFootTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

private const val FILE_PROVIDER_AUTHORITY = "com.derich.bigfoot.fileprovider"
const val PICK_PDF_FILE = 2
class ExportDataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BigFootTheme {
                    Scaffold(
                        topBar = {
                            BigFutAppBar()
                        }
                    ) {
                            innerPadding ->
                        MyScreenContent({ createFile() },
                            modifier = Modifier.padding(innerPadding))
                    }
            }
        }
    }

    private val createFileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    exportingStatus =true
                    lifecycleScope.launch(Dispatchers.IO) {
                        val contentResolver = applicationContext.contentResolver
                        val takeFlags: Int = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                                Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        contentResolver.takePersistableUriPermission(uri, takeFlags)

                        try {
                            // Create a temporary file to write the workbook to
                            val tempFile = File.createTempFile("temp", ".xlsx")
                            val tempUri = Uri.fromFile(tempFile)

                            val workbook = XSSFWorkbook()

                            // Modify or add data to the workbook
//                            exportToExcel(allMemberInformation.value, allTransactions.value, allLoans.value, workbook)
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
                            for ((index, individualMemberDets) in allMemberInformation.value.withIndex()) {
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
                            for ((index, transactionDetail) in allTransactions.value.withIndex()) {
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
                            for ((index, loanDetail) in allLoans.value.withIndex()) {
                                val row = loanDetailsSheet.createRow(index + 1)
                                row.createCell(0).setCellValue(loanDetail.username)
                                row.createCell(1).setCellValue(loanDetail.amountLoaned.toString())
                                row.createCell(2).setCellValue(loanDetail.dateLoaned)
                                row.createCell(3).setCellValue(loanDetail.status)
                                row.createCell(4).setCellValue(loanDetail.transactionCharges.toString())
                                row.createCell(5).setCellValue(loanDetail.dateRepaid)
                                row.createCell(6).setCellValue(loanDetail.amountRepaid.toString())
                            }
                                FileOutputStream(tempFile).use { fileOutputStream ->
                                    workbook.write(fileOutputStream)
                                }

                                // Now copy the temporary file to the selected URI
                                contentResolver.openOutputStream(uri)?.use { outputStream ->
                                    contentResolver.openInputStream(tempUri)?.use { inputStream ->
                                        inputStream.copyTo(outputStream)
                                    }
                                }

                                withContext(Dispatchers.Main) {
                                    this@ExportDataActivity.showMessage("File saved successfully")
                                    exportingStatus=false
                                }
                            } catch (e: IOException) {
                                withContext(Dispatchers.Main) {
                                    e.printStackTrace()
                                    this@ExportDataActivity.showMessage("Error processing file")
                                    exportingStatus=false
                                }
                            }
                        }
                    }
                }
            }

            private fun createFile() {
                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                    putExtra(Intent.EXTRA_TITLE, "BigfutData.xlsx")
                }
                createFileLauncher.launch(intent)
            }
        }

var exportingStatus = false
    @Composable
    fun MyScreenContent(exportListLauncher: () -> Unit,modifier: Modifier) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(16.dp)
        ) {
            if (exportingStatus){
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Fetching data...")
                    LinearProgressIndicator(modifier = Modifier.wrapContentSize(
                        Alignment.Center))
                }
            }
            else{
                Button(onClick = exportListLauncher) {
                    Text("Export to Excel")
                }
            }
        }
    }