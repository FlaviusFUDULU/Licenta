package fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ffudulu.licenta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import transformations.CircleTransform;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fail extends Fragment {


    public Fail() {
        // Required empty public constructor
    }

    private ImageView mAccountImage;
    private FirebaseUser firebaseUser;
    private TextView txtViewName;
    private TextView txtViewEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_account, container, false );

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.id.accountImage );

        mAccountImage = (ImageView) view.findViewById(R.id.accountImage);
        txtViewName = (TextView) view.findViewById( R.id.textViewName );
        txtViewEmail = (TextView) view.findViewById( R.id.textViewEmail1 );
        txtViewName.setText( firebaseUser.getDisplayName() );
        txtViewEmail.setText( firebaseUser.getEmail() );


        Picasso.with(getActivity()).load(firebaseUser.getPhotoUrl())
                .transform(new CircleTransform())
                .centerInside().resize(mAccountImage.getMaxWidth(), mAccountImage.getMaxHeight())
                .into(mAccountImage);
        return view;
    }

}
