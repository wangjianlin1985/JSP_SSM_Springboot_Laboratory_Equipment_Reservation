package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class Device {
    /*设备编号*/
    @NotEmpty(message="设备编号不能为空")
    private String deviceNo;
    public String getDeviceNo(){
        return deviceNo;
    }
    public void setDeviceNo(String deviceNo){
        this.deviceNo = deviceNo;
    }

    /*设备名称*/
    @NotEmpty(message="设备名称不能为空")
    private String deviceName;
    public String getDeviceName() {
        return deviceName;
    }
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /*设备图片*/
    private String devicePhoto;
    public String getDevicePhoto() {
        return devicePhoto;
    }
    public void setDevicePhoto(String devicePhoto) {
        this.devicePhoto = devicePhoto;
    }

    /*设备单价*/
    @NotNull(message="必须输入设备单价")
    private Float devicePrice;
    public Float getDevicePrice() {
        return devicePrice;
    }
    public void setDevicePrice(Float devicePrice) {
        this.devicePrice = devicePrice;
    }

    /*设备数量*/
    @NotNull(message="必须输入设备数量")
    private Integer stockCount;
    public Integer getStockCount() {
        return stockCount;
    }
    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    /*生产厂家*/
    @NotEmpty(message="生产厂家不能为空")
    private String madePlace;
    public String getMadePlace() {
        return madePlace;
    }
    public void setMadePlace(String madePlace) {
        this.madePlace = madePlace;
    }

    /*出厂日期*/
    @NotEmpty(message="出厂日期不能为空")
    private String outDate;
    public String getOutDate() {
        return outDate;
    }
    public void setOutDate(String outDate) {
        this.outDate = outDate;
    }

    /*设备备注*/
    private String deviceMemo;
    public String getDeviceMemo() {
        return deviceMemo;
    }
    public void setDeviceMemo(String deviceMemo) {
        this.deviceMemo = deviceMemo;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonDevice=new JSONObject(); 
		jsonDevice.accumulate("deviceNo", this.getDeviceNo());
		jsonDevice.accumulate("deviceName", this.getDeviceName());
		jsonDevice.accumulate("devicePhoto", this.getDevicePhoto());
		jsonDevice.accumulate("devicePrice", this.getDevicePrice());
		jsonDevice.accumulate("stockCount", this.getStockCount());
		jsonDevice.accumulate("madePlace", this.getMadePlace());
		jsonDevice.accumulate("outDate", this.getOutDate().length()>19?this.getOutDate().substring(0,19):this.getOutDate());
		jsonDevice.accumulate("deviceMemo", this.getDeviceMemo());
		return jsonDevice;
    }}