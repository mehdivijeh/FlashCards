package ir.mehdivijeh.flasher.word.add.adapter;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import ir.mehdivijeh.flasher.R;
import ir.mehdivijeh.flasher.general.TextUtil;
import rx.functions.Action1;

public class AdapterAddExample extends AbstractItem<AdapterAddExample, AdapterAddExample.ViewHolder> {

    private Action1<Void> onClick;

    public AdapterAddExample(Action1<Void> onClick) {
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public AdapterAddExample.ViewHolder getViewHolder(View v) {
        return new AdapterAddExample.ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.crd_add_word;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.row_add_example;
    }

    protected static class ViewHolder extends FastAdapter.ViewHolder<AdapterAddExample> {

        private LinearLayout layoutAddExample;

        ViewHolder(View view) {
            super(view);
            layoutAddExample = view.findViewById(R.id.crd_add_word);
            TextUtil.setFonts(view);
        }

        @Override
        public void bindView(AdapterAddExample item, List<Object> payloads) {
            layoutAddExample.setOnClickListener(v -> item.onClick.call(null));
        }

        @Override
        public void unbindView(AdapterAddExample item) {

        }
    }
}
