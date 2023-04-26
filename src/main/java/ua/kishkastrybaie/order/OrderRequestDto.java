package ua.kishkastrybaie.order;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import ua.kishkastrybaie.order.item.OrderItemRequestDto;
import ua.kishkastrybaie.order.payment.type.PaymentType;
import ua.kishkastrybaie.order.shipping.method.ShippingMethod;
import ua.kishkastrybaie.validation.phonenumber.PhoneNumber;

public record OrderRequestDto(
    @PhoneNumber String phoneNumber,
    @NotNull @Email String email,
    @NotNull @Size(min = 3) String customerFullName,
    @NotNull PaymentType paymentType,
    @NotBlank String address,
    @NotNull ShippingMethod shippingMethod,
    @NotNull Set<OrderItemRequestDto> items) {}
