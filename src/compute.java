import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;


public class compute {
	
	private boolean ctrlpressed=false;
	private boolean shiftpressed=false;
	private boolean altpressed=false;
	private boolean capspressed=false;
	private boolean linuxUp=false;
	private DatagramSocket socket=null;
	private boolean canRun=true;
	private Robot robot;
	
	
	public  void setConnection()
	{
		try {
			byte buf[] = new byte[256];
			socket = new DatagramSocket(6600);
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			System.out.println("inside2");
			
			while (canRun) {
				
				
				socket.receive(packet);
				instruction(packet);

			}
			
			

		} catch (SocketException e) {
			
		}

		catch (IOException e) {
			
		}									
	}
	
	private void instruction(DatagramPacket packet)
	{
		
		try
		{
			keymap obj=new keymap();
			
			 robot=new Robot();
			String inst=new String(packet.getData(), 0, packet.getLength(), "UTF-8");
			System.out.println(inst);
			String split[]=inst.split(",");
			if(split[0].equals("mou"))
			{
				
				int x=Integer.valueOf(split[1]).intValue()+MouseInfo.getPointerInfo().getLocation().x;
				int y=Integer.valueOf(split[2]).intValue()+MouseInfo.getPointerInfo().getLocation().y;
				Toolkit toolkit=Toolkit.getDefaultToolkit();
				Dimension dim=toolkit.getScreenSize();
				if(x>dim.width)
					x=dim.width;
				if(y>dim.height)
					y=dim.height;
				
				if(x<0)
					x=0;
				if(y<0)
					y=0;
			
				robot.mouseMove(x,y );
			}
			else if(split[0].equals("moveClk"))
			{
				if(split[1].equals("single"))
				{
					robot.mouseMove(Integer.parseInt(split[2]),Integer.parseInt(split[3]));
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
				}
				else if(split[1].equals("double"))
				{
					robot.mouseMove(Integer.parseInt(split[2]), Integer.parseInt(split[3]));
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
				}
			}
			
			else if(split[0].equals("clk"))
			{
				if(split[1].equals("left"))
				{
					if(split[2].equals("double"))
					{
						robot.mousePress(InputEvent.BUTTON1_MASK);
						robot.mouseRelease(InputEvent.BUTTON1_MASK);
						robot.mousePress(InputEvent.BUTTON1_MASK);
						robot.mouseRelease(InputEvent.BUTTON1_MASK);
						
					}
					else if(split[2].equals("single"))
					{
						if(linuxUp&&altpressed)
						{
							robot.keyRelease(18);
							linuxUp=false;
						}
						
						robot.mousePress(InputEvent.BUTTON1_MASK);
						robot.mouseRelease(InputEvent.BUTTON1_MASK);
					}
					else if(split[2].equals("press"))
					{
						robot.mousePress(InputEvent.BUTTON1_MASK);
					}
					else if(split[2].equals("release"))
					{
						robot.mouseRelease(InputEvent.BUTTON1_MASK);
					}
				}
				else if(split[1].equals("right"))
				{
					if(split[2].equals("single"))
					{
						robot.mousePress(InputEvent.BUTTON3_MASK);
						robot.mouseRelease(InputEvent.BUTTON3_MASK);
						
					}
					else if(split[2].equals("press"))
					{
						robot.mousePress(InputEvent.BUTTON3_MASK);
					}
					else if(split[2].equals("release"))
					{
						robot.mouseRelease(InputEvent.BUTTON3_MASK);
						
					}
				}
				else if(split[1].equals("middle"))
				{
					if(split[2].equals("single"))
					{
						robot.mousePress(InputEvent.BUTTON2_MASK);
						robot.mouseRelease(InputEvent.BUTTON2_MASK);
					}
				}
			}
			else if(split[0].equals("scroll"))
			{
				
				
				robot.mouseWheel(Integer.valueOf(split[1]).intValue());
				
				
			}
			else if(split[0].equals("key"))
			{
				int code=obj.key(Integer.valueOf(split[2]).intValue());
				
				if(split[1].equals("nor"))
				{
					
					robot.keyPress(code);
					robot.keyRelease(code);
					
				}
				else if(split[1].equals("shift"))
				{
					robot.keyPress(16);
					robot.keyPress(code);
					robot.keyRelease(code);
					robot.keyRelease(16);
					
						
				}
			}
			
			else if(split[0].equals("special"))
			{
				if(split[1].equals("nor"))
				{
					int code=obj.key(Integer.valueOf(split[2]).intValue());
					robot.keyPress(code);
					robot.keyRelease(code);
				}
				else
				{
					int code=0;
					if(split[1].equals("shift"))
					{
						code=16;
					}
					else if(split[1].equals("ctrl"))
						code=17;
					else if(split[1].equals("caps"))
						code=20;
					else if(split[1].equals("alt"))
						code=18;
					
					if(split[2].equals("press"))
					{
						if(code==17)
							ctrlpressed=true;
						else if(code==16)
							shiftpressed=true;
						else if(code==20)
							capspressed=true;
						else if(code==18)
							altpressed=true;
							
						
						robot.keyPress(code);
					}
					if(split[2].equals("release"))
					{
						if(code==17)
							ctrlpressed=false;
						else if(code==16)
							shiftpressed=false;
						else if(code==20)
							capspressed=false;
						else if(code==18)
							altpressed=false;
					
						robot.keyRelease(code);
					}
					
				}
			}
			else if(split[0].equals("gesture"))
			{
				if(split[1].equals("pinch"))
				{
					robot.mouseWheel(Integer.valueOf(split[2]).intValue());
				}
				else if(split[1].equals("swipe"))
				{
					if(split[2].equals("right"))
					{
						robot.keyPress(39);
						robot.keyRelease(39);
					}
					else if(split[2].equals("left"))
					{
						robot.keyPress(37);
						robot.keyRelease(37);
					}
							
				}
				else if(split[1].equals("special"))
				{
					
					if(split[2].equals("2up"))
					{
						if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")&&System.getProperty("os.version").contains("10.7"))
						{
							robot.keyPress(120);
							robot.keyRelease(120);
						}
						else if(System.getProperty("os.name").equalsIgnoreCase("Linux"))
						{
							robot.keyPress(18);
							robot.keyPress(9);
							robot.keyRelease(9);
							altpressed=true;
							linuxUp=true;
							
						}
						
					}
					else if(split[2].equals("2down"))
					{
						if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")&&System.getProperty("os.version").contains("10.7"))
						{
							robot.keyPress(121);
							robot.keyRelease(121);
						}
						else if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X"))
						{
							robot.keyPress(120);
							robot.keyRelease(120);
						}
						else if(System.getProperty("os.name").equalsIgnoreCase("Linux"))
						{
							if(linuxUp)
								robot.keyRelease(18);
						}
					}
					else if(split[2].equals("2left"))
					{
						
						if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")&&System.getProperty("os.version").contains("10.7"))
						{
							robot.keyPress(17);
							robot.keyPress(37);
							robot.keyRelease(37);
							robot.keyRelease(17);
						}
						else if(System.getProperty("os.name").equalsIgnoreCase("Linux"))
						{
							robot.keyPress(17);
							robot.keyPress(18);
							robot.keyPress(37);
							robot.keyRelease(37);
							robot.keyRelease(18);
							robot.keyRelease(17);
						}
						else if(System.getProperty("os.name").contains("Windows"))
						{
							
							robot.keyPress(18);
							robot.keyPress(9);
							robot.keyRelease(9);
							robot.keyPress(37);
							robot.keyRelease(37);
							robot.keyRelease(18);
						}
					}
					else if(split[2].equals("2right"))
					{
						if(System.getProperty("os.name").equalsIgnoreCase("Mac OS X")&&System.getProperty("os.version").contains("10.7"))
						{
							robot.keyPress(17);
							robot.keyPress(39);
							robot.keyRelease(39);
							robot.keyRelease(17);
						}
						else if(System.getProperty("os.name").equalsIgnoreCase("Linux"))
						{
							robot.keyPress(17);
							robot.keyPress(18);
							robot.keyPress(39);
							robot.keyRelease(39);
							robot.keyRelease(18);
							robot.keyRelease(17);
						}
						else if(System.getProperty("os.name").contains("Windows"))
						{
							robot.keyPress(18);
							robot.keyPress(9);
							robot.keyRelease(9);
							robot.keyPress(39);
							robot.keyRelease(39);
							robot.keyRelease(18);
						}
						
					}
				}
				
			}
			
			else if(split[0].equals("airmouse"))
			{
				try
				{
					String res="connect";
					byte buf[]=new byte[1500];
					buf=res.getBytes();
					InetAddress add=packet.getAddress();
					System.out.println("Address"+add.toString());
					
					int port=packet.getPort();
					System.out.println("port+"+port);
					DatagramSocket socket=new DatagramSocket();
					DatagramPacket response=new DatagramPacket(buf, buf.length,add,port);
					socket.send(response);
				}
				catch (SocketException e) {
					// TODO: handle exception
					System.out.println("Socket Error");
				}
				catch(IOException e)
				{
					System.out.println("Io Exception");
				}
				
				
			}
		}
		catch(UnsupportedEncodingException e)
		{
			
		}
		catch(AWTException e)
		{
			
		}
	}
	
	public void cleanup()
	{
		if (ctrlpressed)
			robot.keyRelease(17);
		if (altpressed)
			robot.keyRelease(18);
		if (capspressed)
			robot.keyRelease(20);
		if (shiftpressed)
			robot.keyRelease(16);
	}
	
	

}
