import Tools.ErrorTools;
import Tools.SearchTools;
import Tools.StatTools;


public class Main {
    public static void main(String[] args) {
        try {
            if (!getFileExtention(args[1]).equals("json")){
                throw new IllegalArgumentException("������� ������� ���������� �������� �����");
            }
            if (!getFileExtention(args[2]).equals("json")){
                throw new IllegalArgumentException("������� ������� ���������� ��������� �����");
            }
            if(args.length != 3){
                throw new IllegalArgumentException("���������� ����������� � �������� ����������� ����������.");
            }

            if (args[0].equals("search")) {
                new SearchTools().search(args[1], args[2]);
            } else if (args[0].equals("stat")) {
                new StatTools().getStatistic(args[1], args[2]);
            } else throw new IllegalArgumentException("������� ������ �������� ���� ��������");
        } catch (Exception e) {
            new ErrorTools().setError(args[2], e);
        }
    }

    private static String getFileExtention (String fileName){
        String [] fileNameArray = fileName.split("\\.");
        return fileNameArray[1];
    }
}
