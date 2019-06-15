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

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    Boolean isChecked=false;
    public boolean isSelected;
    public boolean isselectAll=false;

    public boolean isIsselectAll() {
        return isselectAll;
    }

    public void setIsselectAll(boolean isselectAll) {
        this.isselectAll = isselectAll;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}
