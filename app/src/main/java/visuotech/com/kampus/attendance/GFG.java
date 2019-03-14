package visuotech.com.kampus.attendance;

import java.util.*;

public class GFG {

    // Function to convert ArrayList<String> to String[]
    public static String[] GetStringArray(ArrayList<String> arr)
    {

        // Convert ArrayList to object array
        Object[] objArr = arr.toArray();

        // convert Object array to String array
        String[] str = Arrays
                .copyOf(objArr, objArr
                                .length,
                        String[].class);

        return str;
    }

    // Driver code
//    public static void main(String[] args)
//    {
//
//        // declaration and initialise ArrayList
//        ArrayList<String>
//                a1 = new ArrayList<String>();
//
//        a1.add("Geeks");
//        a1.add("for");
//        a1.add("Geeks");
//
//        // print ArrayList
//        System.out.println("ArrayList: " + a1);
//
//        // Get String Array
//        String[] str = GetStringArray(a1);
//
//        // Print Array elements
//        System.out.print("String Array[]: "
//                + Arrays.toString(str));
//    }
}