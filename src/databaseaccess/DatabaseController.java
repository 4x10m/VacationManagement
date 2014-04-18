package databaseaccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import core.CDS;
import core.Employe;
import core.HR;
import core.Request;
import core.enums.RequestType;

public class DatabaseController {
	// /------------------------
	// /SELECT QUERYS
	// /---------------------
	private static final String SELECT_A_HR_QUERY = "select * from hr where idhr = ?";
	private static final String SELECT_ALL_HR_ID_QUERY = "select idhr from hr";

	private static final String SELECT_A_CDS_QUERY = "select * from cds where idcds = ?";
	private static final String SELECT_ALL_CDS_ID_QUERY = "select idcds from cds";

	private static final String SELECT_AN_EMPLOYE_QUERY = "select * from employe where idemploye = ?";
	private static final String SELECT_ALL_EMPLOYE_ID_QUERY = "select idemploye from employe";

	private static final String SELECT_A_REQUEST_QUERY = "select * from request where idrequest = ?";
	private static final String SELECT_ALL_REQUEST_ID_QUERY = "select idrequest from request";

	// /------------------------
	// /INSERT QUERYS
	// /------------------------
	private static final String INSERT_EMPLOYE_QUERY = "insert into employe (idemploye, idhr, idcds, paidhollidaymeter, reductioninworkinghoursmeter, formationmeter) values (default, ?, ?, ?, ?, ?)";
	private static final String INSERT_NEW_REQUEST_QUERY = "insert into request (idrequest, idemploye, checkbycds, checkbyhr, requesttype, begindate, enddate, motif) values (default, ?, false, false, ?, ?, null)";

	// /------------------------
	// /DELETE QUERYS
	// /------------------------
	private static final String DELETE_REQUEST_QUERY = "delete from request where idrequest = ?";
	private static final String DELETE_EMPLOYE_QUERY = "delete from employe where idemploye = ?";
	private static final String DELETE_HR_QUERY = "delete from hr where idhr = ?";
	private static final String DELETE_CDS_QUERY = "delete from cds where idcds = ?";

	private final DatabaseConnector connector;

	public DatabaseController(final DatabaseConnector connector) {
		this.connector = connector;
	}

