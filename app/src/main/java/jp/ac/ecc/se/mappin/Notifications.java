package jp.ac.ecc.se.mappin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Notifications extends ViewModel {

    private final MutableLiveData<String> mText;

    public Notifications() {
        mText = new MutableLiveData<>();
        mText.setValue("通知画面");
    }

    public LiveData<String> getText() {
        return mText;
    }
}