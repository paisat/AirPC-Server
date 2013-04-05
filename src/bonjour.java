import com.apple.dnssd.DNSSD;
import com.apple.dnssd.DNSSDException;
import com.apple.dnssd.DNSSDRegistration;
import com.apple.dnssd.DNSSDService;
import com.apple.dnssd.RegisterListener;
import com.apple.dnssd.TXTRecord;


public class bonjour implements RegisterListener {
	
	DNSSDService record;
	public  void registerService()  {
		try {
			record = DNSSD.register(0,0,null,"_test._tcp.",null,null,1234,null,this);
			
			
		} catch (DNSSDException e) {
			// error handling here
			System.out.println(e.getMessage());
		}
	}
	
	public void unregisterService(){
		System.out.println("Removed");
		record.stop();
               
	}
	public void serviceRegistered(DNSSDRegistration registration, int flags,String serviceName, String regType, String domain){
		System.out.println(serviceName);
	}

	public void operationFailed(DNSSDService registration, int error){
		// do error handling here if you want to
		System.out.println(error);
	}

	
}
