package br.com.bcbbrasil.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String textMessage;

    @Column(nullable = false)
    private String telefoneOrig;

    @Column(nullable = false)
    private String telefoneDest;

    @Column
    private String userEmail;



    private SendType sendType;

}
