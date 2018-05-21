/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPWord.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 *
 * @author u0151316
 */
public class Yin50 {

    class Yin {

        public String H_ = "";
        public String K_ = "";
        public String R_ = "";

        public Yin(String H, String K, String R) {
            H_ = H;
            K_ = K;
            R_ = R;
        }
    }

    private static Yin50 instance_;
    private List<Yin> yins_ = new LinkedList<>();
    public Map<String, String> spec_ = new TreeMap<>();

    public static Yin50 getInstance() {
        if (instance_ == null) {
            instance_ = new Yin50();
        }
        return instance_;
    }

    public IRoma kanaToRoma(String testkana) {
        String kana = testkana.trim();
        Vector<String> result = new Vector<>();
        Roma roma = new Roma();
        for (int i = 0; i < kana.length(); i++) {
            String obj = "";
            obj += kana.charAt(i);
            if (i + 1 < kana.length()) {
                String spec = findYinByKana(obj + kana.charAt(i + 1));
                if (!spec.equals("")) {
                    result.add(spec);
                    i++;
                    continue;
                }
            }
            if (obj.equals("っ") || obj.equals("ッ")) {
                result.add("?");
            } else if (obj.equals("-") || obj.equals("ー")) {
                result.add("*");
            } else {
                String y = findYinByKana(obj);
                if (y.equals("")) {
                    return roma;
                }
                result.add(y);
            }
        }
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).equals("?")) {
                if (i + 1 < result.size()) {
                    result.set(i, result.get(i + 1).substring(0, 1));
                } else {
                    return roma;
                }
            }
            if (result.get(i).equals("*")) {
                if (i == 0) {
                    return roma;
                } else {
                    String temp = result.get(i - 1);
                    result.set(i, temp.substring(temp.length() - 1, temp.length()));
                }
            }
        }
        roma.SetVector(result);
        return roma;
    }

    private String findYinByKana(String kana) {
        for (Yin y : yins_) {
            if (y.H_.equals(kana) || y.K_.equals(kana)) {
                return y.R_;
            }
        }
        if (spec_.containsKey(kana)) {
            return spec_.get(kana);
        }
        return "";
    }

    private Yin50() {
        yins_.add(new Yin("あ", "ア", "a"));
        yins_.add(new Yin("い", "イ", "i"));
        yins_.add(new Yin("う", "ウ", "u"));
        yins_.add(new Yin("え", "エ", "e"));
        yins_.add(new Yin("お", "オ", "o"));
        yins_.add(new Yin("か", "カ", "ka"));
        yins_.add(new Yin("き", "キ", "ki"));
        yins_.add(new Yin("く", "ク", "ku"));
        yins_.add(new Yin("け", "ケ", "ke"));
        yins_.add(new Yin("こ", "コ", "ko"));
        yins_.add(new Yin("さ", "サ", "sa"));
        yins_.add(new Yin("し", "シ", "shi"));
        yins_.add(new Yin("す", "ス", "su"));
        yins_.add(new Yin("せ", "セ", "se"));
        yins_.add(new Yin("そ", "ソ", "so"));
        yins_.add(new Yin("た", "タ", "ta"));
        yins_.add(new Yin("ち", "チ", "chi"));
        yins_.add(new Yin("つ", "ツ", "tsu"));
        yins_.add(new Yin("て", "テ", "te"));
        yins_.add(new Yin("と", "ト", "to"));
        yins_.add(new Yin("な", "ナ", "na"));
        yins_.add(new Yin("に", "ニ", "ni"));
        yins_.add(new Yin("ぬ", "ヌ", "nu"));
        yins_.add(new Yin("ね", "ネ", "ne"));
        yins_.add(new Yin("の", "ノ", "no"));
        yins_.add(new Yin("は", "ハ", "ha"));
        yins_.add(new Yin("ひ", "ヒ", "hi"));
        yins_.add(new Yin("ふ", "フ", "fu"));
        yins_.add(new Yin("へ", "ヘ", "he"));
        yins_.add(new Yin("ほ", "ホ", "ho"));
        yins_.add(new Yin("ま", "マ", "ma"));
        yins_.add(new Yin("み", "ミ", "mi"));
        yins_.add(new Yin("む", "ム", "mu"));
        yins_.add(new Yin("め", "メ", "me"));
        yins_.add(new Yin("も", "モ", "mo"));
        yins_.add(new Yin("や", "ヤ", "ya"));
        yins_.add(new Yin("ゆ", "ユ", "yu"));
        yins_.add(new Yin("よ", "ヨ", "yo"));
        yins_.add(new Yin("ら", "ラ", "ra"));
        yins_.add(new Yin("り", "リ", "ri"));
        yins_.add(new Yin("る", "ル", "ru"));
        yins_.add(new Yin("れ", "レ", "re"));
        yins_.add(new Yin("ろ", "ロ", "ro"));
        yins_.add(new Yin("わ", "ワ", "wa"));
        yins_.add(new Yin("を", "ヲ", "wo"));
        yins_.add(new Yin("ん", "ン", "nn"));

        yins_.add(new Yin("が", "ガ", "ga"));
        yins_.add(new Yin("ぎ", "ギ", "gi"));
        yins_.add(new Yin("ぐ", "グ", "gu"));
        yins_.add(new Yin("げ", "ゲ", "ge"));
        yins_.add(new Yin("ご", "ゴ", "go"));
        yins_.add(new Yin("ざ", "ザ", "za"));
        yins_.add(new Yin("じ", "ジ", "ji"));
        yins_.add(new Yin("ず", "ズ", "zu"));
        yins_.add(new Yin("ぜ", "ゼ", "ze"));
        yins_.add(new Yin("ぞ", "ゾ", "zo"));
        yins_.add(new Yin("だ", "ダ", "da"));
        yins_.add(new Yin("ぢ", "ヂ", "di"));
        yins_.add(new Yin("づ", "ヅ", "du"));
        yins_.add(new Yin("で", "デ", "de"));
        yins_.add(new Yin("ど", "ド", "do"));
        yins_.add(new Yin("ば", "バ", "ba"));
        yins_.add(new Yin("び", "ビ", "bi"));
        yins_.add(new Yin("ぶ", "ブ", "bu"));
        yins_.add(new Yin("べ", "ベ", "be"));
        yins_.add(new Yin("ぼ", "ボ", "bo"));
        yins_.add(new Yin("ぱ", "パ", "pa"));
        yins_.add(new Yin("ぴ", "ピ", "pi"));
        yins_.add(new Yin("ぷ", "プ", "pu"));
        yins_.add(new Yin("ぺ", "ペ", "pe"));
        yins_.add(new Yin("ぽ", "ポ", "po"));

        yins_.add(new Yin("きゃ", "キャ", "kya"));
        yins_.add(new Yin("きゅ", "キュ", "kyu"));
        yins_.add(new Yin("きょ", "キョ", "kyo"));
        yins_.add(new Yin("しゃ", "シャ", "sha"));
        yins_.add(new Yin("しゅ", "シュ", "shu"));
        yins_.add(new Yin("しょ", "ショ", "sho"));
        yins_.add(new Yin("ちゃ", "チャ", "cha"));
        yins_.add(new Yin("ちゅ", "チュ", "chu"));
        yins_.add(new Yin("ちょ", "チョ", "cho"));
        yins_.add(new Yin("にゃ", "ニャ", "nya"));
        yins_.add(new Yin("にゅ", "ニュ", "nyu"));
        yins_.add(new Yin("にょ", "ニョ", "nyo"));
        yins_.add(new Yin("ひゃ", "ヒャ", "hya"));
        yins_.add(new Yin("ひゅ", "ヒュ", "hyu"));
        yins_.add(new Yin("ひょ", "ヒョ", "hyo"));
        yins_.add(new Yin("みゃ", "ミャ", "mya"));
        yins_.add(new Yin("みゅ", "ミュ", "myu"));
        yins_.add(new Yin("みょ", "ミョ", "myo"));
        yins_.add(new Yin("りゃ", "リャ", "rya"));
        yins_.add(new Yin("りゅ", "リュ", "ryu"));
        yins_.add(new Yin("りょ", "リョ", "ryo"));
        yins_.add(new Yin("ぎゃ", "ギャ", "gya"));
        yins_.add(new Yin("ぎゅ", "ギュ", "gyu"));
        yins_.add(new Yin("ぎょ", "ギョ", "gyo"));
        yins_.add(new Yin("じゃ", "ジャ", "ja"));
        yins_.add(new Yin("じゅ", "ジュ", "ju"));
        yins_.add(new Yin("じょ", "ジョ", "jo"));
        yins_.add(new Yin("びゃ", "ビャ", "bya"));
        yins_.add(new Yin("びゅ", "ビュ", "byu"));
        yins_.add(new Yin("びょ", "ビョ", "byo"));
        yins_.add(new Yin("ぴゃ", "ピャ", "pya"));
        yins_.add(new Yin("ぴゅ", "ピュ", "pyu"));
        yins_.add(new Yin("ぴょ", "ピョ", "pyo"));
        yins_.add(new Yin("ぢゃ", "ヂャ", "dya"));
        yins_.add(new Yin("ぢゅ", "ヂュ", "dyu"));
        yins_.add(new Yin("ぢょ", "ヂョ", "dyo"));

        spec_.put("ティ", "thi");
        spec_.put("フェ", "fe");
        spec_.put("ファ", "fa");
        spec_.put("ディ", "dhi");
        spec_.put("デュ", "dhu");
        spec_.put("ジェ", "jye");
        spec_.put("～", "～");
    }

}
