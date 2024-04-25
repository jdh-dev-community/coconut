package com.jdh.community_spring.domain.post.repository;

import com.jdh.community_spring.domain.post.domain.Comment;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public interface CustomBaseRepository {
  default OrderSpecifier[] extractOrder(Sort sort, PathBuilder<?> entityPath) {
    List<OrderSpecifier> orders = new ArrayList<>();

    if (sort.isSorted()) {
      for (Sort.Order order : sort) {
        PathBuilder<Object> path = entityPath.get(order.getProperty());
        Order selectedOrder = order.isAscending() ? Order.ASC : Order.DESC;

        orders.add(new OrderSpecifier(selectedOrder, path));
      }
    }

    return orders.toArray(new OrderSpecifier[0]);
  }
}
