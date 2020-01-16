package ir.mehdivijeh.flasher.word.adapter;

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

public class AdapterWord extends AbstractItem<AdapterWord, AdapterWord.ViewHolder> {

    private String rootWord;
    private String translateWord;
    private boolean isILearn;
    private int number;
    private Action1<AdapterWord> onClick;


    public AdapterWord(String rootWord, String translateWord, boolean isILearn , int number, Action1<AdapterWord> onClick) {
        this.rootWord = rootWord;
        this.translateWord = translateWord;
        this.isILearn = isILearn;
        this.number = number;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public AdapterWord.ViewHolder getViewHolder(View v) {
        return new AdapterWord.ViewHolder(v);
    }

    @Override
    public int getType() {
        return R.id.crd_row_word;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.row_word;
    }


    protected static class ViewHolder extends FastAdapter.ViewHolder<AdapterWord> {

        private TextView txtTranslateWord;
        private TextView txtRootWord;
        private ImageView imgCheck;
        private CardView crdRowWord;

        ViewHolder(View view) {
            super(view);
            txtTranslateWord = view.findViewById(R.id.txt_translate_word);
            txtRootWord = view.findViewById(R.id.txt_root_word);
            imgCheck = view.findViewById(R.id.img_check);
            crdRowWord = view.findViewById(R.id.crd_row_word);
            TextUtil.setFonts(view);
        }

        @Override
        public void bindView(AdapterWord item, List<Object> payloads) {
            txtTranslateWord.setText(item.translateWord);
            txtRootWord.setText(item.number + "- " +item.rootWord);

            if (item.isILearn) {
                imgCheck.setVisibility(View.VISIBLE);
            } else {
                imgCheck.setVisibility(View.GONE);
            }

            crdRowWord.setOnClickListener(v -> item.onClick.call(item));
        }

        @Override
        public void unbindView(AdapterWord item) {

        }

    }
}
