package tools;

import java.io.*;
import java.math.*;
import java.security.*;
import java.security.spec.*;
import java.sql.*;
import java.util.*;

/**
 * DatabaseIO class
 * 
 * @author jdhenry08 and lundbj86
 */
public class DatabaseIO {
	private static Connection db;
	private static final int BUFFER_SIZE = (int) Math.pow(2, 6);

	/**
	 * Connect method that connects to the db
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	private static void connect() throws IOException, SQLException {
		Properties prop = new Properties();
		FileInputStream in = new FileInputStream("user.properties");
		prop.load(in);
		in.close();

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException cnfe) {
			// Big error here!
		}

		String user = prop.getProperty("username");
		String pass = prop.getProperty("password");
		String url = "jdbc:mysql://cs346.cs.uwosh.edu:4381/" + user;
		db = DriverManager.getConnection(url, user, pass);
	}

	/**
	 * Disconnect method that disconnects from the db
	 *
	 * @throws SQLException 
	 */
	private static void disconnect() throws SQLException {
		db.close();
	}

	/**
	 * testConnection method checks to see if the user can access the db
	 * 
	 * @return boolean based on if the testConnection passed
	 */
	public static boolean testConnection() {
		try {
			connect();
			disconnect();
			return true;
		} catch(Exception e) {
			return false;
		}
	}

	/**
	 * upload method inserts a file into the db for the user that
	 * is passed in as a parameter
	 * 
	 * @param fileName file name for the file being uploaded
	 * @param userName name of the user that the file is being added for
	 * @throws FileNotFoundException 
	 */
	public static void upload(String fileName, String userName) throws FileNotFoundException {
		String insertZip = "INSERT INTO files (user_name,file_name,zip) VALUES (?, ?, ?)";
		FileInputStream fis = null;
		PreparedStatement ps = null;

		try {
			connect();
			db.setAutoCommit(false);
			File file = new File(fileName + FileIO.ZIP);
			fis = new FileInputStream(file);
			ps = db.prepareStatement(insertZip);
			ps.setString(1, userName);
			ps.setString(2, fileName);
			ps.setBinaryStream(3, fis, (int) file.length());
			ps.executeUpdate();
			db.commit();
			ps.close();
			fis.close();
			disconnect();
		} catch(SQLException sqle) {
			// Big error here!
		} catch(IOException ioe) {
			// Big error here!
		}
	}

