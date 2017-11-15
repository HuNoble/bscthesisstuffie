
public class Main {

    static SSLDetector ssldetector = new SSLDetector();
    static DNSDetector dnsdetector = new DNSDetector();
    static TCPDetector tcpdetector = new TCPDetector();
    
    public static void main(String[] args){
        FlowDistributor flowDistributor = new FlowDistributor(ssldetector, dnsdetector, tcpdetector);
        flowDistributor.start();
        AppDetector apdet = new AppDetector(ssldetector, dnsdetector, tcpdetector);
        apdet.start();
    }
}
