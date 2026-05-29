import { environment } from "../../environments/environment";

export const AppConstants = {
    PURCHASE_URL: environment.baseUrl + "/checkout/purchase",
    SHIPPING_ADDRESS_URL: environment.baseUrl + "/shipping-address",
    LOGIN_URL: environment.baseUrl + "/member/details",
    PRODUCT_URL: environment.baseUrl + "/products",
    CATEGORY_URL: environment.baseUrl + "/product-category",
    INVENTORY_URL: environment.baseUrl + "/inventory",
    ORDER_HISTORY_URL: environment.baseUrl + "/order-history",
    REQUEST_URL: environment.baseUrl + "/pay/request",
    CONFIRM_URL: environment.baseUrl + "/pay/confirm"
}
