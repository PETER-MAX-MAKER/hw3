package dao.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.DbConnection;
import dao.MemberDao;
import model.Member;

public class MemberDaoImpl implements MemberDao{

	public static void main(String[] args) {
		//測試測試~~~
		// TODO Auto-generated method stub
		/*Member m=new Member("","王曉明","A123456789","123","123","123","123","123","123");
		new MemberDaoImpl().addMember(m);*/

		/*List<Member> l=new MemberDaoImpl().selectAll();
		String show="";
		for(Member m:l) {
			show=show+"name:"+m.getName()+"\tEmail:"+m.getEmail()+"\t"+m.getGrade()+"\n";
		}
		System.out.println(show);*/
		
		//Member m=new MemberDaoImpl().selectaddno("zxc");
		//ystem.out.println(m.getEmail());
		
		
		/*List<Member> l=new MemberDaoImpl().selectByid(3);
		Member[] m=l.toArray(new Member[1]);
		m[0].setGrade("白金會員");
		
		new MemberDaoImpl().updateMember(m[0]);*/
		
		//new MemberDaoImpl().deleteMember(3);
		
		List<Member> l=new MemberDaoImpl().selectByno("M0004");
		Member[] m1=l.toArray(new Member[1]);
		System.out.println(m1[0].getName());
		
	}

	@Override
	public void addMember(Member e) {
		// TODO Auto-generated method stub
		Connection conn=DbConnection.getDb();
		String SQL="insert into member(memberno,name,memberid,password,phone,birthday,email,address,grade)values(?,?,?,?,?,?,?,?,?)";
		
		try {
			PreparedStatement ps=conn.prepareStatement(SQL);
			ps.setString(1, e.getMemberno());
			ps.setString(2, e.getName());
			ps.setString(3, e.getMemberid());
			ps.setString(4, e.getPassword());
			ps.setString(5, e.getPhone());
			ps.setString(6, e.getBirthday());
			ps.setString(7, e.getEmail());
			ps.setString(8, e.getAddress());
			ps.setString(9, e.getGrade());
			
			ps.executeUpdate();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
	}

	@Override
	public List<Member> selectAll() {
		// TODO Auto-generated method stub
		Connection conn=DbConnection.getDb();
		String SQL="select *from member";
		List<Member> l=new ArrayList();
		try {
			PreparedStatement ps=conn.prepareStatement(SQL);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
			Member m=new Member();
			m.setId(rs.getInt("id"));
			m.setMemberno(rs.getString("memberno"));
			m.setName(rs.getString("name"));
			m.setMemberid(rs.getString("memberid"));
			m.setPassword(rs.getString("password"));
			m.setPhone(rs.getString("phone"));
			m.setBirthday(rs.getString("birthday"));
			m.setEmail(rs.getString("email"));
			m.setAddress(rs.getString("address"));
			m.setGrade(rs.getString("grade"));
			
			l.add(m);}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}

	@Override
	public Member selectaddno(String memberid) {
		// TODO Auto-generated method stub
		Connection conn=DbConnection.getDb();
		String SQL="select * from member where memberid=?";
		Member m=null;
		try {
			PreparedStatement ps=conn.prepareStatement(SQL);
			ps.setString(1,memberid);
			
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				m=new Member();
				m.setId(rs.getInt("id"));
				m.setMemberno(rs.getString("memberno"));
				m.setName(rs.getString("name"));
				m.setMemberid(rs.getString("memberid"));
				m.setPassword(rs.getString("password"));
				m.setPhone(rs.getString("phone"));
				m.setBirthday(rs.getString("birthday"));
				m.setEmail(rs.getString("email"));
				m.setAddress(rs.getString("address"));
				m.setGrade(rs.getString("grade"));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}
	
	@Override
	public void updateMember(Member m) {
		// TODO Auto-generated method stub
		Connection conn=DbConnection.getDb();
		String SQL="update member set memberno=?,name=?,phone=?,birthday=?,email=?,address=?,grade=? where id=?";
		try {
			PreparedStatement ps=conn.prepareStatement(SQL);
			ps.setString(1, m.getMemberno());
			ps.setString(2, m.getName());
			ps.setString(3, m.getPhone());
			ps.setString(4, m.getBirthday());
			ps.setString(5, m.getEmail());
			ps.setString(6, m.getAddress());
			ps.setString(7, m.getGrade());
			ps.setInt(8, m.getId());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void deleteMember(int id) {
		// TODO Auto-generated method stub
		Connection conn=DbConnection.getDb();
		String SQl="delete from member where id=?";
		try {
			PreparedStatement ps=conn.prepareStatement(SQl);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<Member> selectByid(int id) {
		// TODO Auto-generated method stub
		Connection conn=DbConnection.getDb();
		String SQL="select * from member where id=?";
		List<Member> l=new ArrayList();
		try {
			PreparedStatement ps=conn.prepareStatement(SQL);
			ps.setInt(1,id);
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				Member m=new Member();
				m.setId(rs.getInt("id"));
				m.setMemberno(rs.getString("memberno"));
				m.setName(rs.getString("name"));
				m.setMemberid(rs.getString("memberid"));
				m.setPassword(rs.getString("password"));
				m.setPhone(rs.getString("phone"));
				m.setBirthday(rs.getString("birthday"));
				m.setEmail(rs.getString("email"));
				m.setAddress(rs.getString("address"));
				m.setGrade(rs.getString("grade"));
				
				l.add(m);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}

	@Override
	public List<Member> selectByno(String memberno) {
		// TODO Auto-generated method stub
		Connection conn=DbConnection.getDb();
		String SQL="select * from member where memberno=?";
		List<Member> l=new ArrayList();
		try {
			PreparedStatement ps=conn.prepareStatement(SQL);
			ps.setString(1,memberno);
			
			ResultSet rs=ps.executeQuery();
			if(rs.next()) {
				Member m=new Member();
				m.setId(rs.getInt("id"));
				m.setMemberno(rs.getString("memberno"));
				m.setName(rs.getString("name"));
				m.setMemberid(rs.getString("memberid"));
				m.setPassword(rs.getString("password"));
				m.setPhone(rs.getString("phone"));
				m.setBirthday(rs.getString("birthday"));
				m.setEmail(rs.getString("email"));
				m.setAddress(rs.getString("address"));
				m.setGrade(rs.getString("grade"));
				l.add(m);
				}	
			}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}

	

}