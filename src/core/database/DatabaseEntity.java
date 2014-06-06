package core.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import core.exceptions.AlreadyIdentifiedException;

public abstract class DatabaseEntity implements Cloneable {
	///STATIC ATTRIBUTES
	private static Connection sqlconnection;
	
	///PRIVATE ATTRIBUTES
	private int id;
	
	
	///CONSTRUCTORS
	protected DatabaseEntity() { }
	protected DatabaseEntity(int id) {
		try {
			setID(id);
		} catch (AlreadyIdentifiedException e) {
			e.printStackTrace();
		}
	}
	
	
	///PRIVATE METHODS	
	private final void setID(int id) throws AlreadyIdentifiedException {
		//delete() set the id to 0
		if(id != 0) {
			//if this object have already an id
			if(getID() == 0) this.id = id;
			else throw new AlreadyIdentifiedException();
		}
		else id = 0;
	}
	
	protected static Connection getSQLConnection() {
		if(sqlconnection == null) {
			System.out.println("Tentative de connexion a la base de donnée");
	
			try {
				String url = "jdbc:postgresql://localhost/vacation_management";
				sqlconnection = DriverManager.getConnection(url, "postgres",
						"postgres");
			} catch (SQLException e) {
				e.printStackTrace();
			}
	
			System.out.println("connexion a la base de donnée établie");
		}
		
		return sqlconnection;
	}
	
	private static String[] getResultSetKeys(ResultSet result) throws SQLException {
		   ResultSetMetaData resultsetmetadata = result.getMetaData();
		   String[] keys = new String[resultsetmetadata.getColumnCount()];
		   
		   for(int i = 0; i < keys.length; i++){
		      String columnname = resultsetmetadata.getColumnName(i+1);
		      
		      keys[i] = columnname;
		   }
		   
		   return keys;
	}
	private static String[] getResultSetValues(ResultSet result) throws SQLException {
		ResultSetMetaData resultsetmetadata = result.getMetaData();
		   String[] values = new String[resultsetmetadata.getColumnCount()];
		   
		   for(int i = 0; i < values.length; i++){
		      String columnname = result.getString(i+1);
		      
		      
		      values[i] = columnname;
		   }
		   
		   return values;
	}
	
	
	///ABSTRACT METHODS
	protected abstract String getTableName();
	
	protected abstract Map<String, String> serialize();
	protected abstract void deserialize(Map<String, String> data);
	
	
	///PROTECTED METHODS
	public final int getID() {
		return id;
	}

	protected void save() {
		//update this if this have an id
		if(getID() > 0) {
			String sqlrequestbase = "update " + this.getTableName() + " set %s = '%s' where id = ?";
			String sqlrequesttemp = null;
			
			Map<String, String> databaseentitydata = this.serialize();
			databaseentitydata.put("id", String.valueOf(getID()));
			
			Set<String> keys = databaseentitydata.keySet();
			Iterator<String> keysiterator  = keys.iterator();
			
			while(keysiterator.hasNext()) {
				String key = keysiterator.next();
				
				if(key != "id") {
					PreparedStatement statement;
					try {
						sqlrequesttemp = String.format(sqlrequestbase, key, databaseentitydata.get(key));
						
						statement = getSQLConnection().prepareStatement(sqlrequesttemp);
						statement.setInt(1, Integer.parseInt(databaseentitydata.get("id")));
						statement.executeUpdate();
					} catch (SQLException e) {
						System.out.println(String.format("Update Error : %s", sqlrequesttemp));
						
						e.printStackTrace();
					}
				}
			}
		}
		//insert this if this have no id
		else {
			String idsqlrequest = "select max(id) from " + this.getTableName();
			
			Map<String, String> data = this.serialize();
			Set<String> keys = data.keySet();
			
			String sqlrequest = "insert into " + this.getTableName() + " (";
			
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
				statement = getSQLConnection().prepareStatement(sqlrequest);
				statement.execute();
				
				statement = getSQLConnection().prepareStatement(idsqlrequest);
				ResultSet result = statement.executeQuery();
				
				result.next();
				
				this.setID(result.getInt(1));
			} catch (SQLException e) {
				System.out.println(String.format("insert error : %s", sqlrequest));
				System.out.println(e);
			} catch (AlreadyIdentifiedException e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void load() {
		String sqlrequest = "select * from " + getTableName() + " where id = ?";
		
		try {
			PreparedStatement statement= getSQLConnection().prepareStatement(sqlrequest);
			statement.setInt(1, getID());
			
			statement.executeQuery();
			
			ResultSet result = statement.executeQuery();
			result.next();
			
			Map<String, String> databaseentitydata = new HashMap<String, String>();
			
			String[] keys = getResultSetKeys(result);
			String[] values = getResultSetValues(result);
	
			if(keys.length == values.length) {
				for(int i = 0; i < keys.length; i++) {
					if(keys[i] != "id") {
						databaseentitydata.put(keys[i], values[i]);
					}
				}
			
				deserialize(databaseentitydata);
			}
		} catch (SQLException e) {
			System.out.println(String.format("get entity data error : %s", sqlrequest));
			
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public void delete() {
		String sqlrequest = "delete from " + this.getTableName() + " where id = ?";
		
		try {
			PreparedStatement statement = getSQLConnection().prepareStatement(sqlrequest);
			statement.setInt(1, this.getID());
			
			statement.execute();
			
			this.setID(0);
		} catch (SQLException e) {
			System.out.println(String.format("delete error %s, %d", sqlrequest, this.getID()));
			
			e.printStackTrace();
		} catch (AlreadyIdentifiedException e) {
			e.printStackTrace();
		}
	}
	
	
	///STATIC METHODS
	protected static DatabaseEntity[] loadAll(DatabaseEntity entity) {
		ArrayList<DatabaseEntity> deserializedentitys = new ArrayList<DatabaseEntity>();
		
		String sqlrequest = "select * from " + entity.getTableName();
		ArrayList<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		
		try {
			PreparedStatement statement = getSQLConnection().prepareStatement(sqlrequest);
			
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
			
			for(int i = 0; i < maparray.length; i++) {
				try {
					DatabaseEntity clone = (DatabaseEntity) entity.clone();
					
					clone.deserialize(maparray[i]);
					
					try {
						clone.setID(Integer.parseInt(maparray[i].get("id")));
					} catch (NumberFormatException | AlreadyIdentifiedException e) {
						e.printStackTrace();
					}
					
					deserializedentitys.add(clone);
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
				

			}
		} catch (SQLException e) {
			System.out.println(String.format("select all error : %s", sqlrequest));
			
			e.printStackTrace();
		}
		
		return deserializedentitys.toArray(new DatabaseEntity[deserializedentitys.size()]);
	}
}
