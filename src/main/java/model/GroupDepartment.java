package model;

import java.util.ArrayList;

import static service.Utilities.LoadDataFromFile;
import static service.Utilities.LoadListFiles;

public class GroupDepartment {
    private static final ArrayList<Department> groupDepartment = LoadDataFromFile(LoadListFiles().get(0));

    public static ArrayList<Department> GetGroupDepartment() {
        return groupDepartment;
    }
}
