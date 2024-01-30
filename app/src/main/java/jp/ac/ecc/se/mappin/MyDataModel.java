package jp.ac.ecc.se.mappin;

//画像の部分をコメントしてあるが画像もする
// リサイクラービューに表示するデータの構造を定義するためのデータモデルクラス
public class MyDataModel {
    // コメントを表すフィールド
    private String comment;

    // 画像のリソースIDを表すフィールド
    //private int imageResourceId;

    private int hurt;

    private int smile;

    private int good;

    private String time;

    //画像の部分をコメントしてあるが画像もする
    // コンストラクタ：データを初期化するためのメソッド
    public MyDataModel(String comment, int hurt, int smile, int good, String time) {
        this.comment = comment;
        //this.imageResourceId = imageResourceId;
        this.hurt = hurt;
        this.smile = smile;
        this.good = good;
        this.time = time;
    }

    //画像の部分をコメントしてあるが画像もする

    // コメントを取得するメソッド
    public String getComment() {
        return comment;
    }

    // 画像のリソースIDを取得するメソッド
//    public int getImageResourceId() {
//        return imageResourceId;
//    }

    // ハートを取得するメソッド
    public int getHurt() {
        return hurt;
    }

    // 笑顔を取得するメソッド
    public int getSmile() {
        return smile;
    }

    // いいねを取得するメソッド
    public int getGood() {
        return good;
    }

    // 時間を取得するメソッド
    public String getTime() {
        return time;
    }
}