package compensation.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import compensation.InventoryApplication;
import compensation.domain.OutOfStock;
import compensation.domain.StockDecreased;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Inventory_table")
@Data
//<<< DDD / Aggregate Root
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long stock;

    public static InventoryRepository repository() {
        InventoryRepository inventoryRepository = InventoryApplication.applicationContext.getBean(
            InventoryRepository.class
        );
        return inventoryRepository;
    }

    //<<< Clean Arch / Port Method
    public static void decreaseStock(OrderPlaced orderPlaced) {
        //implement business logic here:

        repository().findById(
            Long.valueOf(orderPlaced.getProductId())
            ).ifPresent(inventory->{
                if(inventory.getStock() >= orderPlaced.getQty()) { // 재고 있음
                    inventory.setStock(inventory.getStock() - orderPlaced.getQty());
                    repository().save(inventory);

                    StockDecreased stockDecreased = new StockDecreased(inventory);
                    stockDecreased.publishAfterCommit();
                } else { // 재고 없음
                    OutOfStock outOfStock = new OutOfStock(inventory); // 같은 이름을 가진 애들을 복사함. 근데
                    outOfStock.setOrderId(orderPlaced.getId()); // orderId가 비어서 오니까 추가로 설정해줘야 함
                    outOfStock.publishAfterCommit();
                }
         });
    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void increaseStock(OrderCancelled orderCancelled) {
        //implement business logic here:

        /** Example 1:  new item 
        Inventory inventory = new Inventory();
        repository().save(inventory);

        */

        /** Example 2:  finding and process
        

        repository().findById(orderCancelled.get???()).ifPresent(inventory->{
            
            inventory // do something
            repository().save(inventory);


         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