	public CDS loadACDS(final Connection connection, final int id) {
		CDS cds = null;

		try {
			PreparedStatement stmt = connection
					.prepareStatement(DatabaseController.SELECT_A_CDS_QUERY);

			stmt.setInt(1, id);

			ResultSet result = stmt.executeQuery();

			result.next();

			cds = new CDS(result.getInt("idcds"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cds;
	}

	public ArrayList<CDS> loadAllCDS(final Connection connection) {
		ArrayList<CDS> cdss = null;

		try {
			PreparedStatement stmt = connection
					.prepareStatement(DatabaseController.SELECT_ALL_CDS_ID_QUERY);

			ResultSet result = stmt.executeQuery();

			while (result.next()) {
				cdss.add(this.loadACDS(connection, result.getInt("idcds")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return cdss;
	}

	public HR loadAHR(final Connection connection, final int id) {
		HR hr = null;

		try {
			PreparedStatement stmt = connection
					.prepareStatement(DatabaseController.SELECT_A_HR_QUERY);

			stmt.setInt(1, id);

			ResultSet result = stmt.executeQuery();

			result.next();

			hr = new HR(result.getInt("idhr"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return hr;
	}

	public ArrayList<HR> loadAllHR(final Connection connection) {
		ArrayList<HR> hrs = null;

		try {
			PreparedStatement stmt = connection
					.prepareStatement(DatabaseController.SELECT_ALL_HR_ID_QUERY);

			ResultSet result = stmt.executeQuery();

			while (result.next()) {
				hrs.add(this.loadAHR(connection, result.getInt("idhr")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return hrs;
	}

	public Employe loadAEmploye(final Connection connection, final int id) {
		Employe employe = null;

		try {
			PreparedStatement stmt = connection
					.prepareStatement(DatabaseController.SELECT_AN_EMPLOYE_QUERY);

			stmt.setInt(1, id);

			ResultSet result = stmt.executeQuery();

			result.next();

			employe = new Employe(result.getInt("idemploye"),
					result.getInt("idhr"), result.getInt("idcds"),
					result.getInt("paidhollidaymeter"),
					result.getInt("reductioninworkinghoursmeter"),
					result.getInt("formationmeter"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return employe;
	}

	public ArrayList<Employe> loadAllEmployes(final Connection connection) {
		ArrayList<Employe> employes = null;

		employes = new ArrayList<Employe>();

		try {
			PreparedStatement stmt = connection
					.prepareStatement(DatabaseController.SELECT_ALL_EMPLOYE_ID_QUERY);

			ResultSet result = stmt.executeQuery();

			while (result.next()) {
				employes.add(this.loadACDS(connection,
						result.getInt("idemploye")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return employes;
	}

	public Request loadARequest(final Connection connection, final int id,
			final Employe employe) {
		Request request = null;

		try {
			PreparedStatement stmt = connection
					.prepareStatement(DatabaseController.SELECT_ALL_REQUEST_ID_QUERY);

			stmt.setInt(1, id);

			ResultSet result = stmt.executeQuery();

			result.next();

			request = new Request(result.getInt("idrequest"), employe,
					RequestType.valueOf(result.getString("requesttype")),
					result.getTimestamp("begindate"),
					result.getTimestamp("enddate"),
					result.getInt("paidhollidaymeter"),
					result.getInt("reductioninworkingtimemeter"),
					result.getInt("formationmeter"),
					result.getBoolean("checkbycds"),
					result.getBoolean("chechbyhr"));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return request;
	}

	public ArrayList<Request> loadAllRequest(final Connection connection) {
		ArrayList<Request> requests = null;

		try {
			PreparedStatement stmt = connection
					.prepareStatement(DatabaseController.SELECT_ALL_REQUEST_ID_QUERY);

			ResultSet result = stmt.executeQuery();

			while (result.next()) {
				requests.add(this.loadARequest(connection,
						result.getInt("idcds")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return requests;
	}

	public void checkRequestByCDS(final Request request) {
		String query = "update request set checkbycds = ? where idrequest = ?";

		try {
			PreparedStatement stmt = this.connector.getConnection()
					.prepareStatement(query);

			stmt.setBoolean(1, request.isCheckCDS());
			stmt.setInt(2, request.getID());

			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void checkRequestByHR(final Request request) {
		String query = "update request set checkbyhr = true where idrequest = ?";

		try {
			PreparedStatement stmt = this.connector.getConnection()
					.prepareStatement(query);

			stmt.setBoolean(1, request.isCheckHR());
			stmt.setInt(2, request.getID());

			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertARequest(final Request request) {
		try {
			PreparedStatement stmt = this.connector.getConnection()
					.prepareStatement(
							DatabaseController.INSERT_NEW_REQUEST_QUERY);

			stmt.setInt(1, request.getID());
			stmt.setInt(2, request.getOwner().getID());
			stmt.setString(3, request.getType().toString());
			stmt.setTimestamp(4, request.getBeggindate());
			stmt.setTimestamp(5, request.getEnddate());
			stmt.setBoolean(6, request.isCheckCDS());
			stmt.setBoolean(7, request.isCheckHR());

			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertANEmploye(final Employe employe) {
		try {
			PreparedStatement stmt = this.connector.getConnection()
					.prepareStatement(DatabaseController.INSERT_EMPLOYE_QUERY);

			stmt.setInt(1, employe.getID());
			stmt.setInt(2, employe.getChefdeservice().getID());
			stmt.setInt(3, employe.getHumanresource().getID());
			stmt.setInt(4, employe.getCompteurconges());
			stmt.setInt(5, employe.getCompteurrtt());
			stmt.setInt(6, employe.getCompteurformation());

			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertACDS(final CDS cds) {
		try {
			PreparedStatement stmt = this.connector.getConnection()
					.prepareStatement("insert into cds (idcds) values(?)");

			stmt.setInt(1, cds.getID());

			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertAHR(final HR hr) {
		try {
			PreparedStatement stmt = this.connector.getConnection()
					.prepareStatement("insert into hr (idhr) values (?)");

			stmt.setInt(1, hr.getID());

			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
