package swd.logistics.bo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KdniaoConfig {

    @Value("${kdniao.EBusinessID}")
    public String EBusinessID;

    @Value("${kdniao.AppKey}")
    public String AppKey;

    @Value("${kdniao.xd}")
    public String xd;

    @Value("${kdniao.cancelXd}")
    public String cancelXd;

    @Value("${kdniao.cx}")
    public String cx;

    @Value("${kdniao.dy}")
    public String dy;

}
