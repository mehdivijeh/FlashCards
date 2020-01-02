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
import rx.functions.Action1;

public class AdapterAddCollection extends AbstractItem<AdapterAddCollection, AdapterAddCollection.ViewHolder> {

    private int backgroundColor;
    private Action1<AdapterAddCollection> onClick;

    public AdapterAddCollection( int backgroundColor, Action1<AdapterAddCollection> onClick) {
        this.backgroundColor = backgroundColor;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public AdapterAddCollection.ViewHolder getViewHolder(View v) {
        return new AdapterAddCollection.ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.crd_row_add_collection;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.row_collection_add;
    }

    protected static class ViewHolder extends FastAdapter.ViewHolder<AdapterAddCollection> {

        private CardView row_collection;

        ViewHolder(View view) {
            super(view);

            row_collection = view.findViewById(R.id.crd_row_add_collection);
        }

        @Override
        public void bindView(final AdapterAddCollection item, List<Object> payloads) {

            row_collection.setCardBackgroundColor(item.backgroundColor);
            row_collection.setOnClickListener(v -> item.onClick.call(item));

        }

        @Override
        public void unbindView(AdapterAddCollection item) {
        }
    }
}