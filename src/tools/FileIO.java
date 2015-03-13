package tools;

import java.io.*;
import java.math.*;
import java.security.*;
import java.security.spec.*;
import java.util.*;
import java.util.Map.*;
import java.util.zip.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.xml.stream.*;

import gui.*;
import resources.*;

/**
 * FileIO class
 * 
 * @author jdhenry08 and lundbj86
 */
public class FileIO {
	public static final String EXT = ".sur";
	public static final String ZIP = ".sz";
	public static final String PRI = ".pri";
	public static final String PUB = ".pub";
	private static final int BUFFER_SIZE = (int)Math.pow(2, 13);

	private static BigInteger password;
	private static SecretKey key;
	private static Cipher des;

	public static SurmountTableModel load(String fileName, String userName, String password)
										throws IOException, XMLStreamException {
		makeRSAKeys(userName, password);
		DatabaseIO.download(fileName, userName);
		decompress(fileName);
		SurmountTableModel model = parseIn(fileName, userName);
		new File(fileName + EXT).delete();
		return model;
	}

	public static void save(String fileName, String userName, SurmountTableModel model)
										throws IOException, XMLStreamException {
		parseOut(fileName, userName, model);
		compress(fileName);
		DatabaseIO.upload(fileName, userName);
		new File(fileName + ZIP).delete();
	}

	/**
	 * parseIn method reads in an XML document
	 * 
	 * @param fileName String that is the file to parse in
	 * @param model SurmountTableModel to be edited
	 * @throws XMLStreamException 
	 * @throws IOException 
	 */
	private static SurmountTableModel parseIn(String fileName, String userName)
										throws XMLStreamException, IOException {
		SurmountTableModel model =
			new SurmountTableModel(SurmountTable.NUM_ROWS,
								   SurmountTable.NUM_COLS,
								   true);

		ArrayList<String> cellList = new ArrayList<String>();
		ArrayList<String> varList = new ArrayList<String>();

		String lastXMLName = null;

		Reader fIn = new FileReader(fileName + EXT);
		XMLStreamReader in =
			XMLInputFactory.newInstance().createXMLStreamReader(fIn);

		while(in.hasNext()) {
			int eventType = in.getEventType();
			switch(eventType) {
			case XMLStreamConstants.START_ELEMENT:
				if(in.getAttributeCount() == 0 && in.getLocalName().equals("DESKey")) {
					lastXMLName = "pwd";
				} else if(in.getAttributeCount() == 1) {
					varList.add(in.getAttributeValue(0));
					lastXMLName = "var";
				} else if(in.getAttributeCount() == 2) {
					cellList.add(in.getAttributeValue(0));
					cellList.add(in.getAttributeValue(1));
					lastXMLName = "cell";
				}
				break;
			case XMLStreamConstants.END_ELEMENT:
				break;
			case XMLStreamConstants.CHARACTERS:
				if(lastXMLName.equals("cell")) {
					cellList.add(decryptDES(in.getText()));
				} else if(lastXMLName.equals("var")) {
					varList.add(decryptDES(in.getText()));
				} else if(lastXMLName.equals("pwd")) {
					password = decryptRSA(userName, new BigInteger(in.getText()));
				}
				break;
			}

			in.next();
		}

		Iterator<String> iter = cellList.iterator();
		while(iter.hasNext()) {
			int row = Integer.parseInt(iter.next());
			int col = Integer.parseInt(iter.next());
			String val = iter.next();

			model.setValueAt(val, row - 1, col);
		}

		iter = varList.iterator();
		while(iter.hasNext()) {
			String var = iter.next();
			String val = iter.next();

			try {
				Tools.addVariable(var + ":" + val);
			} catch(Exception e) { }
		}

		fIn.close();
		in.close();

		return model;
	}

