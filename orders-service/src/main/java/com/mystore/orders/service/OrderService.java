package com.mystore.orders.service;

import com.mystore.orders.dto.OrderRequest;
import com.mystore.orders.dto.OrderResponse;
import com.mystore.orders.dto.Product;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@RefreshScope
@Service
public class OrderService {

    String GET_PROD_URL = "http://product-service/products/{id}";

    @Autowired
    RestTemplate restTemplate;

    @Value("${db.name}")
    private String orgLocation;

    @Autowired
    private DiscoveryClient discoveryClient;

    public OrderResponse placeOrder(OrderRequest orderRequest) {

        List<ServiceInstance> instances = discoveryClient.getInstances("product-service");

        Integer prodId = orderRequest.getProductId();
        String url = GET_PROD_URL.replace("{id}", prodId.toString());
        System.out.println("URL " + url);

        ResponseEntity<Product> productResponseEntity = restTemplate.getForEntity(url, Product.class);
        Product product = productResponseEntity.getBody();



        Long ordId = new Random().nextLong();
        Double totalPrice = orderRequest.getQty() * product.getPrice();

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(ordId);
        orderResponse.setProductId(prodId);
        orderResponse.setTotalPrice(totalPrice);
        orderResponse.setQty(orderRequest.getQty());
        orderResponse.setProductName(product.getName());



        return orderResponse;

    }

    public String callGreeting(String name) {
        String url = "http://product-service/greetings/sayHi/" + name;

        ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);

        return resp.getBody() + "Org location: " + orgLocation;
    }

}
