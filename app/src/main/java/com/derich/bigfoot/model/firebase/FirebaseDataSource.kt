package com.derich.bigfoot.model.firebase

import com.derich.bigfoot.model.Loan
import com.derich.bigfoot.model.MemberDetails
import com.derich.bigfoot.model.Transactions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseDataSource(
    private val firestore: FirebaseFirestore
) {

    fun getMemberDetails(): Flow<List<MemberDetails>> = callbackFlow{
        var memberDetailsCollection: Query? = null
        try {
            memberDetailsCollection= firestore
                .collectionGroup("allMembers")
                .orderBy("totalAmount", Query.Direction.DESCENDING)
        }
        catch (e: Throwable){
            close(e)
        }
        val memberDetails = memberDetailsCollection?.addSnapshotListener {snapshot,_ ->
            if (snapshot==null) {return@addSnapshotListener}
            try {
                trySend(snapshot.map { snapshots -> snapshots.toObject(MemberDetails::class.java) }).isSuccess
            }
            catch (e: Throwable){

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

            }
        }
        awaitClose{loans?.remove()}
    }

}