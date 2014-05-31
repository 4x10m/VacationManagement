package core.database;

import java.util.ArrayList;
import java.util.Map;

public abstract class DatabaseEntity implements Cloneable {
	private final String tablename;
	private int id;
	
	public String getTableName() {
		return tablename;
	}
	public int getID() {
		return id;
	}
	
	protected void setID(int id) {
		this.id = id;
	}
	
	public DatabaseEntity(String tablename) {
		this.tablename = tablename;
	}
	public DatabaseEntity(String tablename, int id) {
		this.tablename = tablename;
		this.id = id;
		
		load();
	}
	
	public void save() {
		if(getID() != 0) {
			DatabaseController.update(this);
		}
		else {
			setID(DatabaseController.insert(this));
		}
	}
	
	public void load() {
		Map<String ,String> data = DatabaseController.getEntityData(this);
		
		deserialize(data);
	}
	
	public void delete() {
		DatabaseController.delete(this);
	}
	
	public static DatabaseEntity[] loadAll(DatabaseEntity entity) {
		ArrayList<DatabaseEntity> deserializedentitys = new ArrayList<DatabaseEntity>();
		Map<String, String>[] maps = DatabaseController.selectAll(entity.getTableName());
		
		for(int i = 0; i < maps.length; i++) {
			try {
				DatabaseEntity clone = (DatabaseEntity) entity.clone();
				
				clone.deserialize(maps[i]);
				
				deserializedentitys.add(clone);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			

		}
		
		return deserializedentitys.toArray(new DatabaseEntity[maps.length]);
	}
	
	public abstract Map<String, String> serialize();
	public abstract void deserialize(Map<String, String> data);
}
