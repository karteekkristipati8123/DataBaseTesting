package storedFunctionTesting;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SFTesting {
	Connection con = null;
	Statement stmt = null;
	ResultSet rs1;
	ResultSet rs2;
	CallableStatement cStmt;
	@BeforeClass
	void setup() throws SQLException {
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels","root","KKR#23kgrw9");
	}
	
	@AfterClass
	void teardown() throws SQLException {
		con.close();
	}

//	@Test(priority=1)
//	void test_storedFunctionExist() throws SQLException {
//		rs=con.createStatement().executeQuery("SHOW FUNCTION STATUS WHERE Name='customerLevel' ");
//		rs.next();
//		Assert.assertEquals(rs.getString("Name"),"customerLevel");
//	}
//	

//	@Test(priority=2)
//	void test_CustomerLevel_with_SQLstatement() throws SQLException {
//		rs1=con.createStatement().executeQuery("SELECT customerName,customerLevel(creditlimit) FROM customers ");
//		rs2=con.createStatement().executeQuery("SELECT customerName, CASE WHEN creditLimit > 50000 THEN 'Platinum' WHEN creditLimit >= 10000 AND creditLimit <50000 THEN 'GOLD' WHEN creditLimit<10000 THEN 'SILVER' END as customerlevel FROM customers");
//		
//		Assert.assertEquals(compareResultSets(rs1,rs2), true);
//		Assert.assertEquals(compareResultSets(rs1,rs2),true );
//	}
	@Test(priority=3)
	void test_CustomerLevel_with_storedProcedure() throws SQLException {
	cStmt=con.prepareCall("{CALL GetCustomerLevel(?,?)}");
	cStmt.setInt(1, 131);
	cStmt.registerOutParameter(2, Types.VARCHAR);
	cStmt.executeQuery();
	String custlevel=cStmt.getString(2);
	rs1=con.createStatement().executeQuery("	SELECT customerName, CASE WHEN creditLimit > 50000 THEN 'Platinum' WHEN creditLimit >= 10000 AND creditLimit <50000 THEN 'GOLD' WHEN creditLimit<10000 THEN 'SILVER' END as customerlevel FROM customers");
	rs1.next();
	
	String exp_custlevel=rs1.getString("customerlevel");
	
	Assert.assertEquals(custlevel, exp_custlevel);
	
	
	
	}
	public boolean compareResultSets(ResultSet resultSet1,ResultSet resultSet2) throws SQLException {
		while (resultSet1.next())
		{
			resultSet2.next();
			int count =resultSet1.getMetaData().getColumnCount();
			for(int i=1;i<=count;i++)
			{
				if(!org.apache.commons.lang3.StringUtils.equals(resultSet1.getString(i),resultSet2.getString(i)))
				{
					return false;
				}
			}
		}
		return false;
	}
	
}
