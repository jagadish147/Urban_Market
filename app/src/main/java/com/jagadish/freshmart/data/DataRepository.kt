package com.jagadish.freshmart.data

import com.jagadish.freshmart.data.dto.address.AddAddressReq
import com.jagadish.freshmart.data.dto.address.AddAddressRes
import com.jagadish.freshmart.data.dto.address.AddressRes
import com.jagadish.freshmart.data.dto.address.GetAddressReq
import com.jagadish.freshmart.data.dto.cart.*
import com.jagadish.freshmart.data.dto.deliver.orders.DeliveryBoyOrders
import com.jagadish.freshmart.data.dto.deliver.orders.UpdateOrderStatus
import com.jagadish.freshmart.data.dto.deliver.orders.UpdateOrderStatusRes
import com.jagadish.freshmart.data.dto.login.CustomersRequest
import com.jagadish.freshmart.data.dto.login.CustomersRes
import com.jagadish.freshmart.data.dto.login.RequestOtpReq
import com.jagadish.freshmart.data.dto.login.RequestOtpRes
import com.jagadish.freshmart.data.dto.order.*
import com.jagadish.freshmart.data.dto.products.Products
import com.jagadish.freshmart.data.dto.shop.Shop
import com.jagadish.freshmart.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Jagadeesh on 01-03-2021.
 */
class DataRepository @Inject constructor(private val remoteRepository: RemoteData, private val ioDispatcher: CoroutineContext) : DataRepositorySource {
    override suspend fun requestCreateCart(fcm_token: CreateCareReq): Flow<Resource<CreateCartRes>> {
        return flow {
            emit(remoteRepository.requestCreateCart(fcm_token))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestRecipes(pinCode:String): Flow<Resource<Shop>> {
        return flow {
            emit(remoteRepository.requestRecipes(pinCode))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestProducts(categoryId: Int): Flow<Resource<Products>> {
        return flow {
            emit(remoteRepository.requestProducts(categoryId))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestCart(cartId: Int): Flow<Resource<Cart>> {
        return flow {
            emit(remoteRepository.requestCart(cartId))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestAddItem(addItemReq: AddItemReq): Flow<Resource<AddItemRes>> {
        return flow {
            emit(remoteRepository.requestAddItem(addItemReq))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestRemoveItem(addItemReq: AddItemReq): Flow<Resource<AddItemRes>> {
        return flow {
            emit(remoteRepository.requestRemoveItem(addItemReq))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestOTP(addItemReq: RequestOtpReq): Flow<Resource<RequestOtpRes>> {
        return flow {
            emit(remoteRepository.requestOTP(addItemReq))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestDeliveryBoyLogin(addItemReq: RequestOtpReq): Flow<Resource<RequestOtpRes>> {
        return flow {
            emit(remoteRepository.requestDeliveryBoyLogin(addItemReq))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestDeliveryBoyScheduleOrders(deliveryBoyId: Int): Flow<Resource<DeliveryBoyOrders>> {
        return flow {
            emit(remoteRepository.requestDeliveryBoyScheduleOrders(deliveryBoyId))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestDeliveryBoyAllOrders(
        deliveryBoyId: Int,
        all: Boolean
    ): Flow<Resource<DeliveryBoyOrders>> {
        return flow {
            emit(remoteRepository.requestDeliveryBoyAllOrders(deliveryBoyId,all))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestCustomerOrders(phoneNumber: String): Flow<Resource<OrdersRes>> {
        return flow {
            emit(remoteRepository.requestCustomerOrders(phoneNumber))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestUpdateOrderStatus(updateOrderStatus: UpdateOrderStatus): Flow<Resource<UpdateOrderStatusRes>> {
        return flow {
            emit(remoteRepository.requestUpdateOrderStatus(updateOrderStatus))
        }.flowOn(ioDispatcher)
    }

    override suspend fun createCustomers(customersRes: CustomersRequest): Flow<Resource<CustomersRes>> {
        return flow {
            emit(remoteRepository.createCustomers(customersRes))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestCustomerLogin(customersRes: RequestOtpReq): Flow<Resource<CustomersRes>> {
        return flow {
            emit(remoteRepository.requestCustomerLogin(customersRes))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestAddAddress(
        customerId: Int,
        addAddressReq: AddAddressReq
    ): Flow<Resource<AddAddressRes>> {
        return flow {
            emit(remoteRepository.requestAddAddress(customerId,addAddressReq))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestRemoveAddress(
        customerId: Int,
        addAddressReq: AddAddressReq
    ): Flow<Resource<AddAddressRes>> {
        return flow {
            emit(remoteRepository.requestRemoveAddress(customerId,addAddressReq))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestUpdateAddress(
        customerId: Int,
        addAddressReq: AddAddressReq
    ): Flow<Resource<AddAddressRes>> {
        return flow {
            emit(remoteRepository.requestUpdateAddress(customerId,addAddressReq))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestAddress(customerId: Int,phoneNumber : String): Flow<Resource<AddressRes>> {
        return flow {
            emit(remoteRepository.requestAddress(customerId,phoneNumber))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestOrderId(orderReq: OrderReq): Flow<Resource<OrderRes>> {
        return flow {
            emit(remoteRepository.requestOrderId(orderReq))
        }.flowOn(ioDispatcher)
    }

    override suspend fun requestPaymentStatus(paymentStatusReq: PaymentStatusReq): Flow<Resource<PaymentStatusRes>> {
        return flow {
            emit(remoteRepository.requestPaymentStatus(paymentStatusReq))
        }.flowOn(ioDispatcher)
    }


}