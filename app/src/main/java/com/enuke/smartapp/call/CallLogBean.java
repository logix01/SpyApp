package com.enuke.smartapp.call;

public class CallLogBean {
	
	private String mId;
	private String mPhone;
	private String mType;
	private String mDate;
	private String mPhoneName;
	private String mDuration;
	private String mStaus;
	

	
	public CallLogBean(String mPhone,String mType,String mPhoneName,String mDuration,String mDate,String mStaus){
		super();
		this.mPhone=mPhone;
		this.mType=mType;
		this.mPhoneName=mPhoneName;
		this.mDuration=mDuration;
		this.mDate=mDate;
		this.mStaus=mStaus;
		
				
	}
	
	public CallLogBean() {
		// TODO Auto-generated constructor stub
	}

	


	public String getmPhone() {
		return mPhone;
	}
	public void setmPhone(String mPhone) {
		this.mPhone = mPhone;
	}
	public String getmType() {
		return mType;
	}
	public void setmType(String mType) {
		this.mType = mType;
	}
	public String getmDate() {
		return mDate;
	}
	public void setmDate(String mDate) {
		this.mDate = mDate;
	}
	/**
	 * @return the mPhoneName
	 */
	public String getmPhoneName() {
		return mPhoneName;
	}
	/**
	 * @param mPhoneName the mPhoneName to set
	 */
	public void setmPhoneName(String mPhoneName) {
		this.mPhoneName = mPhoneName;
	}
	/**
	 * @return the mDuration
	 */
	public String getmDuration() {
		return mDuration;
	}
	/**
	 * @param mDuration the mDuration to set
	 */
	public void setmDuration(String mDuration) {
		this.mDuration = mDuration;
	}
	/**
	 * @return the mStaus
	 */
	public String getmStaus() {
		return mStaus;
	}
	/**
	 * @param mStaus the mStaus to set
	 */
	public void setmStaus(String mStaus) {
		this.mStaus = mStaus;
	}

	public void setId(String mId) {
	this.mId=mId;
		
	}
}
