package ir.mehdivijeh.flasher.word;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.os.Bundle;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;

import ir.mehdivijeh.flasher.R;

public class WordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);

        Group group = findViewById(R.id.group_no_word);
        group.setVisibility(View.GONE);

        ShimmerFrameLayout container =
                findViewById(R.id.shimmer_view_container);
        container.startShimmer();
    }
}
