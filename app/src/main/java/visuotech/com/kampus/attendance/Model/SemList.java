package visuotech.com.kampus.attendance.Model;

public class SemList {
    String sem;

    public SemList(String sem) {
        this.sem = sem;
    }
    public SemList() {
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public boolean isSelected;
    public boolean isSelected2;

    public boolean isSelected2() {
        return isSelected2;
    }

    public void setSelected2(boolean selected2) {
        isSelected2 = selected2;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}
