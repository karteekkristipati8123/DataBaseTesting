package storedProcedureTesting;

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

import com.mysql.cj.jdbc.CallableStatement;
import com.mysql.cj.util.StringUtils;

public class SPTesting { 
		Connection con = null;
		Statement stmt = null;
		ResultSet rs1;
		ResultSet rs2;
		java.sql.CallableStatement cStmt;
		@BeforeClass
		void setup() throws SQLException {
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels", "root", "KKR#23kgrw9");
		}
		
		@AfterClass
		void teardown() throws SQLException {
			con.close();
		}
//		@Test(priority=1)
//		void test_storedprocedureexists() throws SQLException
//		{
//			stmt=con.createStatement();
//			rs1=stmt.executeQuery("SHOW PROCEDURE  STATUS WHERE Name ='SelectAllCustomers';");
//			rs1.next();
//			Assert.assertEquals(rs1.getString("Name"),"SelectAllCustomers");	
//		}
////		@Test(priority=2)
//		void test_SelectAllCustomers() throws SQLException {
//			cStmt=con.prepareCall("{call SelectAllCustomers()}");
//			rs1=cStmt .executeQuery();
//			Statement stmt=con.createStatement();
//			rs2=stmt.executeQuery("select * from customers");
//			Assert.assertEquals(compareResultSets(rs1, rs2),true);
//		}
//		@Test(priority=3)
//		void test_SelectAllCustomersByCity() throws SQLException {
//			cStmt=con.prepareCall("{call SelectAllCustomersByCity(?)}");
//			cStmt.setString(1, "Singapore");
//			rs1=cStmt .executeQuery();
//			Statement stmt=con.createStatement();
//			rs2=stmt.executeQuery("SELECT  * FROM customers WHERE city= 'Singapore' ");
//			Assert.assertEquals(compareResultSets(rs1, rs2),true);
////			Assert.assertEquals(compareResultSets(rs1, rs2),true);
//		}
		
//	
//		@Test(priority=5)
//		void test_get_order_by_cust() throws SQLException {
//			cStmt=con.prepareCall("{call_get_order_by_cust(?,?,?,?,?)}");
//			cStmt.setInt(1, 141);
//			
//			cStmt.registerOutParameter(2, Types.INTEGER);
//			cStmt.registerOutParameter(3, Types.INTEGER);
//			cStmt.registerOutParameter(4, Types.INTEGER);
//			cStmt.registerOutParameter(5, Types.INTEGER);
//			
//			cStmt.executeQuery();
//			
//			int shipped =cStmt.getInt(2);
//			int canceled =cStmt.getInt(3);
//			int resolved =cStmt.getInt(4);
//			int disputed =cStmt.getInt(5);
//			
//			System.out.println(shipped+"  "+canceled+"  "+ resolved+"  "+disputed);
//			
//			Statement stmt = con.createStatement();
//			rs1=stmt.executeQuery("select(SELECT count(*) as 'shipped' From orders WHERE customerNumber =141 AND status ='shipped') as Shipped,(SELECT count(*) as 'canceled' From orders WHERE customerNumber =141 AND status ='canceled') as Canceled,(SELECT count(*) as 'resolved' From orders WHERE customerNumber =141 AND status ='resolved') as Resolved,(SELECT count(*) as 'disputed' From orders WHERE customerNumber =141 AND status ='disputed') as Disputed");
//			rs1.next();
//			
//			int exp_shipped =rs1.getInt("shipped");
//			int exp_canceled =rs1.getInt("canceled");
//			int exp_resolved =rs1.getInt("resolved");
//			int exp_disputed =rs1.getInt("disputed");
//			
//			if(shipped==exp_shipped && canceled==exp_canceled && resolved== exp_resolved && disputed==exp_disputed)
//					Assert.assertTrue(true);
//			else
//				   Assert.assertTrue(false);
//		}
		
		@Test(priority=6)
		void test_get_customerShipping() throws SQLException {
			cStmt=con.prepareCall("{call GetCustomerShipping(?,?)}");
			cStmt.setInt(1, 112);			
			cStmt.registerOutParameter(2, Types.VARCHAR);
			
			cStmt.executeQuery();
			
			String shippingTime=cStmt.getString(2);
				
			Statement stmt = con.createStatement();
			rs1=stmt.executeQuery("SELECT country, CASE WHEN country = 'USA' THEN '2-day Shipping' WHEN country = 'CANADA' THEN '3-day shipping' ELSE '5-day shipping' END as ShippingTime FROM customers WHERE customerNumber=112;");
			rs1.next();
			
			String exp_shippingTime=rs1.getString("shippingTime");
		
			Assert.assertEquals(shippingTime, exp_shippingTime);
		
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
