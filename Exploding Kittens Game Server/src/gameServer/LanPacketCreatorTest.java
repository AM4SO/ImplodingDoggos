package gameServer;

import static org.junit.jupiter.api.Assertions.*;

import java.net.DatagramPacket;

import org.junit.jupiter.api.Test;

class LanPacketCreatorTest {
	// At this point in the project, I have come to realise that a good way to test and debug different modules of my code is to use
	// test cases. These can be reused when making changes to my code to ensure that my code is still working after changes, and it 
	// keeps my testing code separate from my actual program code. This ensures that my program code is more readable, less cluttered
	// with debugging comments and commented out code whilst also allowing me to keep my testing code, rather than removing it when i'm 
	// finished with it. Unfortunately, I realised this quite far into the project, and so my documentation is likely to be lacking for
	// previous areas of the code. 
	@Test
	void test() {
		GameMaker gameMaker = new GameMaker(25565,"testing",0,2,5,"");
		
		String initial = gameMaker.lanDiscoveryServer.packetHandler.toString();
		System.out.println("converting to bytes: \n".concat(initial));
		byte[] test = new byte[1024];
		DatagramPacket createdPacket = gameMaker.lanDiscoveryServer.packetHandler.createResponse(new DatagramPacket(test,1024));
		PacketCreator x = PacketCreator.fromPacket(createdPacket);
		String recreated = x.toString();
		System.out.println("Back to object: \n".concat(recreated));
		gameMaker.end();
		/// Although the initial and recreated versions of the object appeared to return the same string, the assertion
		/// to test if the strings are equal failed. I decided to use another assertion to check if the lengths are the same, 
		/// and they the assertion failed at this point. Looking at the console output, it can actually be seen that the 'gameName'
		/// attribute has been padded when converting it back from a byte array to a PacketCreator object. 
		assertTrue(initial.length() == recreated.length());
		assertTrue(initial.equals(recreated));
	}

}