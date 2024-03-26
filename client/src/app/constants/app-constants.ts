import { environment } from "../../environments/environment.development";

export const AppConstants = {
    PURCHASE_URL: environment.baseUrl + "/checkout/purchase",
    SHIPPING_ADDRESS_URL: environment.baseUrl + "/api/shipping-address",
    LOGIN_URL: environment.baseUrl + "/member/details",
    PRODUCT_URL: environment.baseUrl + "/api/products",
    CATEGORY_URL: environment.baseUrl + "/api/product-category",
    INVENTORY_URL: environment.baseUrl + "/inventory",
    ORDER_HISTORY_URL: environment.baseUrl + "/order-history"
}
