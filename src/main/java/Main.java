import Tools.SearchTools;
import Tools.StatTools;

public class Main {
    public static void main(String[] args) {
        if (args[0].equals("search")) {
            new SearchTools().search(args[1], args[2]);
        } else if (args[0].equals("stat")){
            new StatTools().getStatistic(args[1], args[2]);
        }
    }
}
