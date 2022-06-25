package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 다른곳에서 객체생성 해서 사용하지 못하도록. 아래의 createOrderItem사용 강제
public class Order {

  @Id @GeneratedValue
  @Column(name = "order_id")
  private Long id;

  @ManyToOne(fetch = LAZY) // n+1문제 발생 할 수 있음.
  @JoinColumn(name = "member_id")
  private Member member;

  @BatchSize(size = 1000)
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderItem> orderItems = new ArrayList<>();

  @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "delivery_id")
  private Delivery delivery;

  private LocalDateTime orderDate; // orderTime

  @Enumerated(EnumType.STRING)
  private OrderStatus status; // ORDER, CANCELED

  //==연관관계 (편입) 메서드==//
  public void setMember(Member member) {
    this.member = member;
    member.getOrders().add(this);
  }

  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
    orderItem.setOrder(this);
  }

  public void setDelivery(Delivery delivery) {
    this.delivery = delivery;
    delivery.setOrder(this);
  }

  //==생성 메서드==//
  public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
    Order order = new Order();
    order.setMember(member);
    order.setDelivery(delivery);
    for(OrderItem orderItem : orderItems) {
      order.addOrderItem(orderItem);
    }
    order.setStatus(OrderStatus.ORDER);
    order.setOrderDate(LocalDateTime.now());

    return order;
  }

  //==비즈니스 로직==//
  /**
   * 주문취소
   */
  public void cancel() {
    if (delivery.getStatus() == DeliveryStatus.COMP) {
      throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능 합니다.");
    }

    this.setStatus(OrderStatus.CANCEL);
    for (OrderItem orderItem : orderItems) {
      orderItem.cancel();
    }
  }

  //==조회로직==//

  /**
   * 전체 주문 가격 조회
   */
  public int getTotalPrice() {
//    int totalPrice = 0;
//    for (OrderItem orderItem : orderItems) {
//      totalPrice += orderItem.getTotalPrice();
//    }
//    return totalPrice;
    int totalPrice = orderItems.stream()
            .mapToInt(OrderItem::getTotalPrice)
            .sum();
    return totalPrice;
  }
}
