package ir.pitok.cafe.models.responseModels.dataModels;

import com.google.gson.annotations.SerializedName;

public class PaymentDataModel {

    @SerializedName("message")
    private String message;

    @SerializedName("total_amounts")
    private int totalAmounts;

    @SerializedName("discount_amount")
    private int discountAmount;

    @SerializedName("amount_for_pay")
    private int amountForPay;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalAmounts() {
        return totalAmounts;
    }

    public void setTotalAmounts(int totalAmounts) {
        this.totalAmounts = totalAmounts;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int getAmountForPay() {
        return amountForPay;
    }

    public void setAmountForPay(int amountForPay) {
        this.amountForPay = amountForPay;
    }
}
