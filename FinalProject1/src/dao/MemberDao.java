package dao;

import java.util.List;

import model.Member;

public interface MemberDao {

	//c
	void addMember(Member e);
	//r
	List<Member> selectAll();
	List<Member> selectByid(int id);
	List<Member> selectByno(String memberno);
	Member selectaddno(String memberid);

	//u
	void updateMember(Member m);

	//d
	void deleteMember(int id);
	
}