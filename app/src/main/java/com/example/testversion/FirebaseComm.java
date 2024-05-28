package com.example.testversion;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseComm {
    private static final String TAG = " Firebase Comm";
    static FirebaseFirestore FIRESTORE;
    private static FirebaseAuth AUTH;
    private boolean isCreated=false;
    ArrayList<Map<String,Object>> arr;
    // Utility functions


    public FirebaseComm() {
        FirebaseFirestore.getInstance();
    }

    public static FirebaseAuth getAuth() {
        if (AUTH == null)
            AUTH = FirebaseAuth.getInstance();
        return AUTH;
    }

    public static FirebaseFirestore getFisrestore() {
        if (FIRESTORE == null)
            FIRESTORE = FirebaseFirestore.getInstance();

        return FIRESTORE;
    }

    public CollectionReference getCollectionReference(String collection) {
        return getFisrestore().collection(collection);
    }

    public static boolean isUserSignedIn() {

        return getAuth().getCurrentUser() != null;

    }

    public static String authUserEmail() {
        return getAuth().getCurrentUser().getEmail();


    }
    // Methods for Authentication

    public void loginUser(String email, String password) {

        getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "onComplete: login success ");
                                } else {
                                    Log.d(TAG, "onComplete: login failed ");
                                }
                            }
                        });
    }

    public boolean createUser(String mail, String password) {
        getAuth().createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete:  register success");
                            isCreated=true;
                        } else
                            Log.d(TAG, "onComplete: " + task.getException());
                    }
                });
        return isCreated;
    }


    //
    // the following methods perform firestore transactions - GENERIC
    // data can be passed to calling class by Interface
    public interface FireStoreResult
    {
        void elementsReturned(ArrayList<Map<String,Object>> arr);
        void elementsChanged(Map<String,Object> map,int oldIndex,int newIndex);
        void elementRemoved(int index);
        void elementAdded(Map<String,Object> map,int index);
        void changedElement(Map<String,Object> map);


    }
    private FireStoreResult fireStoreResult;

    public void setFireStoreResult(FireStoreResult fireStoreResult)
    {
        this.fireStoreResult = fireStoreResult;
    }


    // Add data to a collection
    public void addToFireStoreCollection(String collectionName, Map<String, Object> map) {
        CollectionReference colRef = getCollectionReference(collectionName);
        colRef.add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: insert to collection");
                    }
                });
    }


    // set data in a specific document or create one.
    public void setFireStoreDocument(String collectionName, Map<String, Object> map) {
        CollectionReference docRef = getCollectionReference(collectionName);//.document(documentName);
        // Note there are options for update & Set with Merge Flag
        // Also for a single element
        // shown here ->  set this item whether new or replace existing
        docRef.add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful())
                            Log.d(TAG, "onComplete:  added to document success");
                        else
                            Log.d(TAG, "onComplete:  added to document failed");
                    }
                });
    }


    // Methods to get Data from firestore
    public void getAllDocumentsInCollection(String collectionName)
    {
        getCollectionReference(collectionName).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        getDataFromListener(task);
                    }
                });

    }
    public void getDocumentWhereEqualWithLimit(String collectionName, String field,String value,int limit)
    {
        getCollectionReference(collectionName).whereEqualTo(field,value).limit(limit).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d(TAG, "onComplete:  equal to with limit: size= " + task.getResult().size());
                        getDataFromListener(task);
                    }
                });
    }


    public void getDocumentsOrderedByFieldWithLimit(String collectionName, String document,String field,int limit)
    {
        String ref=collectionName;
        ref+=".child("+document+")";
        getCollectionReference(ref).orderBy(field).limit(limit).get()
        //getCollectionReference(collectionName).orderBy(field).limit(limit).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d(TAG, "onComplete:  order with limit: size= " + task.getResult().size());
                        getDataFromListener(task);
                    }
                });
    }

    //get data from firestore by document name.
    public ArrayList<Map<String,Object>>  getDocuments(String collectionName, String document)
    {
        String ref=collectionName;
        ref+=".child("+document+")";
        getCollectionReference(ref).get()
                //getCollectionReference(collectionName).orderBy(field).limit(limit).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        Log.d(TAG, "onComplete:  order with limit: size= " + task.getResult().size());
                        getDataFromListener(task);
                    }
                });
        return arr;
    }
    // private method to enter data received into ArrayList
    // of Key/Value objects - MAP
    // can be used from all Get Methods used
    private void  getDataFromListener(Task<QuerySnapshot> task) {

        arr = new ArrayList<>();
        if(task.isSuccessful())
        {
            for (QueryDocumentSnapshot doc:task.getResult())
            {
                arr.add(doc.getData());
            }
            Log.d(TAG, "getDataFromListener: succes, received"+  arr.size() + " " +arr.toString());
            if(fireStoreResult!=null)
                fireStoreResult.elementsReturned(arr);
        }
        else
            Log.d(TAG, "getDataFromListener:  FAILED");

    }

    // below methods are listeners for changes->
    // Examples : listener on a collection and on a specific document
    // data shall be provided back to the class/activity via interface
    //

    public void listenToCollectionChanges(Activity ac, String collectionName) {
        getCollectionReference(collectionName).addSnapshotListener(ac,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.d(TAG, "onEvent: Listen Failed");
                    return;
                }
                // Note here
                // change.getDocument().getData() -> returns the Key/Value Object
                // Map -> can return this to the activity
                for (DocumentChange change : value.getDocumentChanges()) {
                    if (change.getType() == DocumentChange.Type.MODIFIED) {
                        if(fireStoreResult!=null)
                            fireStoreResult.elementsChanged(change.getDocument().getData(),change.getOldIndex(), change.getNewIndex());
                        Log.d(TAG, "onEvent: change modified");
                    } else if (change.getType() == DocumentChange.Type.REMOVED) {
                        if(fireStoreResult!=null)
                            fireStoreResult.elementRemoved(change.getOldIndex());
                        Log.d(TAG, "onEvent:  removed ");
                    } else if (change.getType() == DocumentChange.Type.ADDED) {
                        Log.d(TAG, "onEvent listener: ADDED ");
                        if(fireStoreResult!=null)
                            fireStoreResult.elementAdded(change.getDocument().getData(),change.getNewIndex());
                    }
                }

            }
        });

    }

    //
    // listen to changes -
    // A few overloads available for .addSnapshotListener
    // since we would like to stop listening when activity stopped
    // we need to perform unregister.
    // if we used the one implemented here -> when it receives Activity
    // it becomes activityscoped listener and automatically removed during onStop
    public void listenToDocumentChanges(Activity ac,String collectionName, String documentName) {
        getCollectionReference(collectionName).document(documentName).addSnapshotListener(ac,new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null)
                {
                    Log.d(TAG, "onEvent: ERROR is not null" + error.getMessage());
                    return;
                }
                if(value!=null)
                {
                    // value.getData holds the key value
                    // representd by hashmap

                    Log.d(TAG, "onEvent:received map " + value.getData().toString());
                    if(fireStoreResult!=null)
                        fireStoreResult.changedElement(value.getData());
                }

            }
        });
    }
}
