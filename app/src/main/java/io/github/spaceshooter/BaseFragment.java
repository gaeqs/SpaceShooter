package io.github.spaceshooter;

import androidx.fragment.app.Fragment;


public class BaseFragment extends Fragment {
    public boolean onBackPressed() {
        return false;
    }

    protected ScaffoldActivity getScaffoldActivity() {
        return (ScaffoldActivity) getActivity();
    }
}
