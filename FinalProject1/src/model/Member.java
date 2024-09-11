package model;

import java.io.Serializable;

public class Member implements Serializable{
	private Integer id;
	private String memberno;
	private String name;
	private String memberid;
	private String password;
	private String phone;
	private String birthday;
	private String email;
	private String address;
	private String grade;
	public Member() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Member(String memberno, String name, String memberid, String password, String phone, String birthday,
			String email, String address, String grade) {
		super();
		this.memberno = memberno;
		this.name = name;
		this.memberid = memberid;
		this.password = password;
		this.phone = phone;
		this.birthday = birthday;
		this.email = email;
		this.address = address;
		this.grade = grade;
	}


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMemberno() {
		return memberno;
	}
	public void setMemberno(String memberno) {
		this.memberno = memberno;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMemberid() {
		return memberid;
	}
	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}


	public String getGrade() {
		return grade;
	}


	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	

}