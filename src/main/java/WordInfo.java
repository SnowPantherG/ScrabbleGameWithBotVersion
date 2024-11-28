import java.util.List;

public class WordInfo {
    public String word;
    public List<Position> positions;

    public WordInfo(String word, List<Position> positions) {
        this.word = word;
        this.positions = positions;
    }
}
