package com.derich.bigfoot.model.firebase

import android.util.Log
import com.derich.bigfoot.model.Loan
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.Transactions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FirebaseDataSource(
    private val firestore: FirebaseFirestore
) {
    private val sdfDate = SimpleDateFormat("dd-M-yyyy", Locale.US)
    private val sdfTime = SimpleDateFormat("hh:mm:ss", Locale.US)
    private val currentDate = sdfDate.format(Date())
    private val currentTime = sdfTime.format(Date())
    fun getMemberDetails(): Flow<List<MemberDetails>> = callbackFlow{
        var memberDetailsCollection: Query? = null
        try {
            memberDetailsCollection= firestore
                .collectionGroup("allMembers")
                .orderBy("totalAmount", Query.Direction.DESCENDING)
//            delay(5000)
        }
        catch (e: Throwable){
            close(e)
        }
        val memberDetails = memberDetailsCollection?.addSnapshotListener {snapshot,_ ->
            if (snapshot==null) {return@addSnapshotListener}
            try {
                trySend(snapshot.map { snapshots -> snapshots.toObject(MemberDetails::class.java)}).isSuccess
            }
            catch (e: Throwable){
                Log.d("FirestoreQuery", e.message.toString())
                close(e)
            }
        }
        awaitClose{memberDetails?.remove()}
    }
//getting all transactions from firestore db
    fun getAllTransactions(): Flow<List<Transactions>> = callbackFlow{
        var transactionsCollection: Query? = null
        try {
            transactionsCollection= FirebaseFirestore.getInstance()
                .collectionGroup("allTransactions")
                .orderBy("transactionDate", Query.Direction.DESCENDING)
//            delay(5000)
        }
        catch (e: Throwable){
            close(e)
        }
        val transactions = transactionsCollection?.addSnapshotListener {snapshot,_ ->
            if (snapshot==null) {return@addSnapshotListener}
            try {
                trySend(snapshot.map { snapshots -> snapshots.toObject(Transactions::class.java) }).isSuccess
            }
            catch (e: Throwable){
                Log.d("FirestoreQuery", e.message.toString())
                close(e)
            }
        }
        awaitClose{transactions?.remove()}
    }
    fun getAllLoans(): Flow<List<Loan>> = callbackFlow{
        var loansCollection: Query? = null
        try {
            loansCollection= firestore
                .collectionGroup("allLoans")
                .orderBy("dateLoaned", Query.Direction.DESCENDING)
//            delay(5000)
        }
        catch (e: Throwable){
            close(e)
        }
        val loans = loansCollection?.addSnapshotListener {snapshot,_ ->
            if (snapshot==null) {return@addSnapshotListener}
            try {
                trySend(snapshot.map { snapshots -> snapshots.toObject(Loan::class.java) }).isSuccess
            }
            catch (e: Throwable){
                Log.d("Loans error", e.message.toString())
            }
        }
        awaitClose{loans?.remove()}
    }

//    fun uploadToTransactions(transactionDetails: Transactions): StateFlow<Int> {
//        val _status = MutableStateFlow(0)
//
//        firestore.collection("Transactions")
//            .document(currentDate)
//            .collection("allTransactions")
//            .document(currentTime)
//            .set(transactionDetails)
//            .addOnSuccessListener {
//                _status.update { 1 }
//            }
//            .addOnFailureListener { e ->
//                _status.update { 2 }
//            }
//        return _status.asStateFlow()
//    }
fun updateContributions(
    memberPhoneNumber: String,
    memberFullNames: String,
    resultingDate: String,
    newUserAmount: String) =
    firestore.collection("Members")
        .document(memberPhoneNumber)
        .collection("allMembers")
        .document(memberFullNames)
        .update(
            "contributionsDate", resultingDate,
            "totalAmount", newUserAmount
        )
fun uploadTransactions(transactionDetails: Transactions) = firestore.collection("Transactions")
        .document(currentDate)
        .collection("allTransactions")
        .document(currentTime)
        .set(transactionDetails)
//    fun updateContributionsDetails(
//        memberPhoneNumber: String,
//        memberFullNames: String,
//        resultingDate: String,
//        newUserAmount: String): StateFlow<Int> {
//        val _status = MutableStateFlow(0)
//        firestore.collection("Members")
//            .document(memberPhoneNumber)
//            .collection("allMembers")
//            .document(memberFullNames)
//            .update(
//                "contributionsDate", resultingDate,
//                "totalAmount", newUserAmount
//            ).addOnSuccessListener {
//                _status.update { 1 }
//            }
//            .addOnFailureListener { e ->
//                _status.update { 2 }
//            }
//        return _status.asStateFlow()
//    }

}