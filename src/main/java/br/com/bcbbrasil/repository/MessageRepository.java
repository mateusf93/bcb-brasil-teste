package br.com.bcbbrasil.repository;

import br.com.bcbbrasil.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository  extends JpaRepository<Message, Long> {
    List<Message> findMessageByUserEmail(String email);
}
