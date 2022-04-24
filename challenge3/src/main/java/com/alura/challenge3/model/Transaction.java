package com.alura.challenge3.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document("transaction")
public class Transaction {

    private String id;

    private String bankOrigin;
    private String branchOrigin;
    private String accountOrigin;

    private String bankDestination;
    private String branchDestination;
    private String accountDestination;

    private BigDecimal amount;
    private LocalDateTime dateTime;

    private LocalDateTime fileProcessedDate;

}
