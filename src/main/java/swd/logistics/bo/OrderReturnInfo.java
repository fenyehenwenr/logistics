package swd.logistics.bo;

public class OrderReturnInfo {

    private String UniquerRequestNumber;

    private String ResultCode;

    private String Reason;

    private Boolean Success;

    private PlacingOrderReturn Order;

    private String PrintTemplate;

    public String getUniquerRequestNumber() {
        return UniquerRequestNumber;
    }

    public void setUniquerRequestNumber(String uniquerRequestNumber) {
        UniquerRequestNumber = uniquerRequestNumber;
    }

    public String getResultCode() {
        return ResultCode;
    }

    public void setResultCode(String resultCode) {
        ResultCode = resultCode;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public Boolean getSuccess() {
        return Success;
    }

    public void setSuccess(Boolean success) {
        Success = success;
    }

    public PlacingOrderReturn getOrder() {
        return Order;
    }

    public void setOrder(PlacingOrderReturn Order) {
        this.Order = Order;
    }

    public String getPrintTemplate() {
        return PrintTemplate;
    }

    public void setPrintTemplate(String printTemplate) {
        PrintTemplate = printTemplate;
    }

    @Override
    public String toString() {
        return "OrderReturnInfo{" +
                "UniquerRequestNumber='" + UniquerRequestNumber + '\'' +
                ", ResultCode='" + ResultCode + '\'' +
                ", Reason='" + Reason + '\'' +
                ", Success=" + Success +
                ", Order=" + Order +
                ", PrintTemplate='" + PrintTemplate + '\'' +
                '}';
    }
}
