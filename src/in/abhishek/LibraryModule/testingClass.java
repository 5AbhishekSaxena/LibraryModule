package in.abhishek.LibraryModule;

import in.abhishek.LibraryModule.Utils.UtilFunctions;

/**
 * Created by Abhishek Saxena on 01-09-2018.
 */

public class testingClass {


    private static int x;
    private static int y;

    public static void main(String[] args) {
        StringBuilder param = new StringBuilder();
        for (int i = 0; i < 7; i++){
            param.append("?");
            if(i < 6)
                param.append(", ");
        }
        UtilFunctions.println(param.toString());
    }
}