	/**
	 * download method retrieves a file into the db for the user that
	 * is passed in as a parameter
	 * 
	 * @param fileName file name for the file being download
	 * @param userName name of the user that the file is being downloaded for
	 */
	public static void download(String fileName, String userName) {
		String query = "SELECT zip FROM files WHERE file_name LIKE ? AND user_name LIKE ?";

		try {
			connect();
			PreparedStatement ps = db.prepareStatement(query);
			ps.setString(1, fileName);
			ps.setString(2, userName);
			ResultSet rs = ps.executeQuery();
			rs.next();
			Blob blob = rs.getBlob(1);
			FileOutputStream out = new FileOutputStream(fileName + FileIO.ZIP);
			BufferedInputStream in = new BufferedInputStream(blob
					.getBinaryStream());
			int b;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((b = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
				out.write(buffer, 0, b);
			}
			out.close();
			rs.close();
			ps.close();
			disconnect();
		} catch(Exception e) {
			// Big error here!
		}
	}

	/**
	 * getSaves collects the list of file names that are saved under the user's name
	 * 
	 * @param userName name of user that we are checking files for
	 * @return Vector<String> of names of files for passed in user
	 */
	public static Vector<String> getSaves(String userName) {
		Vector<String> files = new Vector<String>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = "SELECT file_name FROM files WHERE user_name = ?";

		try {
			connect();
			ps = db.prepareStatement(query);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			rs.next();
			while (!rs.isAfterLast()) {
				files.add(rs.getString(1));
				rs.next();
			}
			rs.close();
			ps.close();
			disconnect();
		} catch(SQLException sqle) {
			// Big error here!
		} catch(IOException ioe) {
			// Big error here!
		}

		return files;
	}

	/**
	 * getUserList method gathers the names of all users in the db
	 * 
	 * @return Vector<String> of all users in the system
	 */
	public static Vector<String> getUserList() {
		Vector<String> users = new Vector<String>();
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = "SELECT user_name FROM users";

		try {
			connect();
			ps = db.prepareStatement(query);
			rs = ps.executeQuery();
			rs.next();
			while (!rs.isAfterLast()) {
				users.add(rs.getString(1));
				rs.next();
			}
			rs.close();
			ps.close();
			disconnect();
		} catch(SQLException sqle) {
			// Big error here!
		} catch(IOException ioe) {
			// Big error here!
		}

		return users;
	}

	/**
     * getUsersPublicKey will get the user's public key as a BigInt
     * 
     * @param userName name of user that key is being retrieved for
     * @return BigInteger representation of public key
     * @throws SQLException
     */
	public static PublicKey getUsersPublicKey(String userName) {
		PublicKey pubKey = null;
		Blob blob = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = "SELECT public_key FROM users WHERE user_name like ?";

		try {
			connect();
			KeyFactory fact = KeyFactory.getInstance("rsa");
			ps = db.prepareStatement(query);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			rs.next();
			blob = rs.getBlob(1);
			BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
			int k1 = in.available();
			byte[] kb = new byte[k1];
			in.read(kb);
			in.close();
			X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(kb);
			pubKey = fact.generatePublic(pubKeySpec);
			rs.close();
			ps.close();
			disconnect();
		} catch(SQLException sqle) {
			// Big error here!
		} catch(IOException ioe) {
			// Big error here!
		} catch(NoSuchAlgorithmException nsae) {
			// Big error here!
		} catch(InvalidKeySpecException ikse) {
			// Big error here!
		}

		return pubKey;
	}

	/**
	 * setupUser sets the values for the user in the db
	 * 
	 * @param userName users name
	 * @param key BigInt value of public key
	 * @param password setup per user
	 * @throws SQLException
	 */
	public static void setupUser(String userName, String password) {
		String insertZip = "INSERT INTO users(user_name,public_key,password) VALUES(?, ?, ?)";
		BigInteger hashedPW = DatabaseIO.getMD5Hash(password);
		FileInputStream fis = null;
		PreparedStatement ps = null;
		try {
			FileIO.makeRSAKeys(userName, password);
			connect();
			db.setAutoCommit(false);
			File file = new File(userName + FileIO.PUB);
			fis = new FileInputStream(file);
			ps = db.prepareStatement(insertZip);
			ps.setString(1, userName);
			ps.setBinaryStream(2, fis, (int) file.length());
			ps.setBinaryStream(3, new ByteArrayInputStream(hashedPW.toByteArray()));
			ps.executeUpdate();
			db.commit();
			ps.close();
			disconnect();
		} catch(SQLException sqle) {
		} catch(IOException ioe) {
		}
	}

	/**
	 * getHash takes a string and returns the big int representation of that big in
	 * based on the MD5 algorithm
	 * 
	 * @param password to be hashed
	 * @return BigInteger representation of that pw
	 */
	private static BigInteger getMD5Hash(String password) {
		BigInteger hash = null;
		
		byte[] pwBytes = password.getBytes();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(pwBytes);
			byte messageDigest[] = md.digest();
			hash = new BigInteger(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		}
		return hash;
	}
	
	/**
	 * getHash takes a string and returns the big int representation of that big in
	 * based on the RSA algorithm
	 * 
	 * @param password to be hashed
	 * @return BigInteger representation of that pw
	 */
	public static PublicKey getRSAHash(String password) {
		PublicKey pub =  null;
		
		try {
			KeyPairGenerator kg = KeyPairGenerator.getInstance("rsa");
			SecureRandom secureSeed = SecureRandom.getInstance("SHA1PRNG");
			byte[] keyData = password.getBytes();
			secureSeed.setSeed(keyData);
			kg.initialize(512, secureSeed);
			KeyPair pair = kg.generateKeyPair();
			pub = pair.getPublic();
			
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		}
		return pub;
	}
	
	/**
	 * validateUser validates the user with the db.  This will return true if validation works or
	 * false if the user gives the wrong pw
	 * 
	 * @param userName
	 * @param password
	 * @return boolean true for validated or false if not.
	 */
	public static boolean validateUser(String userName, String password) {
		Blob key = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		if(userExists(userName)) {
			String query = "SELECT password FROM users WHERE user_name LIKE ?";
	
			try {
				connect();
				ps = db.prepareStatement(query);
				ps.setString(1, userName);
				rs = ps.executeQuery();
				rs.next();
				key = rs.getBlob(1);
				rs.close();
				ps.close();
				disconnect();
				return getMD5Hash(password).equals(new BigInteger(key.getBytes(1, (int)key.length())));
			} catch(SQLException e) {
				e.printStackTrace();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * userExists checks to see if the user exists
	 * 
	 * @param userName
	 * @return boolean true if exists false if not
	 */
	public static boolean userExists(String userName) {
		String name = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = "SELECT user_name FROM users WHERE user_name LIKE ?";

		try {
			connect();
			ps = db.prepareStatement(query);
			ps.setString(1, userName);
			rs = ps.executeQuery();
			rs.next();
			name = rs.getObject(1).toString();
			rs.close();
			ps.close();
		} catch(SQLException sqle) {
			// Big error here!
		} catch(IOException ioe) {
			// Big error here!
		}

		return userName.equals(name);
	}
	
	/**
	 * delete method removes all entries with file name
	 * 
	 * @param fileName to be deleted
	 */
	public static void delete(String fileName) {
		if(fileExists(fileName))
		{	
			PreparedStatement ps = null;
			String query = "DELETE FROM files WHERE file_name LIKE ?";
			try {
				connect();
				ps = db.prepareStatement(query);
				ps.setString(1, fileName);
				ps.execute();
				ps.close();
			} catch(SQLException sqle) {
				// Big error here!
			} catch(IOException ioe) {
				// Big error here!
			}
		}

	}
	
	public static boolean fileExists(String fileName) {
		String name = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String query = "SELECT file_name FROM files WHERE file_name LIKE ?";

		try {
			connect();
			ps = db.prepareStatement(query);
			ps.setString(1, fileName);
			rs = ps.executeQuery();
			rs.next();
			name = rs.getObject(1).toString();
			rs.close();
			ps.close();
		} catch(SQLException sqle) {
			// Big error here!
		} catch(IOException ioe) {
			// Big error here!
		}

		return fileName.equals(name);
	}
}