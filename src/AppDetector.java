import java.util.Map;

public class AppDetector implements Runnable {
    private Thread t;
    private SSLDetector ssldetector;
    private DNSDetector dnsdetector;
    private TCPDetector tcpdetector;
    static App apps = new App();

    public AppDetector(SSLDetector s, DNSDetector d, TCPDetector t) {
        ssldetector = s;
        dnsdetector = d;
        tcpdetector = t;
        
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(3000);
                System.out.println("dns");
                for (Map<String,String> temp : dnsdetector.DNSList) {
                    System.out.println(temp.get("domain") + " " + temp.get("address"));
                }
                System.out.println("ssl");
                for (Map<String,String> temp : ssldetector.SSLList) {
                    System.out.println(temp.get("domain") + " " + temp.get("address"));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start () {
        if (t == null) {
            t = new Thread(this);
            t.start ();
        }
    }
    
    public SSLDetector getSslDetector(){
    	return ssldetector;
    }
    
    public DNSDetector getDnsDetector(){
    	return dnsdetector;
    }
    
    public TCPDetector getTcpDetector(){
    	return tcpdetector;
    }
}
