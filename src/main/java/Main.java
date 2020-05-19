import Tools.ErrorTools;
import Tools.SearchTools;
import Tools.StatTools;


public class Main {
    public static void main(String[] args) {
        try {
            if (args[0].equals("search")) {
                new SearchTools().search(args[1], args[2]);
            } else if (args[0].equals("stat")) {
                new StatTools().getStatistic(args[1], args[2]);
            } else throw new IllegalArgumentException("Ќеверно указан параметр типа операции");
        } catch (Exception e) {
            new ErrorTools().setError(args[2], e);
        }
    }
}
