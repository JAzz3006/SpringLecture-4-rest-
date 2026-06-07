package com.example.rest.rest.model;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "client_name")
    private String name;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @ToStringExclude
    @Builder.Default
    private List<Order> orders = new ArrayList<>();

    public void addOrder(Order order){
        if (orders == null){
            orders = new ArrayList<>();
        }
        orders.add(order);
    }

    public void removeOrder(Long id){
         orders = orders.stream()
                 .filter(o -> !o.getId().equals(id))
                 .toList();
    }
}