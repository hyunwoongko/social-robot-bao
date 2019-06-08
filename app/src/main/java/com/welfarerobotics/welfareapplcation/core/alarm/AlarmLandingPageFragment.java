package com.welfarerobotics.welfareapplcation.core.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.welfarerobotics.welfareapplcation.R;
import com.welfarerobotics.welfareapplcation.util.Sound;


public final class AlarmLandingPageFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_alarm_landing_page, container, false);
        final Button dismiss = (Button) v.findViewById(R.id.ok_button);
        dismiss.setOnClickListener(this);
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        Sound.get().pause();
    }

    @Override
    public void onResume() {
        Sound.get().resume(getActivity(), R.raw.alarm);
        Sound.get().loop(true);
        super.onResume();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        Sound.get().stop();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ok_button:
                Sound.get().stop();
                startActivity(new Intent(getActivity(), AlarmActivity.class));
                getActivity().finish();
                break;
        }
    }
}