	/**
	 * parseOut method turns a model into a XML document
	 * 
	 * @param fileName String the name of the file being written to
	 * @param model SurmountTableModel the is being written from
	 * @throws XMLStreamException 
	 * @throws IOException 
	 */
	private static void parseOut(String fileName, String userName, SurmountTableModel model)
										throws XMLStreamException, IOException {
		FileOutputStream fOut = new FileOutputStream(fileName + EXT);
		XMLStreamWriter out =
			XMLOutputFactory.newInstance().createXMLStreamWriter(fOut);

		password = new BigInteger(makePass().getBytes());

		out.writeStartDocument("1.0");
		out.writeStartElement("spreadsheet");
		out.writeStartElement("DESKey");
		out.writeCharacters(encryptRSA(userName, password).toString());
		out.writeEndElement();

		Set<Entry<String,String>> vars = Tools.getVariableList();
		Iterator<Entry<String,String>> varIt = vars.iterator();

		while(varIt.hasNext()) {
			Entry<String,String> a = varIt.next();
			char[] cellLoc = a.getKey().toCharArray();
			if(Character.isDigit(cellLoc[1])) {
				String col = "" + (Character.getNumericValue(cellLoc[0]) - 10);
				int rowNum = 0;
				for(int i = 1; i < cellLoc.length; i++) {
					rowNum = (10 * rowNum) + Integer.parseInt(String.valueOf(cellLoc[i]));
				}
				String row = String.valueOf(rowNum);
				out.writeStartElement("cell");
				out.writeAttribute("row", row);
				out.writeAttribute("column", col);
				out.writeCharacters(encryptDES(a.getValue()));
				out.writeEndElement();
			} else if(Character.isDigit(cellLoc[2])) {
				String col = "" + ((26 * (Character.getNumericValue(cellLoc[0]) - 9)) + Character.getNumericValue(cellLoc[1]) - 10);
				int rowNum = 0;
				for(int i = 2; i < cellLoc.length; i++) {
					rowNum = (10 * rowNum) + Integer.parseInt(String.valueOf(cellLoc[i]));
				}
				String row = String.valueOf(rowNum);
				out.writeStartElement("cell");
				out.writeAttribute("row", row);
				out.writeAttribute("column", col);
				out.writeCharacters(encryptDES(a.getValue()));
				out.writeEndElement();
			} else {
				out.writeStartElement("var");
				out.writeAttribute("name", a.getKey());
				out.writeCharacters(encryptDES(a.getValue()));
				out.writeEndElement();
			}
		}

		out.writeEndElement();
		out.writeEndDocument();
		out.flush();
		fOut.close();
		out.close();
	}

