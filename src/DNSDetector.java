import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;

public class DNSDetector {
    public HashSet<Map<String,String>> DNSList = new HashSet<>();

    public void addToDNSTable(StreamModel inputJSON) {
        DNSList.addAll(Arrays.asList(inputJSON.dns));
        //System.out.println(DNSList);
    }
}
