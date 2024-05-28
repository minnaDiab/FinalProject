package com.example.testversion;

import static com.example.testversion.FirebaseComm.FIRESTORE;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Firebase {
    private static final String TAG = " Firebase Comm";
    private static FirebaseFirestore FIRESTORE;
    private static FirebaseAuth AUTH;
    private boolean isCreated=false;
    public static final String UsersCollection = "Clients";

    public static final String NameKey = "name";
    public static final String EmailKey = "email";
    public static final String AddressdKey = "address";
    public static final String PhoneKey = "phone_num";
    public static final String ImageUriKey = "imageUri";

    public static final String ImageFolder = "cust_images";
    public ArrayList<Map<String,Object>> arr;

    public Firebase() {
        FIRESTORE = FirebaseFirestore.getInstance();
    }

    private static Map<String, Object> prepareData2Save(Customer customer)
    {
        Map<String, Object> userHashMap = new HashMap<String, Object>();
        userHashMap.put(NameKey, customer.getName());
        userHashMap.put(EmailKey, customer.getEmail());
        userHashMap.put(AddressdKey, customer.getAddress());
        userHashMap.put(PhoneKey, customer.getPhone());
        userHashMap.put(ImageUriKey, customer.getUserPic());

        return userHashMap;
    }



    public interface FireStoreResult
    {
        void elementsReturned(ArrayList<Map<String,Object>> arr);
        void elementsChanged(Map<String,Object> map,int oldIndex,int newIndex);
        void elementRemoved(int index);
        void elementAdded(Map<String,Object> map,int index);
        void changedElement(Map<String,Object> map);


    }

    public static String add2Firebase(Customer customer, CollectionReference mColctRef,String docId, final Context context)
    {
        Map<String, Object> data2save = prepareData2Save(customer);

        DocumentReference document = mColctRef.document(docId);
        //String id = document.getId();
        document.set(data2save).
                addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Log.d("Firebase-Save new", "Docment was saved successfuly");
                            Toast.makeText(context, "Docment was saved successfuly", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Log.w("Firebase-Save new", "Oy vey", task.getException());
                            Toast.makeText(context, "Oy vey:\n" +  task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
        return docId;
    }

    public static void updateInFirebase(Customer customer, DocumentReference mDocRef, String custId,final Context context)
    {
        Map<String, Object> data2save = prepareData2Save(customer);
        mDocRef.set(data2save).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Log.d("Firebase-Save existing", "Docment was saved successfuly");
                    Toast.makeText(context, "Docment was saved successfuly", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Log.w("Firebase-Save existing", "Oy vey", task.getException());
                    Toast.makeText(context, "Oy vey:\n" +  task.getException(), Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    //FireStoreCom
    // Utility functions

    private FirebaseAuth getAuth() {
        if (AUTH == null)
            AUTH = FirebaseAuth.getInstance();
        return AUTH;
    }

    public static FirebaseFirestore getFisrestore() {
        if (FIRESTORE == null)
            FIRESTORE = FirebaseFirestore.getInstance();

        return FIRESTORE;
    }
    public void userSignout(){
        getAuth().signOut();
    }

    // Methods for Authentication
    boolean isUserSignedIn() {

        return getAuth().getCurrentUser() != null;

    }

    public String authUserEmail() {
        return getAuth().getCurrentUser().getEmail();


    }

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
    private FireStoreResult fireStoreResult;
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


    public CollectionReference getCollectionReference(String collection) {
        return getFisrestore().collection(collection);
    }

    public ArrayList<Map<String,Object>> getDocumentWhereEqualWithLimit(String collectionName, String field, String value, int limit)
    {
        getCollectionReference(collectionName).whereEqualTo(field,value).limit(limit).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG, "onComplete:  equal to with limit: size= " + task.getResult().size());
                getDataFromListener(task);
            }
        });

        return arr;
    }
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
}
