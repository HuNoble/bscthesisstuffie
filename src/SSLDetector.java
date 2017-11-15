import java.util.ArrayList;
import java.util.HashSet;

public class SSLDetector {
    public HashSet<ArrayList<String>> SSLList = new HashSet<>();

    public void addToSSLTable(StreamModel inputJSON) {
        ArrayList<String> addToList = new ArrayList<>();
        addToList.add(inputJSON.dest_address);
        addToList.add(inputJSON.ssl.get("server_name"));
        SSLList.add(addToList);
        /*System.out.println("Start of list.");
        for (ArrayList<String> temp : SSLList) {
            System.out.println(temp.get(0) + " " + temp.get(1));
        }
        System.out.println("End of list.");*/
    }
}
