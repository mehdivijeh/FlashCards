package ir.mehdivijeh.flasher.word.add.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

import ir.mehdivijeh.flasher.R;
import ir.mehdivijeh.flasher.general.TextUtil;
import rx.functions.Action1;

public class AdapterAddedExample extends AbstractItem<AdapterAddedExample, AdapterAddedExample.ViewHolder> {

    private String rootExample;
    private String translateExample;
    private Action1<Integer> onClick;

    public AdapterAddedExample(String rootExample, String translateExample, Action1<Integer> onClick) {
        this.rootExample = rootExample;
        this.translateExample = translateExample;
        this.onClick = onClick;
    }

    public String getRootExample() {
        return rootExample;
    }

    public String getTranslateExample() {
        return translateExample;
    }

    @NonNull
    @Override
    public AdapterAddedExample.ViewHolder getViewHolder(View v) {
        return new AdapterAddedExample.ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.crd_row_added_example;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.row_added_example;
    }

    protected static class ViewHolder extends FastAdapter.ViewHolder<AdapterAddedExample> {

        private TextView txtTranslateWord;
        private TextView txtRootWord;
        private CardView crdRowExample;
        private ImageView imgDelete;

        ViewHolder(View view) {
            super(view);
            txtTranslateWord = view.findViewById(R.id.txt_translate_word);
            txtRootWord = view.findViewById(R.id.txt_root_word);
            crdRowExample = view.findViewById(R.id.crd_row_example);
            imgDelete = view.findViewById(R.id.img_delete);
            TextUtil.setFonts(view);
        }

        @Override
        public void bindView(AdapterAddedExample item, List<Object> payloads) {
            txtTranslateWord.setText(item.translateExample);
            txtRootWord.setText(item.rootExample);

            imgDelete.setOnClickListener(v -> item.onClick.call(getAdapterPosition()));

        }

        @Override
        public void unbindView(AdapterAddedExample item) {

        }
    }
}
