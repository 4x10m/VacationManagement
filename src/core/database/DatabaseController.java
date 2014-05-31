package core.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class DatabaseController {
	private static Connection sqlconnection;
	
	public static void setConnection(DatabaseConnector connector) {
		sqlconnection = connector.getConnection();
	}
	
	public static void update(DatabaseEntity databaseentity) {
		String sqlrequestbase = "update " + databaseentity.getTableName() + " set %s = ? where id = ?";
		String sqlrequesttemp = null;
		
		Map<String, String> databaseentitydata = databaseentity.serialize();
		Set<String> keys = databaseentitydata.keySet();
		Iterator<String> keysiterator  = keys.iterator();
		
		while(keysiterator.hasNext()) {
			String key = keysiterator.next();
			
			sqlrequesttemp = String.format(sqlrequestbase, key);
			
			PreparedStatement statement;
			try {
				statement = sqlconnection.prepareStatement(sqlrequesttemp);
				statement.setString(1, databaseentitydata.get(key));
				statement.setInt(2, Integer.parseInt(databaseentitydata.get("id")));
				statement.executeQuery();
			} catch (SQLException e) {
				System.out.println(String.format("Update Error : %s", sqlrequesttemp));
				
				e.printStackTrace();
			}
		}
	}
	
	public static int insert(DatabaseEntity databaseentity) {
		String idsqlrequest = "select max(id) from " + databaseentity.getTableName();
		int id = 0;
		
		Map<String, String> data = databaseentity.serialize();
		Set<String> keys = data.keySet();
		
		String sqlrequest = "insert into " + databaseentity.getTableName() + " (";
		
		for(String key : keys.toArray(new String[keys.size()])) {
			if(key != "id") {
				sqlrequest += "\"" + key + "\"";
				sqlrequest += ", ";
			}
		}
		
		sqlrequest = sqlrequest.substring(0, sqlrequest.length() - 2);
		sqlrequest += ")  values (";
	
		for(String key : keys.toArray(new String[keys.size()])) {
			if(key != "id") {
				sqlrequest += "'" + (String)data.get(key) + "'";
				sqlrequest += ", ";
			}
		}
		
		sqlrequest = sqlrequest.substring(0, sqlrequest.length() - 2);
		sqlrequest += ");";
		
		PreparedStatement statement;
		try {
			statement = sqlconnection.prepareStatement(sqlrequest);
			statement.execute();
			
			statement = sqlconnection.prepareStatement(idsqlrequest);
			ResultSet result = statement.executeQuery();
			
			result.next();
			id = result.getInt(1);
		} catch (SQLException e) {
			System.out.println(String.format("insert error : %s", sqlrequest));
			System.out.println(e);
		}
		
		return id;
	}

	public static Map<String, String>[] selectAll(String tablename) {
		String sqlrequest = "select * from " + tablename;
		ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		
		try {
			PreparedStatement statement = sqlconnection.prepareStatement(sqlrequest);
			
			ResultSet result = statement.executeQuery();
			
			while (result.next()) {
				Map<String, String> databaseentitydata = new HashMap<String, String>();
				
				String[] keys = getResultSetKeys(result);
				String[] values = getResultSetValues(result);
		
				if(keys.length == values.length) {
					for(int i = 0; i < keys.length; i++) {
						databaseentitydata.put(keys[i], values[i]);
					}
				
					maps.add(databaseentitydata);
				}
			}
			
			Map<String, String>[] maparray = maps.toArray(new Map[maps.size()]);
			
			return maparray;
		} catch (SQLException e) {
			System.out.println(String.format("select all error : %s", sqlrequest));
			
			e.printStackTrace();
		}
		
		return null;
	}

	public static void refresh(DatabaseEntity databaseentity) {
		String sqlrequest = "select * from " + databaseentity.getTableName() + " where id = ?";
		
		Map<String, String> databaseentitydata = null;
		DatabaseEntity backup = databaseentity;
		
		try {
			PreparedStatement statement = sqlconnection.prepareStatement(sqlrequest);
			statement.setInt(1, databaseentity.getID());
			
			statement.executeQuery();
			
			ResultSet result = statement.executeQuery();
			result.next();
			
			databaseentitydata = new HashMap<String, String>();
			
			String[] keys = getResultSetKeys(result);
			String[] values = getResultSetValues(result);
	
			if(keys.length == values.length) {
				for(int i = 0; i < keys.length; i++) {
					databaseentitydata.put(keys[i], values[i]);
				}
				
				databaseentity.deserialize(databaseentitydata);
			}
		} catch (SQLException e) {
			System.out.println(String.format("Refresh error : %s, %d", databaseentity.getTableName(), databaseentity.getID()));
			System.out.println(String .format("old object content : %s", backup));
			System.out.println(String .format("downloaded content : %s", databaseentitydata));
			System.out.println(String .format("actual object content : %s", databaseentity));
		}
	}

	public static Map<String, String> getEntityData(DatabaseEntity entity) {
		return getEntityData(entity.getTableName(), entity.getID());
	}

	public static Map<String, String> getEntityData(String tableName, int id) {
		String sqlrequest = "select * from " + tableName + " where id = ?";
		
		try {
			PreparedStatement statement= sqlconnection.prepareStatement(sqlrequest);
			statement.setInt(1, id);
			
			statement.executeQuery();
			
			ResultSet result = statement.executeQuery();
			result.next();
			
			Map<String, String> databaseentitydata = new HashMap<String, String>();
			
			String[] keys = getResultSetKeys(result);
			String[] values = getResultSetValues(result);
	
			if(keys.length == values.length) {
				for(int i = 0; i < keys.length; i++) {
					databaseentitydata.put(keys[i], values[i]);
				}
			
				return databaseentitydata;
			}
		} catch (SQLException e) {
			System.out.println(String.format("get entity data error : %s", sqlrequest));
			
			e.printStackTrace();
		}
	
		return null;
	}

	public static String[] getResultSetKeys(ResultSet result) throws SQLException {
		   ResultSetMetaData resultsetmetadata = result.getMetaData();
		   String[] keys = new String[resultsetmetadata.getColumnCount()];
		   
		   for(int i = 0; i < keys.length; i++){
		      String columnname = resultsetmetadata.getColumnName(i+1);
		      
		      keys[i] = columnname;
		   }
		   
		   return keys;
	}

	public static String[] getResultSetValues(ResultSet result) throws SQLException {
		ResultSetMetaData resultsetmetadata = result.getMetaData();
		   String[] values = new String[resultsetmetadata.getColumnCount()];
		   
		   for(int i = 0; i < values.length; i++){
		      String columnname = result.getString(i+1);
		      
		      
		      values[i] = columnname;
		   }
		   
		   return values;
	}

	public static int checkCredential(String username, String password) {
		String slqrequest = "select id from users where email = ? and password = ?";

		try {
			PreparedStatement statement = sqlconnection.prepareStatement(slqrequest);
			
			statement.setString(1, username);
			statement.setString(2, password);
			
			ResultSet result = statement.executeQuery();
			
			result.next();
			
			int id = result.getInt("id");
			
			return id;
		} catch (SQLException e) {
			System.out.println(String.format("check credential error : %s", slqrequest));
		}
		
		return 0;
	}

	public static void delete(DatabaseEntity databaseEntity) {
		String sqlrequest = "delete from " + databaseEntity.getTableName() + " where id = ?";
		
		try {
			PreparedStatement statement = sqlconnection.prepareStatement(sqlrequest);
			statement.setInt(1, databaseEntity.getID());
			
			statement.execute();
		} catch (SQLException e) {
			System.out.println(String.format("delete error %s, %d", sqlrequest, databaseEntity.getID()));
			
			e.printStackTrace();
		}
	}
}
