package cn.gljp.sign.admin.entity;

import java.io.Serializable;

public class Config implements Serializable {
    private String webSiteName;
    private String webSiteClose;
    private Integer schoolStructure;
    private Integer currentPici;
    private String currentGrade;
    private String configGroupList;
    private String bankCode;
    private String expandedUnitCode;
    private String LOGOIMG;
    private String BGIMG;
    private String webSiteAddress;
    private String admissionsProfile;

    public String getBGIMG() {
        return BGIMG;
    }

    public void setBGIMG(String BGIMG) {
        this.BGIMG = BGIMG;
    }

    public String getWebSiteAddress() {
        return webSiteAddress;
    }

    public void setWebSiteAddress(String webSiteAddress) {
        this.webSiteAddress = webSiteAddress;
    }

    public String getAdmissionsProfile() {
        return admissionsProfile;
    }

    public void setAdmissionsProfile(String admissionsProfile) {
        this.admissionsProfile = admissionsProfile;
    }

    public String getLOGOIMG() {
        return LOGOIMG;
    }

    public void setLOGOIMG(String LOGOIMG) {
        this.LOGOIMG = LOGOIMG;
    }

    public String getExpandedUnitCode() {
        return expandedUnitCode;
    }

    public void setExpandedUnitCode(String expandedUnitCode) {
        this.expandedUnitCode = expandedUnitCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    private String chargeAddress;
    public String getChargeAddress() {
        return chargeAddress;
    }

    public void setChargeAddress(String chargeAddress) {
        this.chargeAddress = chargeAddress;
    }

    public Integer getLastSnoBumber() {
        return lastSnoBumber;
    }

    public void setLastSnoBumber(Integer lastSnoBumber) {
        this.lastSnoBumber = lastSnoBumber;
    }

    private String limitClassType;
    private Integer lastSnoBumber;
    public String getLimitClassType() {
        return limitClassType;
    }

    public void setLimitClassType(String limitClassType) {
        this.limitClassType = limitClassType;
    }

    public Config() {
    }


    public String getConfigGroupList() {
        return configGroupList;
    }

    public void setConfigGroupList(String configGroupList) {
        this.configGroupList = configGroupList;
    }

    public String getWebSiteName() {
        return webSiteName;
    }

    public void setWebSiteName(String webSiteName) {
        this.webSiteName = webSiteName;
    }

    public String getWebSiteClose() {
        return webSiteClose;
    }

    public void setWebSiteClose(String webSiteClose) {
        this.webSiteClose = webSiteClose;
    }

    public Integer getSchoolStructure() {
        return schoolStructure;
    }

    public void setSchoolStructure(Integer schoolStructure) {
        this.schoolStructure = schoolStructure;
    }

    public Integer getCurrentPici() {
        return currentPici;
    }

    public void setCurrentPici(Integer currentPici) {
        this.currentPici = currentPici;
    }

    public String getCurrentGrade() {
        return currentGrade;
    }

    public void setCurrentGrade(String currentGrade) {
        this.currentGrade = currentGrade;
    }
    @Override
    public String toString() {
        return "Config{" +
                "webSiteName='" + webSiteName + '\'' +
                ", webSiteClose='" + webSiteClose + '\'' +
                ", schoolStructure=" + schoolStructure +
                ", currentPici=" + currentPici +
                ", currentGrade='" + currentGrade + '\'' +
                ", configGroupList='" + configGroupList + '\'' +
                ", limitClassType='" + limitClassType + '\'' +
                ", lastSnoBumber=" + lastSnoBumber +
                '}';
    }

}
