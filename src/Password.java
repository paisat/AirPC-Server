import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.htree.HTree;

public class Password {

	private RecordManager recMan;
	private HTree hashTable;
	private String passwordKey="AirMousePcPassword";
	
	public Password()
	{

		try {
			Properties props = new Properties();
			recMan = RecordManagerFactory.createRecordManager("AirPcMouse",
					props);
			long recId=recMan.getNamedObject("passwordStore");
			
			if(recId!=0)
			{
				hashTable=HTree.load(recMan, recId);
			}
			else
			{
				hashTable=HTree.createInstance(recMan);
				recMan.setNamedObject("passwordStore", hashTable.getRecid());
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			
			System.out.println(e.getMessage());
			
			
		}
	}

	public void savePassword(String passwd)

	{
		try
		{
			hashTable.put("length",passwd.length());
			hashTable.put(passwordKey,getMD5(passwd));
			
			recMan.commit();
		}
		
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}

	}
	
	private String getMD5(String passwd)
	{
		
		try
		{
			MessageDigest md=MessageDigest.getInstance("MD5");
			
			md.reset();
			md.update(passwd.getBytes());
			byte[] digest = md.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			passwd = bigInt.toString(16);
			
			
		}
		catch(NoSuchAlgorithmException e)
		{
			
		}
		
		return passwd;
		
	}
	
	public Integer getLength()
	{
		Integer length=new Integer(-1);
		
		try
		{
		
			length=(Integer)hashTable.get("length");
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		return length;
	}
	
	
	public String getPassword()
	{
		
		
		String passwd=new String();
		passwd=null;
		try
		{
			
			passwd=(String) hashTable.get(passwordKey);
			
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
		
		return passwd;
		
	}
	
	public void destroy()
	{
		try
		{
			recMan.close();
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}

}
