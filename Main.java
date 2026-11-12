import MainCode.SuperUser;
import WindowsCode.Index;

public class Main {
    public static void main(String[] args){
        SuperUser admin = new SuperUser("admin", "admin");
        admin.create_SuperUser_profile();

        Index controlPanel = new Index();
        controlPanel.createWindows();
    }
}
