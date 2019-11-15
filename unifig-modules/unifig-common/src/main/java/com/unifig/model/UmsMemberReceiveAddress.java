package com.unifig.model;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class UmsMemberReceiveAddress implements Serializable {

    @ApiModelProperty("收货地址id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long memberId;

    /**
     * 收货人名称
     *
     * @mbggenerated
     */
    @ApiModelProperty("收货人名称")
    private String name;

    @ApiModelProperty("电话号码")
    private String phoneNumber;

    /**
     * 是否为默认
     *
     * @mbggenerated
     */
    @ApiModelProperty("是否为默认")
    private Integer defaultStatus;

    /**
     * 邮政编码
     *
     * @mbggenerated
     */
    @ApiModelProperty("邮政编码")
    private String postCode;

    /**
     * 省份/直辖市
     *
     * @mbggenerated
     */
    @ApiModelProperty("省份/直辖市")
    private String province;

    /**
     * 城市
     *
     * @mbggenerated
     */
    @ApiModelProperty("城市")
    private String city;

    /**
     * 区
     *
     * @mbggenerated
     */
    @ApiModelProperty("区")
    private String region;

    /**
     * 详细地址(街道)
     *
     * @mbggenerated
     */
    @ApiModelProperty("详细地址(街道)")
    private String detailAddress;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getDefaultStatus() {
        return defaultStatus;
    }

    public void setDefaultStatus(Integer defaultStatus) {
        this.defaultStatus = defaultStatus;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", memberId=").append(memberId);
        sb.append(", name=").append(name);
        sb.append(", phoneNumber=").append(phoneNumber);
        sb.append(", defaultStatus=").append(defaultStatus);
        sb.append(", postCode=").append(postCode);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", region=").append(region);
        sb.append(", detailAddress=").append(detailAddress);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}