package swd.logistics.logisticsUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import swd.logistics.bo.Customer;

@Component
public class CustomerUtil {
    @Value("${kdniao.yd.CustomerName}")
    public String CustomerName;//韵达
    @Value("${kdniao.yd.CustomerPwd}")
    public String CustomerPwd;//韵达

    @Value("${kdniao.sto.CustomerName}")
    public String CustomerNameSTO;//申通
    @Value("${kdniao.sto.CustomerPwd}")
    public String CustomerPwdSTO;//申通
    @Value("${kdniao.sto.CustomerSendSite}")
    public String CustomerSendSite;//申通

    @Value("${kdniao.HTKY.CustomerName}")
    public String CustomerNameHTKY;//百世
    @Value("${kdniao.HTKY.CustomerPwd}")
    public String CustomerPwdHTKY;//百世

    public  Customer getCustomer(String shipperCode){
        Customer customer = null;
        switch (shipperCode){
            case "STO":customer = new Customer();//申通
            customer.setCustomerName("斯沃德校服");
            customer.setCustomerPwd(CustomerPwdSTO);
            customer.setSendSite("江西南昌联发公司");
            customer.setPayType(3);
            customer.setTemplateSize("150");break;
            case "HHTT":customer = new Customer();//天天
                customer.setCustomerName("testhhtt");
                customer.setCustomerPwd("testhhttpwd");
                customer.setSendSite("testhhttsite");
                customer.setTemplateSize("");break;
            case "ZTO":customer = new Customer();//中通
                customer.setCustomerName("testzto");
                customer.setCustomerPwd("testztopwd");
                customer.setSendSite("");
                customer.setTemplateSize("");break;
            case "YD":customer = new Customer();//韵达
                customer.setCustomerName(CustomerName);
                customer.setCustomerPwd(CustomerPwd);
                customer.setSendSite("");
                customer.setTemplateSize("180");break;
            case "HTKY":customer = new Customer();//百世
                customer.setCustomerName(CustomerNameHTKY);
                customer.setCustomerPwd(CustomerPwdHTKY);
                customer.setSendSite("");
                customer.setTemplateSize("");break;
            case "YTO":customer = new Customer();//圆通
                customer.setCustomerName("testyto");
                customer.setCustomerPwd("");
                customer.setSendSite("");
                customer.setTemplateSize("180");break;
            case "DBL":customer = new Customer();//德邦
                customer.setCustomerName("testdbl");
                customer.setCustomerPwd("");
                customer.setSendSite("");
                customer.setTemplateSize("18002");
                customer.setPayType(3);break;//月结模式
            case "YZBK":customer = new Customer();//邮政国内标快
                customer.setCustomerName("");
                customer.setCustomerPwd("");
                customer.setSendSite("");
                customer.setTemplateSize("150");break;
                default:customer = new Customer();//默认空
                    customer.setCustomerName("");
                    customer.setCustomerPwd("");
                    customer.setSendSite("");
                    customer.setTemplateSize("");break;
        }
        return customer;
    }
}
