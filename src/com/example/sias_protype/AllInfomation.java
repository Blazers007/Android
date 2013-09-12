package com.example.sias_protype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.cookie.Cookie;

import android.app.Application;

public class AllInfomation extends Application{
	private String user;
	private String password;
	private List<Cookie> Cookies;
	private ArrayList<HashMap<String,String>> message = new ArrayList<HashMap<String,String>>();
	/**
	 * @return the mesage
	 */
	public ArrayList<HashMap<String, String>> getMessage() {
		return message;
	}
	public void setMessage(ArrayList<HashMap<String, String>> mesage) {
		this.message = mesage;
	}

	private String welComeName;
	/**
	 * @return the welComeName
	 */
	public String getWelComeName() {
		return welComeName;
	}
	/**
	 * @param welComeName the welComeName to set
	 */
	public void setWelComeName(String welComeName) {
		this.welComeName = welComeName;
	}

	private boolean RegState = false;
	/**
	 * @return the regState
	 */
	public boolean isRegState() {
		return RegState;
	}
	/**
	 * @param regState the regState to set
	 */
	public void setRegState(boolean regState) {
		RegState = regState;
	}

	private boolean LogState = false;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
////////////////////////////////////////
	public List<Cookie> getCookies() {
		return Cookies;
	}
	public void setCookies(List<Cookie> cookies) {
		Cookies = cookies;
	}
	
	public boolean getLogState(){
		return LogState;
	}
	
	public void setLogState(boolean logState){
		LogState = logState;
	}
	
	//////////////以下是个人资料  另外能不能在这开个线程 保持发送在线状态？
	private String nickName;
	private String signature;
	private int height;
	private int weight;
	private int skinRating;//可以再返回后除以2？四舍五入 显示自己的被打分？
	private int faceRating;
	
	private int level;//等级跟经验
	private int exp;
	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getSkinRating() {
		return skinRating;
	}

	public void setSkinRating(int skinRating) {
		this.skinRating = skinRating;
	}

	public int getFaceRating() {
		return faceRating;
	}

	public void setFaceRating(int faceRating) {
		this.faceRating = faceRating;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}
}
