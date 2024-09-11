package service;

import java.util.List;


import model.Member;

public interface MemberService {
	void addMember(Member m);
	List<Member> Select();
	List<Member> selectByno(String memberno);
	Member queryById(int id);
	
	
	void update(int id,String name,String password,String phone,String birthday,String email,String address,String grade);
	
	
	void delete(int id);

}