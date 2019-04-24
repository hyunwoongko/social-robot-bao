package com.welfarerobotics.welfareapplcation.core.fairytale;

import android.arch.lifecycle.AndroidViewModel;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.core.base.TransparentActivity;
import com.welfarerobotics.welfareapplcation.databinding.FairytaleView;

public class FairytaleActivity extends TransparentActivity<FairytaleView, FairytaleViewModel> {

    @Override public void onCreateView(int layout, Class<? extends AndroidViewModel> clazz) {
        super.onCreateView(R.layout.fairytale_view, FairytaleViewModel.class);
    }
}
