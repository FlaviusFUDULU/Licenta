package fragments;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
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
import transformations.FakePageFragment;

public class AccountFragment extends Fragment
        implements AppBarLayout.OnOffsetChangedListener {

    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;

    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private ImageView mProfileImage;
    private ImageView mProfileImageSmall;
    private int mMaxScrollSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_details, container, false);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        TextView mName = (TextView) view.findViewById(R.id.cardView_text_nume);
        mName.setText(firebaseUser.getDisplayName());

        TextView mEmailCard = (TextView) view.findViewById(R.id.cardView_text_email);
        mEmailCard.setText(firebaseUser.getEmail());

        AppBarLayout appbarLayout = (AppBarLayout) view.findViewById(R.id.materialup_appbar);

        appbarLayout.addOnOffsetChangedListener(this);
        mMaxScrollSize = appbarLayout.getTotalScrollRange();

        TextView mFullName = (TextView) view.findViewById(R.id.materialup_full_name);
        mFullName.setText(firebaseUser.getDisplayName());

        TextView mEmail = (TextView) view.findViewById(R.id.materialup_email);
        mEmail.setText(firebaseUser.getEmail());

        mProfileImage = (ImageView) view.findViewById(R.id.materialup_profile_image);
        Picasso.with(getActivity()).load(firebaseUser.getPhotoUrl())
                .transform(new CircleTransform())
//                .centerInside().resize(mProfileImage.getMaxWidth(), mProfileImage.getMaxHeight())
                .into(mProfileImage);


        return view;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        mProfileImageSmall = (ImageView) getView()
                                                .findViewById(R.id.materialup_profile_image_small);
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;


        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;

            mProfileImage.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();
            mProfileImageSmall.setVisibility(getView().VISIBLE);
            Picasso.with(getActivity()).load(firebaseUser.getPhotoUrl())
                    .transform(new CircleTransform())
//                .centerInside().resize(mProfileImage.getMaxWidth(), mProfileImage.getMaxHeight())
                    .into(mProfileImageSmall);

        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            mProfileImage.animate()
                    .scaleY(1).scaleX(1)
                    .start();
            mProfileImageSmall.setVisibility(getView().INVISIBLE);
        }
    }
    private static class TabsAdapter extends FragmentPagerAdapter {
        private static final int TAB_COUNT = 2;

        TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public Fragment getItem(int i) {
            return FakePageFragment.newInstance();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Tab " + String.valueOf(position);
        }
    }

}
