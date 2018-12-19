package com.example.ermolaenkoalex.nytimes.common;

import com.example.ermolaenkoalex.nytimes.R;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseOneTwoFragmentActivity extends BaseActivity {
    private static final String BACKSTACK_INIT_NAME = "BACKSTACK_INIT_NAME";

    private boolean isTwoPanel;

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        isTwoPanel = findViewById(R.id.frame_detail) != null;
    }

    public @IdRes
    int getFrameIdForNextFragments() {
        return isTwoPanel ? R.id.frame_detail : R.id.frame_list;
    }

    protected void changePrimaryFragment(@NonNull Fragment fragment, @Nullable String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_list, fragment, tag)
                .addToBackStack(BACKSTACK_INIT_NAME)
                .commit();
    }

    protected void changeSecondaryFragment(@NonNull Fragment fragment, @Nullable String tag) {
        changeSecondaryFragment(fragment, tag, false);
    }

    protected void changeSecondaryFragment(@NonNull Fragment fragment, @Nullable String tag, boolean popBackStack) {
        if (popBackStack) {
            getSupportFragmentManager().popBackStackImmediate(BACKSTACK_INIT_NAME, 0);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        android.R.animator.fade_in,
                        android.R.animator.fade_out)
                .replace(getFrameIdForNextFragments(), fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    protected void resetSecondaryFragment() {
        getSupportFragmentManager().popBackStackImmediate(BACKSTACK_INIT_NAME, 0);
    }
}