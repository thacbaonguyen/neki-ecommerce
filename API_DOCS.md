# API Documentation: NEKI E-COMMERCE API
Version: v1

## Endpoints

### `/api/v1/review/update/{id}`

#### `PUT` 
**Tags:** review-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [ReviewRequest](#reviewrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseReviewResponse](#apiresponsereviewresponse) |

---

### `/api/v1/cart/item/update/{cartItemId}/{quantity}`

#### `PUT` 
**Tags:** cart-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `cartItemId` | path | True | integer |  |
| `quantity` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/cart/item/change/{cartItemId}/{quantity}`

#### `PUT` 
**Tags:** cart-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `cartItemId` | path | True | integer |  |
| `quantity` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/auth/users/{userId}/unblock`

#### `PUT` 
**Tags:** user-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `userId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/auth/users/{userId}/block`

#### `PUT` 
**Tags:** user-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `userId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/auth/profile`

#### `GET` 
**Tags:** user-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseUserResponseDTO](#apiresponseuserresponsedto) |

---

#### `PUT` 
**Tags:** user-controller

**Request Body:**

- Content-Type: `application/json` -> [UserUpdateRequest](#userupdaterequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/auth/change-password`

#### `PUT` 
**Tags:** user-controller

**Request Body:**

- Content-Type: `application/json` -> [ChangePasswordRequest](#changepasswordrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/topics/{id}`

#### `GET` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseTopicResponse](#apiresponsetopicresponse) |

---

#### `PUT` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [TopicRequest](#topicrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseTopicResponse](#apiresponsetopicresponse) |

---

#### `DELETE` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/sizes/{id}`

#### `GET` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseSizeResponse](#apiresponsesizeresponse) |

---

#### `PUT` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [SizeRequest](#sizerequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseSizeResponse](#apiresponsesizeresponse) |

---

#### `DELETE` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/products/{productId}/images/reorder`

#### `PUT` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `productId` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> Array of integer

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/products/{id}`

#### `GET` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseProductDetailResponse](#apiresponseproductdetailresponse) |

---

#### `PUT` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [ProductRequest](#productrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseProductDetailResponse](#apiresponseproductdetailresponse) |

---

#### `DELETE` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/products/variants/{variantId}`

#### `PUT` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `variantId` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [ProductVariantRequest](#productvariantrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseProductVariantResponse](#apiresponseproductvariantresponse) |

---

#### `DELETE` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `variantId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/products/variants/{variantId}/inventory`

#### `PUT` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `variantId` | path | True | integer |  |
| `quantity` | query | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/payment-method/{id}`

#### `PUT` 
**Tags:** admin-payment-method-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |
| `status` | query | True | boolean |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

#### `DELETE` 
**Tags:** admin-payment-method-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/order/update/{orderId}`

#### `PUT` 
**Tags:** admin-order-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `orderId` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [UpdateOrderStatusRequest](#updateorderstatusrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseOrderResponse](#apiresponseorderresponse) |

---

### `/api/v1/admin/order/mark-delivered/{orderId}`

#### `PUT` 
**Tags:** admin-order-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `orderId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseOrderResponse](#apiresponseorderresponse) |

---

### `/api/v1/admin/order/bulk-update`

#### `PUT` 
**Tags:** admin-order-controller

**Request Body:**

- Content-Type: `application/json` -> [BulkUpdateOrderRequest](#bulkupdateorderrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/colors/{id}`

#### `GET` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseColorResponse](#apiresponsecolorresponse) |

---

#### `PUT` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [ColorRequest](#colorrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseColorResponse](#apiresponsecolorresponse) |

---

#### `DELETE` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/collections/{id}`

#### `GET` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseCollectionResponse](#apiresponsecollectionresponse) |

---

#### `PUT` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [CollectionRequest](#collectionrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseCollectionResponse](#apiresponsecollectionresponse) |

---

#### `DELETE` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/categories/{id}`

#### `GET` 
**Tags:** admin-category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseCategoryResponse](#apiresponsecategoryresponse) |

---

#### `PUT` 
**Tags:** admin-category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [CategoryRequest](#categoryrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseCategoryResponse](#apiresponsecategoryresponse) |

---

#### `DELETE` 
**Tags:** admin-category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/categories/subcategories/{id}`

#### `GET` 
**Tags:** admin-category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseSubCategoryResponse](#apiresponsesubcategoryresponse) |

---

#### `PUT` 
**Tags:** admin-category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [SubCategoryRequest](#subcategoryrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseSubCategoryResponse](#apiresponsesubcategoryresponse) |

---

#### `DELETE` 
**Tags:** admin-category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/brands/{id}`

#### `GET` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseBrandResponse](#apiresponsebrandresponse) |

---

#### `PUT` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [BrandRequest](#brandrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseBrandResponse](#apiresponsebrandresponse) |

---

#### `DELETE` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/aip/v1/admin/discount/update/{id}`

#### `PUT` 
**Tags:** admin-discount-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [DiscountRequest](#discountrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/payment/payos_transfer_handler`

#### `POST` 
**Tags:** payment-controller

**Request Body:**

- Content-Type: `application/json` -> object

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | object |

---

### `/api/v1/wishlist/add-product`

#### `POST` 
**Tags:** wish-list-controller

**Request Body:**

- Content-Type: `application/json` -> [WishListRequest](#wishlistrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/review/add`

#### `POST` 
**Tags:** review-controller

**Request Body:**

- Content-Type: `application/json` -> [ReviewRequest](#reviewrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseReviewResponse](#apiresponsereviewresponse) |

---

### `/api/v1/order/re-order/{orderId}`

#### `POST` 
**Tags:** order-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `orderId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseOrderResponse](#apiresponseorderresponse) |

---

### `/api/v1/order/create`

#### `POST` 
**Tags:** order-controller

**Request Body:**

- Content-Type: `application/json` -> [OrderRequest](#orderrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseOrderResponse](#apiresponseorderresponse) |

---

### `/api/v1/order/create-selected`

#### `POST` 
**Tags:** order-controller

**Request Body:**

- Content-Type: `application/json` -> [CreateSelectedOrderRequest](#createselectedorderrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseOrderResponse](#apiresponseorderresponse) |

---

### `/api/v1/order/cancel/{orderId}`

#### `POST` 
**Tags:** order-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `orderId` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [CancelOrderRequest](#cancelorderrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/order/buy-now`

#### `POST` 
**Tags:** order-controller

**Request Body:**

- Content-Type: `application/json` -> [BuyNowRequest](#buynowrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseOrderResponse](#apiresponseorderresponse) |

---

### `/api/v1/cart/add-product`

#### `POST` 
**Tags:** cart-controller

**Request Body:**

- Content-Type: `application/json` -> [CartRequest](#cartrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseCartResponse](#apiresponsecartresponse) |

---

### `/api/v1/auth/verify-forgot-password`

#### `POST` 
**Tags:** user-controller

**Request Body:**

- Content-Type: `application/json` -> [VerifyAccountRequest](#verifyaccountrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseMapStringString](#apiresponsemapstringstring) |

---

### `/api/v1/auth/verify-account`

#### `POST` 
**Tags:** user-controller

**Request Body:**

- Content-Type: `application/json` -> [VerifyAccountRequest](#verifyaccountrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/auth/signup`

#### `POST` 
**Tags:** user-controller

**Request Body:**

- Content-Type: `application/json` -> [UserRegisterRequest](#userregisterrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/auth/set-password/{token}`

#### `POST` 
**Tags:** user-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `token` | path | True | string |  |

**Request Body:**

- Content-Type: `application/json` -> [SetPasswordRequest](#setpasswordrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/auth/regenerate-otp`

#### `POST` 
**Tags:** user-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `email` | query | True | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/auth/refresh-token`

#### `POST` 
**Tags:** user-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `refreshToken` | query | True | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseTokenResponse](#apiresponsetokenresponse) |

---

### `/api/v1/auth/logout`

#### `POST` 
**Tags:** user-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `refreshToken` | query | True | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/auth/login`

#### `POST` 
**Tags:** user-controller

**Request Body:**

- Content-Type: `application/json` -> [UserLoginRequest](#userloginrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseTokenResponse](#apiresponsetokenresponse) |

---

### `/api/v1/auth/forgot-password`

#### `POST` 
**Tags:** user-controller

**Request Body:**

- Content-Type: `application/json` -> [ForgotPasswordRequest](#forgotpasswordrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/topics`

#### `GET` 
**Tags:** admin-attribute-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListTopicResponse](#apiresponselisttopicresponse) |

---

#### `POST` 
**Tags:** admin-attribute-controller

**Request Body:**

- Content-Type: `application/json` -> [TopicRequest](#topicrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseTopicResponse](#apiresponsetopicresponse) |

---

### `/api/v1/admin/sizes`

#### `GET` 
**Tags:** admin-attribute-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `type` | query | False | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListSizeResponse](#apiresponselistsizeresponse) |

---

#### `POST` 
**Tags:** admin-attribute-controller

**Request Body:**

- Content-Type: `application/json` -> [SizeRequest](#sizerequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseSizeResponse](#apiresponsesizeresponse) |

---

### `/api/v1/admin/products`

#### `GET` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `filter` | query | True | [ProductFilterRequest](#productfilterrequest) |  |
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageProductListResponse](#apiresponsepageproductlistresponse) |

---

#### `POST` 
**Tags:** admin-product-controller

**Request Body:**

- Content-Type: `application/json` -> [ProductRequest](#productrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseProductDetailResponse](#apiresponseproductdetailresponse) |

---

### `/api/v1/admin/products/{productId}/variants`

#### `GET` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `productId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListProductVariantResponse](#apiresponselistproductvariantresponse) |

---

#### `POST` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `productId` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [ProductVariantRequest](#productvariantrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseProductVariantResponse](#apiresponseproductvariantresponse) |

---

### `/api/v1/admin/products/{productId}/images`

#### `POST` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `productId` | path | True | integer |  |
| `colorId` | query | False | integer |  |
| `displayOrder` | query | False | integer |  |
| `isPrimary` | query | False | boolean |  |

**Request Body:**

- Content-Type: `application/json` -> object

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseProductImageResponse](#apiresponseproductimageresponse) |

---

### `/api/v1/admin/products/{productId}/images/url`

#### `POST` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `productId` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [ProductImageRequest](#productimagerequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseProductImageResponse](#apiresponseproductimageresponse) |

---

### `/api/v1/admin/products/variants/{variantId}/inventory/adjust`

#### `POST` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `variantId` | path | True | integer |  |
| `quantity` | query | True | integer |  |
| `reason` | query | True | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/payment-method/add`

#### `POST` 
**Tags:** admin-payment-method-controller

**Request Body:**

- Content-Type: `application/json` -> [PaymentMethodRequest](#paymentmethodrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/colors`

#### `GET` 
**Tags:** admin-attribute-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListColorResponse](#apiresponselistcolorresponse) |

---

#### `POST` 
**Tags:** admin-attribute-controller

**Request Body:**

- Content-Type: `application/json` -> [ColorRequest](#colorrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseColorResponse](#apiresponsecolorresponse) |

---

### `/api/v1/admin/collections`

#### `GET` 
**Tags:** admin-attribute-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListCollectionResponse](#apiresponselistcollectionresponse) |

---

#### `POST` 
**Tags:** admin-attribute-controller

**Request Body:**

- Content-Type: `application/json` -> [CollectionRequest](#collectionrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseCollectionResponse](#apiresponsecollectionresponse) |

---

### `/api/v1/admin/categories`

#### `GET` 
**Tags:** admin-category-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListCategoryResponse](#apiresponselistcategoryresponse) |

---

#### `POST` 
**Tags:** admin-category-controller

**Request Body:**

- Content-Type: `application/json` -> [CategoryRequest](#categoryrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseCategoryResponse](#apiresponsecategoryresponse) |

---

### `/api/v1/admin/categories/{categoryId}/subcategories`

#### `GET` 
**Tags:** admin-category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `categoryId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListSubCategoryResponse](#apiresponselistsubcategoryresponse) |

---

#### `POST` 
**Tags:** admin-category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `categoryId` | path | True | integer |  |

**Request Body:**

- Content-Type: `application/json` -> [SubCategoryRequest](#subcategoryrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseSubCategoryResponse](#apiresponsesubcategoryresponse) |

---

### `/api/v1/admin/categories/subcategories/reorder`

#### `POST` 
**Tags:** admin-category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `parentId` | query | False | integer |  |

**Request Body:**

- Content-Type: `application/json` -> Array of integer

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/categories/reorder`

#### `POST` 
**Tags:** admin-category-controller

**Request Body:**

- Content-Type: `application/json` -> Array of integer

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/brands`

#### `GET` 
**Tags:** admin-attribute-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListBrandResponse](#apiresponselistbrandresponse) |

---

#### `POST` 
**Tags:** admin-attribute-controller

**Request Body:**

- Content-Type: `application/json` -> [BrandRequest](#brandrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseBrandResponse](#apiresponsebrandresponse) |

---

### `/aip/v1/admin/discount/add`

#### `POST` 
**Tags:** admin-discount-controller

**Request Body:**

- Content-Type: `application/json` -> [DiscountRequest](#discountrequest)

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/products/{id}/status`

#### `PATCH` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |
| `isActive` | query | True | boolean |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/products/variants/{variantId}/status`

#### `PATCH` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `variantId` | path | True | integer |  |
| `isActive` | query | True | boolean |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/products/images/{imageId}/primary`

#### `PATCH` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `imageId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/products/bulk/status`

#### `PATCH` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `isActive` | query | True | boolean |  |

**Request Body:**

- Content-Type: `application/json` -> Array of integer

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/wishlist/my-wishlist`

#### `GET` 
**Tags:** wish-list-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseWishListResponse](#apiresponsewishlistresponse) |

---

### `/api/v1/search/products`

#### `GET` 
**Tags:** search-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `q` | query | True | string |  |
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageProductListResponse](#apiresponsepageproductlistresponse) |

---

### `/api/v1/review/all-review/product/{productId}`

#### `GET` 
**Tags:** review-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `productId` | path | True | integer |  |
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageReviewResponse](#apiresponsepagereviewresponse) |

---

### `/api/v1/products`

#### `GET` 
**Tags:** product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `filter` | query | True | [ProductFilterRequest](#productfilterrequest) |  |
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageProductListResponse](#apiresponsepageproductlistresponse) |

---

### `/api/v1/products/{id}`

#### `GET` 
**Tags:** product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseProductDetailResponse](#apiresponseproductdetailresponse) |

---

### `/api/v1/products/{id}/related`

#### `GET` 
**Tags:** product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |
| `limit` | query | False | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListProductListResponse](#apiresponselistproductlistresponse) |

---

### `/api/v1/products/slug/{slug}`

#### `GET` 
**Tags:** product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `slug` | path | True | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseProductDetailResponse](#apiresponseproductdetailresponse) |

---

### `/api/v1/products/similar/{productId}`

#### `GET` 
**Tags:** product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `productId` | path | True | integer |  |
| `page` | query | False | integer |  |
| `size` | query | False | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageProductListResponse](#apiresponsepageproductlistresponse) |

---

### `/api/v1/products/sale`

#### `GET` 
**Tags:** product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageProductListResponse](#apiresponsepageproductlistresponse) |

---

### `/api/v1/products/new`

#### `GET` 
**Tags:** product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageProductListResponse](#apiresponsepageproductlistresponse) |

---

### `/api/v1/products/for-you`

#### `GET` 
**Tags:** product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `limit` | query | False | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListProductListResponse](#apiresponselistproductlistresponse) |

---

### `/api/v1/products/filters`

#### `GET` 
**Tags:** product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `categoryId` | query | False | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseFilterOptionsResponse](#apiresponsefilteroptionsresponse) |

---

### `/api/v1/products/featured`

#### `GET` 
**Tags:** product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageProductListResponse](#apiresponsepageproductlistresponse) |

---

### `/api/v1/products/best-sellers`

#### `GET` 
**Tags:** product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageProductListResponse](#apiresponsepageproductlistresponse) |

---

### `/api/v1/payment-method`

#### `GET` 
**Tags:** payment-method-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListPaymentMethodResponse](#apiresponselistpaymentmethodresponse) |

---

### `/api/v1/order/{orderNumber}`

#### `GET` 
**Tags:** order-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `orderNumber` | path | True | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseOrderResponse](#apiresponseorderresponse) |

---

### `/api/v1/order/{id}`

#### `GET` 
**Tags:** order-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseOrderResponse](#apiresponseorderresponse) |

---

### `/api/v1/order/tracking/{orderNumber}`

#### `GET` 
**Tags:** order-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `orderNumber` | path | True | string |  |
| `email` | query | True | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseOrderResponse](#apiresponseorderresponse) |

---

### `/api/v1/order/order-timeline/{orderId}`

#### `GET` 
**Tags:** order-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `orderId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListObject](#apiresponselistobject) |

---

### `/api/v1/order/my-order`

#### `GET` 
**Tags:** order-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageOrderSummaryResponse](#apiresponsepageordersummaryresponse) |

---

### `/api/v1/order/my-order/{status}`

#### `GET` 
**Tags:** order-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `status` | path | True | string |  |
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageOrderSummaryResponse](#apiresponsepageordersummaryresponse) |

---

### `/api/v1/discount`

#### `GET` 
**Tags:** discount-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `discountType` | query | True | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListDiscountResponse](#apiresponselistdiscountresponse) |

---

### `/api/v1/categories`

#### `GET` 
**Tags:** category-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListCategoryResponse](#apiresponselistcategoryresponse) |

---

### `/api/v1/categories/{id}`

#### `GET` 
**Tags:** category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseCategoryResponse](#apiresponsecategoryresponse) |

---

### `/api/v1/categories/{id}/subcategories`

#### `GET` 
**Tags:** category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListSubCategoryResponse](#apiresponselistsubcategoryresponse) |

---

### `/api/v1/categories/subcategories/{id}`

#### `GET` 
**Tags:** category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseSubCategoryResponse](#apiresponsesubcategoryresponse) |

---

### `/api/v1/categories/subcategories/slug/{slug}`

#### `GET` 
**Tags:** category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `slug` | path | True | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseSubCategoryResponse](#apiresponsesubcategoryresponse) |

---

### `/api/v1/categories/slug/{slug}`

#### `GET` 
**Tags:** category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `slug` | path | True | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseCategoryResponse](#apiresponsecategoryresponse) |

---

### `/api/v1/catalog/topics`

#### `GET` 
**Tags:** catalog-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListTopicResponse](#apiresponselisttopicresponse) |

---

### `/api/v1/catalog/topics/{id}`

#### `GET` 
**Tags:** catalog-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseTopicResponse](#apiresponsetopicresponse) |

---

### `/api/v1/catalog/topics/slug/{slug}`

#### `GET` 
**Tags:** catalog-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `slug` | path | True | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseTopicResponse](#apiresponsetopicresponse) |

---

### `/api/v1/catalog/sizes`

#### `GET` 
**Tags:** catalog-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `type` | query | False | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListSizeResponse](#apiresponselistsizeresponse) |

---

### `/api/v1/catalog/colors`

#### `GET` 
**Tags:** catalog-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListColorResponse](#apiresponselistcolorresponse) |

---

### `/api/v1/catalog/collections`

#### `GET` 
**Tags:** catalog-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListCollectionResponse](#apiresponselistcollectionresponse) |

---

### `/api/v1/catalog/collections/{id}`

#### `GET` 
**Tags:** catalog-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseCollectionResponse](#apiresponsecollectionresponse) |

---

### `/api/v1/catalog/collections/slug/{slug}`

#### `GET` 
**Tags:** catalog-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `slug` | path | True | string |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseCollectionResponse](#apiresponsecollectionresponse) |

---

### `/api/v1/catalog/brands`

#### `GET` 
**Tags:** catalog-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListBrandResponse](#apiresponselistbrandresponse) |

---

### `/api/v1/cart/items`

#### `GET` 
**Tags:** cart-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseCartResponse](#apiresponsecartresponse) |

---

### `/api/v1/auth/users/{userId}`

#### `GET` 
**Tags:** user-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `userId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseUserResponseDTO](#apiresponseuserresponsedto) |

---

#### `DELETE` 
**Tags:** user-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `userId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/auth/users/stats/count`

#### `GET` 
**Tags:** user-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseMapStringInteger](#apiresponsemapstringinteger) |

---

### `/api/v1/auth/users/role/{roleName}`

#### `GET` 
**Tags:** user-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `roleName` | path | True | string |  |
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageUserResponseDTO](#apiresponsepageuserresponsedto) |

---

### `/api/v1/auth/users/blocked`

#### `GET` 
**Tags:** user-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageUserResponseDTO](#apiresponsepageuserresponsedto) |

---

### `/api/v1/auth/users/active`

#### `GET` 
**Tags:** user-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageUserResponseDTO](#apiresponsepageuserresponsedto) |

---

### `/api/v1/auth/test`

#### `GET` 
**Tags:** user-controller

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseString](#apiresponsestring) |

---

### `/api/v1/admin/order`

#### `GET` 
**Tags:** admin-order-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `request` | query | True | [OrderFilterRequest](#orderfilterrequest) |  |
| `pageable` | query | True | [Pageable](#pageable) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponsePageOrderResponse](#apiresponsepageorderresponse) |

---

### `/api/v1/admin/order/{orderId}`

#### `GET` 
**Tags:** admin-order-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `orderId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseOrderResponse](#apiresponseorderresponse) |

---

### `/api/v1/admin/order/export`

#### `GET` 
**Tags:** admin-order-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `request` | query | True | [ExportOrderRequest](#exportorderrequest) |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseByte[]](#apiresponsebyte[]) |

---

### `/api/v1/admin/categories/{categoryId}/subcategories/hierarchy`

#### `GET` 
**Tags:** admin-category-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `categoryId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseListSubCategoryResponse](#apiresponselistsubcategoryresponse) |

---

### `/api/v1/wishlist/product/{id}`

#### `DELETE` 
**Tags:** wish-list-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/review/delete/{id}`

#### `DELETE` 
**Tags:** review-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/cart/item/{cartItemId}`

#### `DELETE` 
**Tags:** cart-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `cartItemId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/products/images/{imageId}`

#### `DELETE` 
**Tags:** admin-product-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `imageId` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/api/v1/admin/products/bulk`

#### `DELETE` 
**Tags:** admin-product-controller

**Request Body:**

- Content-Type: `application/json` -> Array of integer

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

### `/aip/v1/admin/discount/delete/{id}`

#### `DELETE` 
**Tags:** admin-discount-controller

**Parameters:**

| Name | In | Required | Type | Description |
|---|---|---|---|---|
| `id` | path | True | integer |  |

**Responses:**

| Code | Description | Schema |
|---|---|---|
| 200 | OK | [ApiResponseVoid](#apiresponsevoid) |

---

## Schemas (Models)

### ReviewRequest
| Property | Type | Ref |
|---|---|---|
| `productId` | integer |  |
| `rating` | integer |  |
| `title` | string |  |
| `comment` | string |  |

### ApiResponseReviewResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [ReviewResponse](#reviewresponse) |
| `errors` | object |  |

### ReviewResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `rating` | integer |  |
| `title` | string |  |
| `comment` | string |  |
| `createAt` | string |  |
| `updateAt` | string |  |
| `user` |  | [UserResponseDTO](#userresponsedto) |

### UserResponseDTO
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `email` | string |  |
| `fullName` | string |  |
| `phone` | string |  |
| `isActive` | boolean |  |
| `emailVerified` | boolean |  |
| `provider` | string |  |
| `roles` | Array of Array of string |  |
| `createdAt` | string |  |
| `updatedAt` | string |  |

### ApiResponseVoid
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | object |  |
| `errors` | object |  |

### UserUpdateRequest
| Property | Type | Ref |
|---|---|---|
| `fullName` | string |  |
| `phone` | string |  |

### ChangePasswordRequest
| Property | Type | Ref |
|---|---|---|
| `oldPassword` | string |  |
| `newPassword` | string |  |
| `confirmPassword` | string |  |

### TopicRequest
| Property | Type | Ref |
|---|---|---|
| `name` | string |  |
| `description` | string |  |
| `isActive` | boolean |  |

### ApiResponseTopicResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [TopicResponse](#topicresponse) |
| `errors` | object |  |

### TopicResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `name` | string |  |
| `slug` | string |  |
| `description` | string |  |
| `isActive` | boolean |  |
| `createdAt` | string |  |

### SizeRequest
| Property | Type | Ref |
|---|---|---|
| `name` | string |  |
| `categoryType` | string |  |
| `displayOrder` | integer |  |

### ApiResponseSizeResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [SizeResponse](#sizeresponse) |
| `errors` | object |  |

### SizeResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `name` | string |  |
| `categoryType` | string |  |
| `displayOrder` | integer |  |

### ProductImageRequest
| Property | Type | Ref |
|---|---|---|
| `imageUrl` | string |  |
| `colorId` | integer |  |
| `displayOrder` | integer |  |
| `isPrimary` | boolean |  |

### ProductRequest
| Property | Type | Ref |
|---|---|---|
| `subCategoryId` | integer |  |
| `brandId` | integer |  |
| `name` | string |  |
| `description` | string |  |
| `basePrice` | number |  |
| `salePrice` | number |  |
| `gender` | string |  |
| `isFeatured` | boolean |  |
| `isNew` | boolean |  |
| `isActive` | boolean |  |
| `metaTitle` | string |  |
| `metaDescription` | string |  |
| `metaKeywords` | string |  |
| `collectionIds` | Array of Array of integer |  |
| `topicIds` | Array of Array of integer |  |
| `variants` | Array of Array of [ProductVariantRequest](#productvariantrequest) | [ProductVariantRequest](#productvariantrequest) |
| `images` | Array of Array of [ProductImageRequest](#productimagerequest) | [ProductImageRequest](#productimagerequest) |

### ProductVariantRequest
| Property | Type | Ref |
|---|---|---|
| `colorId` | integer |  |
| `sizeId` | integer |  |
| `additionalPrice` | number |  |
| `quantity` | integer |  |
| `isActive` | boolean |  |

### ApiResponseProductDetailResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [ProductDetailResponse](#productdetailresponse) |
| `errors` | object |  |

### BrandResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `name` | string |  |
| `description` | string |  |
| `isActive` | boolean |  |
| `createdAt` | string |  |

### CollectionResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `name` | string |  |
| `slug` | string |  |
| `description` | string |  |
| `imageUrl` | string |  |
| `displayOrder` | integer |  |
| `isActive` | boolean |  |
| `subCategoryIds` | Array of Array of integer |  |
| `createdAt` | string |  |

### ColorResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `name` | string |  |
| `hexCode` | string |  |

### ProductDetailResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `name` | string |  |
| `slug` | string |  |
| `description` | string |  |
| `subCategory` |  | [SubCategoryResponse](#subcategoryresponse) |
| `brand` |  | [BrandResponse](#brandresponse) |
| `basePrice` | number |  |
| `salePrice` | number |  |
| `currentPrice` | number |  |
| `discountPercentage` | number |  |
| `isOnSale` | boolean |  |
| `gender` | string |  |
| `isFeatured` | boolean |  |
| `isNew` | boolean |  |
| `isActive` | boolean |  |
| `metaTitle` | string |  |
| `metaDescription` | string |  |
| `metaKeywords` | string |  |
| `averageRating` | number |  |
| `reviewCount` | integer |  |
| `totalSold` | integer |  |
| `viewCount` | integer |  |
| `collections` | Array of Array of [CollectionResponse](#collectionresponse) | [CollectionResponse](#collectionresponse) |
| `topics` | Array of Array of [TopicResponse](#topicresponse) | [TopicResponse](#topicresponse) |
| `images` | Array of Array of [ProductImageResponse](#productimageresponse) | [ProductImageResponse](#productimageresponse) |
| `variants` | Array of Array of [ProductVariantResponse](#productvariantresponse) | [ProductVariantResponse](#productvariantresponse) |
| `createdAt` | string |  |
| `updatedAt` | string |  |

### ProductImageResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `imageUrl` | string |  |
| `colorId` | integer |  |
| `colorName` | string |  |
| `displayOrder` | integer |  |
| `isPrimary` | boolean |  |

### ProductVariantResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `color` |  | [ColorResponse](#colorresponse) |
| `size` |  | [SizeResponse](#sizeresponse) |
| `additionalPrice` | number |  |
| `finalPrice` | number |  |
| `isActive` | boolean |  |
| `quantity` | integer |  |
| `reservedQuantity` | integer |  |
| `inStock` | boolean |  |

### SubCategoryResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `categoryId` | integer |  |
| `categoryName` | string |  |
| `parentId` | integer |  |
| `name` | string |  |
| `slug` | string |  |
| `description` | string |  |
| `imageUrl` | string |  |
| `level` | integer |  |
| `displayOrder` | integer |  |
| `isActive` | boolean |  |
| `children` | Array of Array of [SubCategoryResponse](#subcategoryresponse) | [SubCategoryResponse](#subcategoryresponse) |
| `createdAt` | string |  |

### ApiResponseProductVariantResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [ProductVariantResponse](#productvariantresponse) |
| `errors` | object |  |

### UpdateOrderStatusRequest
| Property | Type | Ref |
|---|---|---|
| `status` | string |  |

### ApiResponseOrderResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [OrderResponse](#orderresponse) |
| `errors` | object |  |

### OrderItemResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `variant` |  | [ProductVariantResponse](#productvariantresponse) |
| `quantity` | integer |  |
| `unitPrice` | number |  |
| `totalPrice` | number |  |

### OrderResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `orderNumber` | string |  |
| `user` |  | [UserResponseDTO](#userresponsedto) |
| `totalAmount` | number |  |
| `shippingFee` | number |  |
| `discountAmount` | number |  |
| `finalAmount` | number |  |
| `status` | string |  |
| `phoneDelivery` | string |  |
| `province` | string |  |
| `district` | string |  |
| `ward` | string |  |
| `addressDetail` | string |  |
| `note` | string |  |
| `orderItems` | Array of Array of [OrderItemResponse](#orderitemresponse) | [OrderItemResponse](#orderitemresponse) |
| `payments` | Array of Array of [PaymentResponse](#paymentresponse) | [PaymentResponse](#paymentresponse) |
| `createdAt` | string |  |
| `updatedAt` | string |  |
| `paymentLink` | object |  |

### PaymentMethodResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `name` | string |  |
| `description` | string |  |
| `createAt` | string |  |

### PaymentResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `amount` | number |  |
| `transactionId` | string |  |
| `status` | string |  |
| `paidAt` | string |  |
| `createdAt` | string |  |
| `paymentMethod` |  | [PaymentMethodResponse](#paymentmethodresponse) |

### BulkUpdateOrderRequest
| Property | Type | Ref |
|---|---|---|
| `orderIds` | Array of Array of integer |  |
| `status` | string |  |

### ColorRequest
| Property | Type | Ref |
|---|---|---|
| `name` | string |  |
| `hexCode` | string |  |

### ApiResponseColorResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [ColorResponse](#colorresponse) |
| `errors` | object |  |

### CollectionRequest
| Property | Type | Ref |
|---|---|---|
| `name` | string |  |
| `description` | string |  |
| `isActive` | boolean |  |
| `subCategoryIds` | Array of Array of integer |  |

### ApiResponseCollectionResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [CollectionResponse](#collectionresponse) |
| `errors` | object |  |

### CategoryRequest
| Property | Type | Ref |
|---|---|---|
| `name` | string |  |
| `description` | string |  |
| `displayOrder` | integer |  |
| `isActive` | boolean |  |

### ApiResponseCategoryResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [CategoryResponse](#categoryresponse) |
| `errors` | object |  |

### CategoryResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `name` | string |  |
| `slug` | string |  |
| `description` | string |  |
| `displayOrder` | integer |  |
| `isActive` | boolean |  |
| `subCategories` | Array of Array of [SubCategoryResponse](#subcategoryresponse) | [SubCategoryResponse](#subcategoryresponse) |
| `createdAt` | string |  |

### SubCategoryRequest
| Property | Type | Ref |
|---|---|---|
| `categoryId` | integer |  |
| `parentId` | integer |  |
| `name` | string |  |
| `description` | string |  |
| `displayOrder` | integer |  |
| `isActive` | boolean |  |

### ApiResponseSubCategoryResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [SubCategoryResponse](#subcategoryresponse) |
| `errors` | object |  |

### BrandRequest
| Property | Type | Ref |
|---|---|---|
| `name` | string |  |
| `description` | string |  |
| `isActive` | boolean |  |

### ApiResponseBrandResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [BrandResponse](#brandresponse) |
| `errors` | object |  |

### DiscountRequest
| Property | Type | Ref |
|---|---|---|
| `name` | string |  |
| `percent` | integer |  |
| `reduceAmount` | number |  |
| `discountType` | string |  |
| `description` | string |  |
| `usageLimit` | integer |  |
| `userUsageLimit` | integer |  |
| `minOrderAmount` | number |  |
| `startDate` | string |  |
| `endDate` | string |  |
| `active` | boolean |  |

### WishListRequest
| Property | Type | Ref |
|---|---|---|
| `productId` | integer |  |

### OrderRequest
| Property | Type | Ref |
|---|---|---|
| `phoneDelivery` | string |  |
| `province` | string |  |
| `district` | string |  |
| `ward` | string |  |
| `addressDetail` | string |  |
| `note` | string |  |
| `paymentMethodId` | integer |  |
| `discountCode` | string |  |

### CreateSelectedOrderRequest
| Property | Type | Ref |
|---|---|---|
| `orderRequest` |  | [OrderRequest](#orderrequest) |
| `orderItemRequests` | Array of Array of [OrderItemRequest](#orderitemrequest) | [OrderItemRequest](#orderitemrequest) |

### OrderItemRequest
| Property | Type | Ref |
|---|---|---|
| `variantId` | integer |  |
| `quantity` | integer |  |

### CancelOrderRequest
| Property | Type | Ref |
|---|---|---|
| `reason` | string |  |

### BuyNowRequest
| Property | Type | Ref |
|---|---|---|
| `orderRequest` |  | [OrderRequest](#orderrequest) |
| `orderItemRequest` |  | [OrderItemRequest](#orderitemrequest) |

### CartRequest
| Property | Type | Ref |
|---|---|---|
| `variantId` | integer |  |
| `quantity` | integer |  |

### ApiResponseCartResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [CartResponse](#cartresponse) |
| `errors` | object |  |

### CartItemResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `variant` |  | [ProductVariantResponse](#productvariantresponse) |
| `quantity` | integer |  |

### CartResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `user` |  | [UserResponseDTO](#userresponsedto) |
| `cartItems` | Array of Array of [CartItemResponse](#cartitemresponse) | [CartItemResponse](#cartitemresponse) |

### VerifyAccountRequest
| Property | Type | Ref |
|---|---|---|
| `email` | string |  |
| `otp` | string |  |

### ApiResponseMapStringString
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | object |  |
| `errors` | object |  |

### UserRegisterRequest
| Property | Type | Ref |
|---|---|---|
| `email` | string |  |
| `fullName` | string |  |
| `phone` | string |  |
| `password` | string |  |
| `confirmPassword` | string |  |
| `provider` | string |  |

### SetPasswordRequest
| Property | Type | Ref |
|---|---|---|
| `newPassword` | string |  |
| `confirmPassword` | string |  |

### ApiResponseTokenResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [TokenResponse](#tokenresponse) |
| `errors` | object |  |

### TokenResponse
| Property | Type | Ref |
|---|---|---|
| `accessToken` | string |  |
| `refreshToken` | string |  |
| `tokenType` | string |  |
| `expiresIn` | integer |  |
| `user` |  | [UserResponseDTO](#userresponsedto) |

### UserLoginRequest
| Property | Type | Ref |
|---|---|---|
| `email` | string |  |
| `password` | string |  |

### ForgotPasswordRequest
| Property | Type | Ref |
|---|---|---|
| `email` | string |  |

### ApiResponseProductImageResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [ProductImageResponse](#productimageresponse) |
| `errors` | object |  |

### PaymentMethodRequest
| Property | Type | Ref |
|---|---|---|
| `name` | string |  |
| `description` | string |  |

### ApiResponseWishListResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [WishListResponse](#wishlistresponse) |
| `errors` | object |  |

### ProductListResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `name` | string |  |
| `slug` | string |  |
| `categoryName` | string |  |
| `subCategoryName` | string |  |
| `brandName` | string |  |
| `basePrice` | number |  |
| `salePrice` | number |  |
| `currentPrice` | number |  |
| `discountPercentage` | number |  |
| `isOnSale` | boolean |  |
| `gender` | string |  |
| `isFeatured` | boolean |  |
| `isNew` | boolean |  |
| `primaryImage` | string |  |
| `averageRating` | number |  |
| `reviewCount` | integer |  |
| `totalSold` | integer |  |
| `inStock` | boolean |  |
| `availableColors` | Array of Array of string |  |

### WishListResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `user` |  | [UserResponseDTO](#userresponsedto) |
| `createdAt` | string |  |
| `product` | Array of Array of [ProductListResponse](#productlistresponse) | [ProductListResponse](#productlistresponse) |

### Pageable
| Property | Type | Ref |
|---|---|---|
| `page` | integer |  |
| `size` | integer |  |
| `sort` | Array of Array of string |  |

### ApiResponsePageProductListResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [PageProductListResponse](#pageproductlistresponse) |
| `errors` | object |  |

### PageProductListResponse
| Property | Type | Ref |
|---|---|---|
| `totalPages` | integer |  |
| `totalElements` | integer |  |
| `pageable` |  | [PageableObject](#pageableobject) |
| `size` | integer |  |
| `content` | Array of Array of [ProductListResponse](#productlistresponse) | [ProductListResponse](#productlistresponse) |
| `number` | integer |  |
| `sort` |  | [SortObject](#sortobject) |
| `first` | boolean |  |
| `last` | boolean |  |
| `numberOfElements` | integer |  |
| `empty` | boolean |  |

### PageableObject
| Property | Type | Ref |
|---|---|---|
| `paged` | boolean |  |
| `pageNumber` | integer |  |
| `pageSize` | integer |  |
| `offset` | integer |  |
| `sort` |  | [SortObject](#sortobject) |
| `unpaged` | boolean |  |

### SortObject
| Property | Type | Ref |
|---|---|---|
| `sorted` | boolean |  |
| `empty` | boolean |  |
| `unsorted` | boolean |  |

### ApiResponsePageReviewResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [PageReviewResponse](#pagereviewresponse) |
| `errors` | object |  |

### PageReviewResponse
| Property | Type | Ref |
|---|---|---|
| `totalPages` | integer |  |
| `totalElements` | integer |  |
| `pageable` |  | [PageableObject](#pageableobject) |
| `size` | integer |  |
| `content` | Array of Array of [ReviewResponse](#reviewresponse) | [ReviewResponse](#reviewresponse) |
| `number` | integer |  |
| `sort` |  | [SortObject](#sortobject) |
| `first` | boolean |  |
| `last` | boolean |  |
| `numberOfElements` | integer |  |
| `empty` | boolean |  |

### ProductFilterRequest
| Property | Type | Ref |
|---|---|---|
| `categoryId` | integer |  |
| `subCategoryId` | integer |  |
| `brandId` | integer |  |
| `collectionId` | integer |  |
| `topicId` | integer |  |
| `gender` | string |  |
| `minPrice` | number |  |
| `maxPrice` | number |  |
| `colorIds` | Array of Array of integer |  |
| `sizeIds` | Array of Array of integer |  |
| `isFeatured` | boolean |  |
| `isNew` | boolean |  |
| `isOnSale` | boolean |  |
| `inStock` | boolean |  |
| `keyword` | string |  |
| `sortBy` | string |  |
| `sortDirection` | string |  |

### ApiResponseListProductListResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | Array of Array of [ProductListResponse](#productlistresponse) | [ProductListResponse](#productlistresponse) |
| `errors` | object |  |

### ApiResponseFilterOptionsResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [FilterOptionsResponse](#filteroptionsresponse) |
| `errors` | object |  |

### FilterOptionsResponse
| Property | Type | Ref |
|---|---|---|
| `categories` | Array of Array of [CategoryResponse](#categoryresponse) | [CategoryResponse](#categoryresponse) |
| `brands` | Array of Array of [BrandResponse](#brandresponse) | [BrandResponse](#brandresponse) |
| `collections` | Array of Array of [CollectionResponse](#collectionresponse) | [CollectionResponse](#collectionresponse) |
| `topics` | Array of Array of [TopicResponse](#topicresponse) | [TopicResponse](#topicresponse) |
| `colors` | Array of Array of [ColorResponse](#colorresponse) | [ColorResponse](#colorresponse) |
| `sizes` | Array of Array of [SizeResponse](#sizeresponse) | [SizeResponse](#sizeresponse) |
| `minPrice` | number |  |
| `maxPrice` | number |  |

### ApiResponseListPaymentMethodResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | Array of Array of [PaymentMethodResponse](#paymentmethodresponse) | [PaymentMethodResponse](#paymentmethodresponse) |
| `errors` | object |  |

### ApiResponseListObject
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | Array of Array of object |  |
| `errors` | object |  |

### ApiResponsePageOrderSummaryResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [PageOrderSummaryResponse](#pageordersummaryresponse) |
| `errors` | object |  |

### OrderSummaryResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `orderNumber` | string |  |
| `finalAmount` | number |  |
| `totalItems` | integer |  |
| `createdAt` | string |  |

### PageOrderSummaryResponse
| Property | Type | Ref |
|---|---|---|
| `totalPages` | integer |  |
| `totalElements` | integer |  |
| `pageable` |  | [PageableObject](#pageableobject) |
| `size` | integer |  |
| `content` | Array of Array of [OrderSummaryResponse](#ordersummaryresponse) | [OrderSummaryResponse](#ordersummaryresponse) |
| `number` | integer |  |
| `sort` |  | [SortObject](#sortobject) |
| `first` | boolean |  |
| `last` | boolean |  |
| `numberOfElements` | integer |  |
| `empty` | boolean |  |

### ApiResponseListDiscountResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | Array of Array of [DiscountResponse](#discountresponse) | [DiscountResponse](#discountresponse) |
| `errors` | object |  |

### DiscountResponse
| Property | Type | Ref |
|---|---|---|
| `id` | integer |  |
| `name` | string |  |
| `description` | string |  |
| `discountType` | string |  |
| `percent` | integer |  |
| `reduceAmount` | number |  |
| `usageLimit` | integer |  |
| `usedCount` | integer |  |
| `userUsageLimit` | integer |  |
| `minOrderAmount` | number |  |
| `startDate` | string |  |
| `endDate` | string |  |
| `createAt` | string |  |

### ApiResponseListCategoryResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | Array of Array of [CategoryResponse](#categoryresponse) | [CategoryResponse](#categoryresponse) |
| `errors` | object |  |

### ApiResponseListSubCategoryResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | Array of Array of [SubCategoryResponse](#subcategoryresponse) | [SubCategoryResponse](#subcategoryresponse) |
| `errors` | object |  |

### ApiResponseListTopicResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | Array of Array of [TopicResponse](#topicresponse) | [TopicResponse](#topicresponse) |
| `errors` | object |  |

### ApiResponseListSizeResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | Array of Array of [SizeResponse](#sizeresponse) | [SizeResponse](#sizeresponse) |
| `errors` | object |  |

### ApiResponseListColorResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | Array of Array of [ColorResponse](#colorresponse) | [ColorResponse](#colorresponse) |
| `errors` | object |  |

### ApiResponseListCollectionResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | Array of Array of [CollectionResponse](#collectionresponse) | [CollectionResponse](#collectionresponse) |
| `errors` | object |  |

### ApiResponseListBrandResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | Array of Array of [BrandResponse](#brandresponse) | [BrandResponse](#brandresponse) |
| `errors` | object |  |

### ApiResponseUserResponseDTO
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [UserResponseDTO](#userresponsedto) |
| `errors` | object |  |

### ApiResponseMapStringInteger
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | object |  |
| `errors` | object |  |

### ApiResponsePageUserResponseDTO
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [PageUserResponseDTO](#pageuserresponsedto) |
| `errors` | object |  |

### PageUserResponseDTO
| Property | Type | Ref |
|---|---|---|
| `totalPages` | integer |  |
| `totalElements` | integer |  |
| `pageable` |  | [PageableObject](#pageableobject) |
| `size` | integer |  |
| `content` | Array of Array of [UserResponseDTO](#userresponsedto) | [UserResponseDTO](#userresponsedto) |
| `number` | integer |  |
| `sort` |  | [SortObject](#sortobject) |
| `first` | boolean |  |
| `last` | boolean |  |
| `numberOfElements` | integer |  |
| `empty` | boolean |  |

### ApiResponseString
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | string |  |
| `errors` | object |  |

### ApiResponseListProductVariantResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | Array of Array of [ProductVariantResponse](#productvariantresponse) | [ProductVariantResponse](#productvariantresponse) |
| `errors` | object |  |

### OrderFilterRequest
| Property | Type | Ref |
|---|---|---|
| `keyword` | string |  |
| `userId` | integer |  |
| `minAmount` | number |  |
| `maxAmount` | number |  |
| `startDate` | string |  |
| `endDate` | string |  |
| `district` | string |  |
| `ward` | string |  |

### ApiResponsePageOrderResponse
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` |  | [PageOrderResponse](#pageorderresponse) |
| `errors` | object |  |

### PageOrderResponse
| Property | Type | Ref |
|---|---|---|
| `totalPages` | integer |  |
| `totalElements` | integer |  |
| `pageable` |  | [PageableObject](#pageableobject) |
| `size` | integer |  |
| `content` | Array of Array of [OrderResponse](#orderresponse) | [OrderResponse](#orderresponse) |
| `number` | integer |  |
| `sort` |  | [SortObject](#sortobject) |
| `first` | boolean |  |
| `last` | boolean |  |
| `numberOfElements` | integer |  |
| `empty` | boolean |  |

### ExportOrderRequest
| Property | Type | Ref |
|---|---|---|
| `orderFilter` |  | [OrderFilterRequest](#orderfilterrequest) |
| `format` | string |  |

### ApiResponseByte[]
| Property | Type | Ref |
|---|---|---|
| `timestamp` | string |  |
| `code` | integer |  |
| `status` | string |  |
| `message` | string |  |
| `data` | string |  |
| `errors` | object |  |
