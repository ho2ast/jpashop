package jpabook.jpashop.service.query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<jpabook.jpashop.service.query.OrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<jpabook.jpashop.service.query.OrderDto> collect = orders.stream()
                .map(o -> new jpabook.jpashop.service.query.OrderDto(o))
                .collect(toList());
        return collect;
    }



}
