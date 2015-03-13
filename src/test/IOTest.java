package test;

import java.io.*;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.xml.stream.*;
import org.junit.*;
import static org.junit.Assert.*;

import gui.*;
import resources.*;
import tools.*;

public class IOTest {
	private static SurmountTableModel testModel;
	/*
	@BeforeClass
	public static void setup() {
		try {
			testModel = FileIO.load("test", "user", "pass");
			FileIO.save("test2", "user", "pass", testModel);
			testModel = FileIO.load("test2", "user", "pass");
		} catch(IOException e) {
			System.out.println(e.getMessage());
		} catch(XMLStreamException e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void test_in_a() {
		assertEquals("2+3",testModel.getValueAt(0, 0));
	}

	@Test
	public void test_in_b() {
		assertEquals("A1",testModel.getValueAt(1, 0));
	}

	@Test
	public void test_in_c() {
		assertEquals("5",testModel.getValueAt(2, 0));
	}

	@Test
	public void test_in_d() {
		assertEquals("poop",testModel.getValueAt(4, 4));
	}
	
	@Test
	public void test_in_e() {
		assertEquals("5+5", testModel.getValueAt(50, 50));
	}
	
	@Test
	public void test_in_f() {
		try {
			assertEquals("5",Tools.getExpression("poop"));
		} catch(NoSuchVariableExistsException e) {
			System.out.println(e.getMessage());
		} catch(VariableAssignmentException e) {
			System.out.println(e.getMessage());
		} catch(InvalidExpressionException e) {
			System.out.println(e.getMessage());
		} catch(DivideByZeroException e) {
			System.out.println(e.getMessage());
		} catch(CircularReferenceException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
	@Test
	public void test_db_upload()
	{
		try {
			FileIO.connect();
			FileIO.upload("lundbj86", "test");
			FileIO.disconnect();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	*/
	String user1 = "testuser1";
	String pw1 = "group4test1";
	String user2 = "testuser2";
	String pw2 = "group4test2";
	String user3 = "testuser3";
	String pw3 = "group4test3";
	
	@Before
	public void setup()
	{
		try {
			FileIO.makeRSAKeys(user3, pw3);
			if(!DatabaseIO.userExists(user3))
				DatabaseIO.setupUser(user3,pw3);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	@Test
	public void test_users1()
	{
		try {
			assertEquals(true, DatabaseIO.testConnection());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void test_users2()
	{
		try {
			assertEquals(true,DatabaseIO.validateUser(user3, pw3));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void test_users3()
	{
		try {
			KeyFactory keyFactory = KeyFactory.getInstance("rsa");
			FileInputStream fIn = new FileInputStream(user3 + ".pub");
			int k1 = fIn.available();
			byte[] kb = new byte[k1];
			fIn.read(kb);
			fIn.close();
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(kb);
			Key pubKey = keyFactory.generatePublic(pubKeySpec);
			assertEquals(pubKey,DatabaseIO.getRSAHash(pw3));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void test_users4()
	{
		try {
			assertEquals(DatabaseIO.getRSAHash(pw3),DatabaseIO.getUsersPublicKey(user3));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void test_delete()
	{
		DatabaseIO.delete("test");
	}
	
	
}