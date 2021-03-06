package swd.logistics.bo;

import org.springframework.stereotype.Component;

@Component
public class Customer {

    private int PayType = 1;//支付方式

    private String ExpType = "1";//快递业务类型，默认为1，标准快递

    private String customerName;

    private String customerPwd;

    private String SendSite;

    private String templateSize;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPwd() {
        return customerPwd;
    }

    public void setCustomerPwd(String customerPwd) {
        this.customerPwd = customerPwd;
    }

    public String getSendSite() {
        return SendSite;
    }

    public void setSendSite(String sendSite) {
        SendSite = sendSite;
    }

    public String getTemplateSize() {
        return templateSize;
    }

    public void setTemplateSize(String templateSize) {
        this.templateSize = templateSize;
    }

    public int getPayType() {
        return PayType;
    }

    public void setPayType(int payType) {
        PayType = payType;
    }

    public String getExpType() {
        return ExpType;
    }

    public void setExpType(String expType) {
        ExpType = expType;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "PayType=" + PayType +
                ", ExpType='" + ExpType + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerPwd='" + customerPwd + '\'' +
                ", SendSite='" + SendSite + '\'' +
                ", templateSize='" + templateSize + '\'' +
                '}';
    }
}
