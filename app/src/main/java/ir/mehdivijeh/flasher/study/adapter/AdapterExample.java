package ir.mehdivijeh.flasher.study.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import ir.mehdivijeh.flasher.R;
import ir.mehdivijeh.flasher.general.TextUtil;

public class AdapterExample extends AbstractItem<AdapterExample, AdapterExample.ViewHolder> {

    private String rootExample;
    private String translateExample;

    public AdapterExample(String rootExample, String translateExample) {
        this.rootExample = rootExample;
        this.translateExample = translateExample;
    }

    @NonNull
    @Override
    public AdapterExample.ViewHolder getViewHolder(View v) {
        return new AdapterExample.ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.crd_row_example;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.row_example;
    }

    protected static class ViewHolder extends FastAdapter.ViewHolder<AdapterExample> {

        private TextView txtTranslateWord;
        private TextView txtRootWord;

        ViewHolder(View view) {
            super(view);
            txtTranslateWord = view.findViewById(R.id.txt_translate_word);
            txtRootWord = view.findViewById(R.id.txt_root_word);
            TextUtil.setFonts(view);
        }

        @Override
        public void bindView(AdapterExample item, List<Object> payloads) {
            txtTranslateWord.setText(item.translateExample);
            txtRootWord.setText(item.rootExample);
        }

        @Override
        public void unbindView(AdapterExample item) {

        }
    }
}
