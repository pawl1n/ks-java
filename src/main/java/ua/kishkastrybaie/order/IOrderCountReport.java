package ua.kishkastrybaie.order;

import ua.kishkastrybaie.order.status.OrderStatus;

import java.time.LocalDate;

public interface IOrderCountReport {
  Integer getCount();

  LocalDate getDate();

  OrderStatus getStatus();
}
