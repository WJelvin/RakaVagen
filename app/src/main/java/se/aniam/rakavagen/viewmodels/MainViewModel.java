package se.aniam.rakavagen.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;


public class MainViewModel extends AndroidViewModel {

    private String text =  "Hello world!!";

    public MainViewModel(@NonNull Application application) {
        super(application);
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
