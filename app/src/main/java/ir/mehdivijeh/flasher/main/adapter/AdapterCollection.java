package ir.mehdivijeh.flasher.main.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.app.progresviews.ProgressWheel;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import ir.mehdivijeh.flasher.R;
import ir.mehdivijeh.flasher.general.TextUtil;
import rx.functions.Action1;

public class AdapterCollection extends AbstractItem<AdapterCollection, AdapterCollection.ViewHolder> {

    private String name;
    private int count;
    private int progress;
    private int backgroundColor;
    private Action1<AdapterCollection> onClick;
    private Action1<AdapterCollection> onLongClick;

    public AdapterCollection(String name, int count, int progress, int backgroundColor, Action1<AdapterCollection> onClick, Action1<AdapterCollection> onLongClick) {
        this.name = name;
        this.count = count;
        this.progress = progress;
        this.backgroundColor = backgroundColor;
        this.onClick = onClick;
        this.onLongClick = onLongClick;
    }

    @NonNull
    @Override
    public AdapterCollection.ViewHolder getViewHolder(View v) {
        return new AdapterCollection.ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.crd_row_collection;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.row_collection;
    }

    protected static class ViewHolder extends FastAdapter.ViewHolder<AdapterCollection> {

        private TextView txt_collection_name;
        private TextView txt_collection_size;
        private ProgressWheel prg_learn_progress;
        private Button btn_learn;
        private CardView row_collection;

        ViewHolder(View view) {
            super(view);

            txt_collection_name = view.findViewById(R.id.txt_collection_name);
            txt_collection_size = view.findViewById(R.id.txt_collection_size);
            prg_learn_progress = view.findViewById(R.id.prg_learn_progress);
            btn_learn = view.findViewById(R.id.btn_learn);
            row_collection = view.findViewById(R.id.crd_row_collection);
            TextUtil.setFonts(view);

        }

        @Override
        public void bindView(final AdapterCollection item, List<Object> payloads) {

            txt_collection_name.setText(item.name);
            txt_collection_size.setText(String.valueOf(item.count));

            int percent = (int) (item.progress * (360.0 / ((float) item.count)));
            prg_learn_progress.setPercentage(percent);

            String stepCount = String.valueOf(item.progress);
            String def = "Words";
            if (item.count > 0) {
                if (item.progress == item.count) {
                    stepCount = "finish!";
                    def = "✓";
                }
            } else {
                stepCount = "no word";
                def = "add a new word";
            }

            prg_learn_progress.setStepCountText(stepCount);
            prg_learn_progress.setDefText(def);


            btn_learn.setOnClickListener(view -> item.onClick.call(item));

            btn_learn.setOnLongClickListener(view -> {
                item.onLongClick.call(item);
                return true;
            });

            row_collection.setCardBackgroundColor(item.backgroundColor);

        }

        @Override
        public void unbindView(AdapterCollection item) {
        }
    }
}