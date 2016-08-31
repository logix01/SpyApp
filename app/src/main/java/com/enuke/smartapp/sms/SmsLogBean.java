package com.enuke.smartapp.sms;

public class SmsLogBean {
	
	private String mId;
	private String mPhone;
	private String mSmsBody;
	private String mType;
	private String mDate;
	private String mPhoneName;
	private String mDuration;
	private String mStaus;
	

	
	public SmsLogBean(String mPhone,String mSmsBody,String mType,String mPhoneName,String mDate,String mStaus){
		super();
		this.mPhone=mPhone;
		this.setmSmsBody(mSmsBody);
		this.mType=mType;
		this.mPhoneName=mPhoneName;	
		this.mDate=mDate;
		this.mStaus=mStaus;
		
				
	}
	
	public SmsLogBean() {
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
		
	}public String getId(String mId) {
		return mId;
		
		}

	/**
	 * @return the mSmsBody
	 */
	public String getmSmsBody() {
		return mSmsBody;
	}

	/**
	 * @param mSmsBody the mSmsBody to set
	 */
	public void setmSmsBody(String mSmsBody) {
		this.mSmsBody = mSmsBody;
	}
}
