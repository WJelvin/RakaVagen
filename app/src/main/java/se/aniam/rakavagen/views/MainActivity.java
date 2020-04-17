package se.aniam.rakavagen.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import se.aniam.rakavagen.R;
import se.aniam.rakavagen.viewmodels.MainViewModel;
import se.aniam.rakavagen.viewmodels.MainViewModelFactory;

public class MainActivity extends AppCompatActivity {

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, MainViewModelFactory.getInstance(getApplication())).get(MainViewModel.class);
        setContentView(R.layout.activity_main);
        TextView mainText = findViewById(R.id.mainText);
        mainText.setText(viewModel.getText());
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, ArrowFragment.newInstance())
//                    .commitNow();
//        }
    }
}
