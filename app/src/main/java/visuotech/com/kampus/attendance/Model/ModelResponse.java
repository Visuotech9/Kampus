package visuotech.com.kampus.attendance.Model;

import java.util.List;

public class ModelResponse {
    private List<Student> mData;

    public ModelResponse() {
    }

    public List<Student> getmData() {
        return mData;
    }

    public void setmData(List<Student> mData) {
        this.mData = mData;
    }
}