	private static String encryptDES(String data) {
		try {
			DESKeySpec keySpec = new DESKeySpec(password.toByteArray());
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
			key = factory.generateSecret(keySpec);
			des = Cipher.getInstance("des");
			InputStream in = new ByteArrayInputStream(data.getBytes());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			des.init(Cipher.ENCRYPT_MODE, key);

			byte[] input = new byte[64];
			while(true) {
				int bytesRead = in.read(input);
				if(bytesRead == -1) break;
				byte[] output = des.update(input, 0, bytesRead);
				if(output != null) out.write(output);
			}

			byte[] output = des.doFinal();
			if(output != null) out.write(output);
			out.flush();
			String answer = new BigInteger(out.toByteArray()).toString();

			in.close();
			out.close();

			return answer;
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
		} catch(BadPaddingException bpe) {
			bpe.printStackTrace();
		} catch(InvalidKeyException ike) {
			ike.printStackTrace();
		} catch(NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch(InvalidKeySpecException ikse) {
			ikse.printStackTrace();
		} catch(NoSuchPaddingException nspe) {
			nspe.printStackTrace();
		}

		return null;
	}

	private static String decryptDES(String dataStr) {
		try {
			BigInteger data = new BigInteger(dataStr);
			DESKeySpec keySpec = new DESKeySpec(password.toByteArray());
			SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");
			key = factory.generateSecret(keySpec);
			des = Cipher.getInstance("des");
			InputStream in = new ByteArrayInputStream(data.toByteArray());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			des.init(Cipher.DECRYPT_MODE, key);

			byte[] input = new byte[64];
			while(true) {
				int bytesRead = in.read(input);
				if(bytesRead == -1) break;
				byte[] output = des.update(input, 0, bytesRead);
				if(output != null) out.write(output);
			}

			byte[] output = des.doFinal();
			if(output != null) out.write(output);
			out.flush();
			String answer = out.toString();

			in.close();
			out.close();

			return answer;
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
		} catch(NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch(InvalidKeySpecException ikse) {
			ikse.printStackTrace();
		} catch(NoSuchPaddingException nspe) {
			nspe.printStackTrace();
		} catch (IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
		} catch (BadPaddingException bpe) {
			bpe.printStackTrace();
		}

		return null;
	}

	private static BigInteger encryptRSA(String userName, BigInteger toEncrypt) {
		try {
			InputStream in = new ByteArrayInputStream(toEncrypt.toByteArray());
			Key pubKey = DatabaseIO.getUsersPublicKey(userName);

			Cipher rsa = Cipher.getInstance("rsa");
			rsa.init(Cipher.ENCRYPT_MODE, pubKey);
			ArrayList<Byte> bytes = new ArrayList<Byte>();
			byte[] input = new byte[BUFFER_SIZE];
			while(true) {
				int bytesRead = in.read(input);
				if(bytesRead == -1) break;
				byte[] output = rsa.update(input, 0, bytesRead);
				if(output != null) {
					for(int i = 0; i < output.length; i++) {
						bytes.add(new Byte(output[i]));
					}
				}
			}

			byte[] output = rsa.doFinal();
			if(output != null) {
				for(int i = 0; i < output.length; i++) {
					bytes.add(new Byte(output[i]));
				}
			}
			in.close();
			Object dataWrap[] = bytes.toArray();
			byte data[] = new byte[dataWrap.length];
			for(int i = 0; i < dataWrap.length; i++) {
				data[i] = ((Byte)dataWrap[i]).byteValue();
			}

			return new BigInteger(data);
		} catch(Exception e) {
			// Big error here!
		}

		return null;
	}

	private static BigInteger decryptRSA(String userName, BigInteger data) {
		try {
			InputStream in = new ByteArrayInputStream(data.toByteArray());
			KeyFactory keyFactory = KeyFactory.getInstance("rsa");
			FileInputStream fIn = new FileInputStream(userName + PRI);
			int k1 = fIn.available();
			byte[] kb = new byte[k1];
			fIn.read(kb);
			fIn.close();
			PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(kb);
			Key priKey = keyFactory.generatePrivate(priKeySpec);

			Cipher rsa = Cipher.getInstance("rsa");
			rsa.init(Cipher.DECRYPT_MODE, priKey);

			String decrypted = "";
			byte[] input = new byte[BUFFER_SIZE];
			while(true) {
				int bytesRead = in.read(input);
				if(bytesRead == -1) break;
				byte[] output = rsa.update(input, 0, bytesRead);
				if(output != null) {
					decrypted = decrypted.concat(new String(output));
				}
			}

			byte[] output = rsa.doFinal();
			if(output != null) {
				decrypted = decrypted.concat(new String(output));
			}
			in.close();
			return new BigInteger(decrypted.getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void makeRSAKeys(String userName, String password) {
		try {
			KeyPairGenerator kg = KeyPairGenerator.getInstance("rsa");
			SecureRandom secureSeed = SecureRandom.getInstance("SHA1PRNG");
			byte[] keyData = password.getBytes();
			secureSeed.setSeed(keyData);
			kg.initialize(512, secureSeed);
			KeyPair pair = kg.generateKeyPair();
			PrivateKey priKey = pair.getPrivate();
			PublicKey pubKey = pair.getPublic();
			FileOutputStream out = new FileOutputStream(userName + PRI);
			out.write(priKey.getEncoded());
			out.close();
			out = new FileOutputStream(userName + PUB);
			out.write(pubKey.getEncoded());
			out.close();
		} catch(Exception e) {
			// Big error here!
		}
	}

	private static String makePass() {
		char[] chars = new char[8];
		for(int i = 0; i < chars.length; i++) {
			chars[i] = (char)(33 + ((int)(Math.random() * 94) % 94));
		}

		return String.valueOf(chars);
	}

	private static void compress(String fileName) throws IOException {
		FileOutputStream out = new FileOutputStream(fileName + ZIP);
		GZIPOutputStream zOut = new GZIPOutputStream(out);
		FileInputStream in = new FileInputStream(fileName + EXT);
		byte[] buffer = new byte[BUFFER_SIZE];

		int len;
		while((len = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
			zOut.write(buffer, 0, len);
		}

		in.close();
		zOut.close();

		new File(fileName + EXT).delete();
	}

	private static void decompress(String fileName) throws IOException {
		FileInputStream in = new FileInputStream(fileName + ZIP);
		GZIPInputStream zIn = new GZIPInputStream(in);
		FileOutputStream out = new FileOutputStream(fileName + EXT);
		byte[] buffer = new byte[BUFFER_SIZE];

		int len;
		while((len = zIn.read(buffer, 0, BUFFER_SIZE)) != -1) {
			out.write(buffer, 0, len);
		}

		out.close();
		zIn.close();

		new File(fileName + ZIP).delete();
	}
}