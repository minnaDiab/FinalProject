package com.example.testversion;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    TextInputLayout etName, etEmail, etAddress, etPhone;
    ImageView ivUser;
    Button btnSave, btnOrders, btnBills;

    //DBHelper db;
    FirebaseFirestore db;
    private Customer customer;
    public  static Customer currentCust;

    ActivityResultLauncher<Intent> cameraActivityResultLauncher;

    static final String TAG = "DEBUG";
    private CollectionReference mColctRef;
    private DocumentReference mDocRef;
    Uri imageUri = null;  // the *Local* URI of the image file in the Android device
    private StorageReference fileReference;
    private StorageTask mUploadTask; // used to refrain from multiple concurrent uploads
    private StorageReference mStorageRef;
    private ProgressBar progressBarUpload;
    private String downloadUrl = ""; // the URI in the image file in the storage (to be saved to the DB)
    private String id;
    private DocumentSnapshot currentDoc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        btnSave.setOnClickListener(this);
        ivUser.setOnClickListener(this);
        Intent intent=getIntent();
        id=intent.getStringExtra("Phone");

        btnOrders=findViewById(R.id.btnOrders);
        btnOrders.setOnClickListener(this);
        btnBills=findViewById(R.id.btnBills);
        btnBills.setOnClickListener(this);


        cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Bundle xtras = data.getExtras();
                            Bitmap picBitmap = (Bitmap) xtras.get("data");
                            // אם קיימת תמונת רקע כברירת מחדל, יתכן שנרצה להסירה
                            ivUser.setBackgroundResource(0);
                            ivUser.setImageBitmap(picBitmap);
                            uploadImage(picBitmap);
                        }
                    }
                });


        if(!id.isEmpty()) {
            initDB();
        }
        else
            newCustomer();

        String stPhone= String.valueOf(etPhone);
        Intent intent1=new Intent();
        intent1.putExtra("phone_num",stPhone);
    }

    private void showMyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Orders options");

        builder.setPositiveButton("The orders", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(RegisterActivity.this, OrdersList.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Add new order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(RegisterActivity.this, ItemListActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    private void initViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddr);
        etPhone = findViewById(R.id.etPhone);
        ivUser = findViewById(R.id.ivUser);
        btnSave = findViewById(R.id.btnSaveUser);
        progressBarUpload = findViewById(R.id.progressBarUpload);
        //init Firebases
        db = FirebaseFirestore.getInstance();
        mColctRef = db.collection(Firebase.UsersCollection);
        mStorageRef = FirebaseStorage.getInstance().getReference(Firebase.ImageFolder);
    }
    private void initDB() {

        //Select all rows from the users collection
        //If rows exist display info for the first user
        db.collection(Firebase.UsersCollection).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // If result empty (no documents) then create a new document
                            if (task.getResult().size() > 0) {
                                mColctRef.document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("Firebase-GetUser", "User " + id + " was retrieved successfully");
                                            Toast.makeText(RegisterActivity.this, "User " + id + " was retrieved successfully", Toast.LENGTH_LONG).show();
                                            currentDoc = task.getResult();
                                            Map<String, Object> data = currentDoc.getData();
                                            if(data!=null) {
                                                //initForm(currentDoc);
                                                mDocRef = currentDoc.getReference();
                                                populateUserInView(currentDoc);
                                            }
                                            else{
                                                Toast.makeText(RegisterActivity.this, "Client was not found..", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        } else {
                                            Log.e("Camera-GetItem", "Oy vey", task.getException());
                                            Toast.makeText(RegisterActivity.this, "Oy vey:\n" + task.getException(), Toast.LENGTH_LONG).show();
                                            task.getException().printStackTrace();
                                        }
                                    }
                                });
                            }

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }
    private void newCustomer() {
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ivUser) {
            Intent intent=new Intent().setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            //intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                // Activate the camera to get a picture
                cameraActivityResultLauncher.launch(intent);
            }
            return;
        }
        if(view.getId()==R.id.btnBills){
            Intent intent=new Intent(RegisterActivity.this, BillList.class);
            startActivity(intent);
            return;
        }
        if(view.getId()==R.id.btnOrders) {
            showMyDialog();
            return;
        }

        // Clicked Save Button. Check valid input
        if (!checkValidInput())
            return;

        saveToDB();
    }

    private void saveToDB() {
        mColctRef = db.collection(Firebase.UsersCollection);
        if (mUploadTask != null && mUploadTask.isInProgress())
        {
            Toast.makeText(this, "File upload is still in progress, please wait", Toast.LENGTH_SHORT).show();
            return;
        }

        customer = new Customer( etName.getEditText().getText().toString(),
                etEmail.getEditText().getText().toString(),
                etAddress.getEditText().getText().toString(),
                etPhone.getEditText().getText().toString(),
                downloadUrl);

        if (id.length() == 0) {
            id=etPhone.getEditText().getText().toString();
            Firebase.add2Firebase(customer, mColctRef, id, this);
        }
        else
            Firebase.updateInFirebase(customer, mDocRef,id, this);
    }

    private boolean checkValidInput() {
        if (etName.getEditText().getText().toString().trim().equals("")) {
            showAlertDialog("אופס!","יש למלא השם");
            return false;
        }

        if (etEmail.getEditText().getText().toString().trim().equals("")) {
            showAlertDialog("אופס!","יש למלא כתובת מייל");
            return false;
        }

        if (etAddress.getEditText().getText().toString().trim().equals("")) {
            showAlertDialog("אופס!","יש למלא כתובת");
            return false;
        }

        if (etPhone.getEditText().getText().toString().trim().equals("")) {
            showAlertDialog("אופס!","יש למלא מספר טלפון");
            return false;
        }

        return true;
    }

    private void showAlertDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, androidx.appcompat.R.style.AlertDialog_AppCompat);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setIcon(R.drawable.question_mark);
        builder.setCancelable(false);
        builder.setPositiveButton("חזרה", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void uploadImage(Bitmap picBitmap) {
        // First we need to save the image to external storage. Then we upload to Firestore DB
        imageUri = Utils.writeImage(this, picBitmap, "image"); // in order to upload the file we need to save it to the phone storage
        if (imageUri != null)
            downloadUrl=uploadFile();
    }

    public String uploadFile()
    {
        mStorageRef = FirebaseStorage.getInstance().getReference(Firebase.ImageFolder);
        String storageFileName =
                System.currentTimeMillis() + ".jpg"; // Time is used to ensure unique file name
        fileReference = mStorageRef.child(storageFileName);

        mUploadTask = fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                progressBarUpload.setProgress(0);
                            }
                        }, 500); // Delay zeroing of the 100% progress bar for 0.5 sec
                        // Now add a DB entry for the upload
                        Toast.makeText(RegisterActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>()
                {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressBarUpload.setProgress((int) progress);
                    }
                });
        return storageFileName;
    }

    private void populateUserInView(DocumentSnapshot currentDoc) {
        String name,email,address,phone;
        etName.getEditText().setText(currentDoc.get(Firebase.NameKey).toString());
        etEmail.getEditText().setText(currentDoc.get(Firebase.EmailKey).toString());
        etAddress.getEditText().setText(currentDoc.get(Firebase.AddressdKey).toString());
        etPhone.getEditText().setText(currentDoc.get(Firebase.PhoneKey).toString());
        downloadUrl = currentDoc.get(Firebase.ImageUriKey).toString();
        if (downloadUrl != "") {
            ivUser.setBackgroundResource(0);
            Utils.download2ImageView(downloadUrl, ivUser, RegisterActivity.this);
        }
        name=etName.getEditText().toString();
        email=etEmail.getEditText().toString();
        address=etAddress.getEditText().toString();
        phone=currentDoc.get(Firebase.PhoneKey).toString();
        currentCust=new Customer(name,email,address,phone,"");
    }
}