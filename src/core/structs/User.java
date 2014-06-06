package core.structs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import core.database.DatabaseEntity;
import core.enums.UserType;
import core.exceptions.InvalidCredentialException;

public abstract class User extends DatabaseEntity {
	private String username;
	private String password;
	
	@Override
	protected String getTableName() {
		return "users";
	}
	
	public String getUsername() {
		return username;
	}
	
	public String password() {
		return password;
	}
	
	public User(int id) {
		super(id);
		
		load();
	}
	
	public static User identify(String username, String password) throws InvalidCredentialException {
		User user = null;
		String slqrequest = "select id, privilege from users where email = ? and password = ?";
		
		try {
			PreparedStatement statement = getSQLConnection().prepareStatement(slqrequest);

			statement.setString(1, username);
			statement.setString(2, password);

			ResultSet result = statement.executeQuery();

			result.next();

			int id = result.getInt("id");
			
			switch(UserType.valueOf(result.getString("privilege"))) {
			case CDS:
				user = new CDS(id);
				break;
			case EMPLOYE:
				user = new Employe(id);
				break;
			case HR:
				user = new HR(id);
				break;
			}
		} catch (SQLException e) {
			throw new InvalidCredentialException();
		}
		
		return user;
	}

	@Override
	public Map<String, String> serialize() {
		Map<String, String> data = new HashMap<String, String>();
		
		data.put("id", String.valueOf(getID()));
		data.put("email", this.username);
		data.put("password", this.password);
		
		return data;
	}
	
	@Override
	public void deserialize(Map<String, String> data) {
		this.username = data.get("email");
		this.password = data.get("password");
	}
}
