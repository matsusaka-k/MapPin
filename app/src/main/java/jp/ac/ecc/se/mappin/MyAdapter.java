package jp.ac.ecc.se.mappin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<MyDataModel> dataList;

    // コンストラクタでデータリストを受け取る
    public MyAdapter(List<MyDataModel> dataList) {
        this.dataList = dataList;
    }

    // 新しいViewHolderを作成する
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // レイアウトのインフレート（作成）を行い、それを元にViewHolderを作成
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_post, parent, false);
        return new ViewHolder(view);
    }

    //取得したデータのコメントセット
    // ViewHolderにデータをバインドする
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // データリストから対応するデータを取得
        MyDataModel data = dataList.get(position);
//        holder.commentTextView.setText(data.getComment());
//        holder.imageView.setImageResource(data.getImageResourceId());
       // holder.postImage.setImageBitmap(data.getImageResourceId());
        // データをViewHolderの各要素にセット
        //画像の部分をコメントしてあるが画像もする
        holder.comment.setText(data.getComment());
        holder.heartText.setText(data.getHurt());
        holder.smiletext.setText(data.getSmile());
        holder.goodText.setText(data.getGood());
        holder.timeText.setText(data.getTime());
    }

    // データリストのサイズを返す
    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // ViewHolderクラス
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //ImageView postImage;
        TextView comment;
        Button hurtButton;
        Button smileBbutton;
        Button goodBbutton;
        TextView heartText;
        TextView smiletext;
        TextView goodText;
        TextView timeText;

        // コンストラクタでViewHolderの各要素を初期化
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //postImage = itemView.findViewById(R.id.post_image);
            comment = itemView.findViewById(R.id.comment);
            hurtButton = itemView.findViewById(R.id.heart_button);
            smileBbutton = itemView.findViewById(R.id.smile_button);
            goodBbutton = itemView.findViewById(R.id.good_button);
            heartText = itemView.findViewById(R.id.heart_text);
            smiletext = itemView.findViewById(R.id.smile_text);
            goodText = itemView.findViewById(R.id.good_text);
            timeText = itemView.findViewById(R.id.time_text);
        }
    }
}
