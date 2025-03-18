package compensation.domain;

import compensation.domain.OrderPlaced;
import compensation.domain.OrderCancelled;
import compensation.OrderApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;


@Entity
@Table(name="Order_table")
@Data

//<<< DDD / Aggregate Root
public class Order  {


    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
 
    private Long id;

    private String productId;
    
    private Integer qty;
    
    private String customerId;
    
    private Double amount;
    
    private String status;

    private String address;

    @PostPersist
    public void onPostPersist(){
        OrderPlaced orderPlaced = new OrderPlaced(this);
        orderPlaced.publishAfterCommit();
    }

    @PreRemove
    public void onPreRemove() {
        OrderCancelled orderCancelled = new OrderCancelled(this);
        orderCancelled.publishAfterCommit();
    }
    
    public static OrderRepository repository(){
        OrderRepository orderRepository = OrderApplication.applicationContext.getBean(OrderRepository.class);
        return orderRepository;
    }

    // 재고 부족으로 인해 주문 상태 업데이트
    public static void updateStatus(OutOfStock outOfStock){ // OutOfStock이 들어왔을 때, 업데이트 해라
        //implement business logic here:
        // Example 2:  finding and process
        repository().findById( // 내 저장소에서 Id를 찾아.
            outOfStock.getOrderId() // OutOfStock 중에 orderId가 있네.
            ).ifPresent(order->{
            
                order.setStatus("OrderCancelled"); // do something - status를 바꿔야겠다!!
                repository().save(order);
         });  
    }
//>>> Clean Arch / Port Method


}
//>>> DDD / Aggregate Root
