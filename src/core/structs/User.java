package core.structs;

import java.util.HashMap;
import java.util.Map;

import core.database.DatabaseController;
import core.database.DatabaseEntity;
import core.enums.UserType;

public abstract class User extends DatabaseEntity {
	private static final String TABLE_NAME = "users";
	
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	
	public String password() {
		return password;
	}
	
	public User(int id) {
		super(TABLE_NAME);
		
		setID(id);
		
		deserialize(DatabaseController.getEntityData(this));
	}

	public User(String username, String password) {
		super(TABLE_NAME);
		
		this.username = username;
		this.password = password;
	}

	public boolean checkCredential() {
		int id = DatabaseController.checkCredential(username, password);
		
		if(id > 0) {
			load(id);
			
			return true;
		}
		
		return false;
	}
	
	public void load(int id) {
		setID(id);
		
		deserialize(DatabaseController.getEntityData(this));
	}
	
	public static User static_load(int id) {
		User result = null;
		
		Map<String, String> data = DatabaseController.getEntityData(TABLE_NAME, id);
		
		UserType type = UserType.valueOf((data.get("privilege")));
		
		switch (type) {
		case CDS:
			result = new CDS(id);
			break;
		case EMPLOYE:
			result = new Employe(id);
			break;
		case HR:
			result = new HR(id);
			break;
		}
		
		return result;
	}

	@Override
	public Map<String, String> serialize() {
		Map<String, String> data = new HashMap<String, String>();
		
		data.put("id", String.valueOf(getID()));
		data.put("email", this.username);
		data.put("password", this.password);
		
		if(this instanceof Employe) {
			data.put("privilege", String.valueOf(UserType.EMPLOYE));
		}
		
		if(this instanceof CDS) {
			data.put("privilege", String.valueOf(UserType.CDS));
		}
		
		if(this instanceof HR) {
			data.put("privilege", String.valueOf(UserType.HR));
		}
		
		return data;
	}
	
	@Override
	public void deserialize(Map<String, String> data) {
		setID(Integer.parseInt(data.get("id")));
		
		this.username = data.get("email");
		this.password = data.get("password");
	}
}
