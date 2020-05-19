import Tools.ErrorTools;
import Tools.SearchTools;
import Tools.StatTools;


public class Main {
    public static void main(String[] args) {
        try {
            if (!getFileExtention(args[1]).equals("json")){
                throw new IllegalArgumentException("неверно указано расширение входного файла");
            }
            if (!getFileExtention(args[2]).equals("json")){
                throw new IllegalArgumentException("неверно указано расширение выходного файла");
            }
            if(args.length != 3){
                throw new IllegalArgumentException("Приложение запускается с неверным количеством аргументов.");
            }

            if (args[0].equals("search")) {
                new SearchTools().search(args[1], args[2]);
            } else if (args[0].equals("stat")) {
                new StatTools().getStatistic(args[1], args[2]);
            } else throw new IllegalArgumentException("Неверно указан параметр типа операции");
        } catch (Exception e) {
            new ErrorTools().setError(args[2], e);
        }
    }

    private static String getFileExtention (String fileName){
        String [] fileNameArray = fileName.split("\\.");
        return fileNameArray[1];
    }
}
