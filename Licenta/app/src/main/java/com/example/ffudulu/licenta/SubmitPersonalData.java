package com.example.ffudulu.licenta;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SubmitPersonalData extends Activity {

    private String[] personalType = {"Doctor" , "Asistent"};
    private ArrayAdapter<String> adapterPersonalType;
    private Spinner mPersonalTypeSpinner;
    private FirebaseUser firebaseUser;
    private EditText mFirstName;
    private EditText mLastName;
    private Button mBtnSave;
    private String firstName;
    private String lastName;
    private FirebaseAuth mAuth;
    private ImageButton mPhotoUploadBtn;
    private ImageButton mTakePhotoBtn;
    private ImageView mImgProfilePic;
    private ProgressBar mProgressBarUpload;

    private static final int CAMERA_REQUEST_CODE = 1;

    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_personal_data);

        mTakePhotoBtn = (ImageButton) findViewById(R.id.imageButtonPhoto);
        mPhotoUploadBtn = (ImageButton) findViewById(R.id.imageButtonUpload);
        mImgProfilePic = (ImageView) findViewById(R.id.accountImageView);

        mProgressBarUpload = (ProgressBar) findViewById(R.id.progressBarUpload);

        mStorage = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        mFirstName = (EditText) findViewById(R.id.txtFirstName);
        mLastName = (EditText) findViewById(R.id.txtLastName);

        //Spinner
        mPersonalTypeSpinner = (Spinner) findViewById(R.id.spinnerFunction);
        adapterPersonalType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, personalType);
        adapterPersonalType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, personalType);
        adapterPersonalType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPersonalTypeSpinner.setAdapter(adapterPersonalType);
        //END

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String fullname = null;
        if((fullname = firebaseUser.getDisplayName()) != null){
            mFirstName.setText(fullname.split(" ")[0].substring(0,1).toUpperCase() +
                    fullname.split(" ")[0].substring(1).toLowerCase());
            mLastName.setText(fullname.split(" ")[1]);
        }

        //The rest
        mBtnSave = (Button) findViewById(R.id.btnSaveChanges);

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = mFirstName.getText().toString();
                lastName = mLastName.getText().toString();
                if (!TextUtils.isEmpty(firstName) ||
                        !TextUtils.isEmpty(lastName)) {

                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                            .setDisplayName(mFirstName.getText().toString() + " "
                                    + mLastName.getText().toString())
                            .build();
                    firebaseUser.updateProfile(profileUpdate)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SubmitPersonalData.this,
                                                "Registration succesfull!" ,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    Intent mDrawer = new Intent(SubmitPersonalData.this, MainDrawer.class);
                                    startActivity(mDrawer);
                                }
                            });;
                }
                else{
                    Toast.makeText(SubmitPersonalData.this, "All fields are requiered",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        mTakePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mCamera= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (mCamera.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(mCamera, CAMERA_REQUEST_CODE);
                }
                //mProgressBarUpload.setVisibility(View.VISIBLE);
            }
        });
        //end

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            Uri uri = data.getData();

            StorageReference filepath = mStorage.child("TEST").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(SubmitPersonalData.this,
                            "Upload finished!" ,
                            Toast.LENGTH_SHORT).show();
                    //mProgressBarUpload.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
    }
}
