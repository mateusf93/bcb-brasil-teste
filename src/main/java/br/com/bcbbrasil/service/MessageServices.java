package br.com.bcbbrasil.service;


import br.com.bcbbrasil.dto.CostumerDTO;
import br.com.bcbbrasil.dto.MessageDTO;
import br.com.bcbbrasil.models.Balance;
import br.com.bcbbrasil.models.BalanceType;
import br.com.bcbbrasil.models.Message;
import br.com.bcbbrasil.models.User;
import br.com.bcbbrasil.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServices {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    AuthService authService;
    @Autowired
    JwtService jwtService;
    @Autowired
    BalanceServices balanceServices;
    @Autowired
    UserServices userServices;

    public void sendMessage(MessageDTO messageDTO, String token) throws Exception {
        try {
            token = token.replace("Bearer ", "");
            String email = jwtService.getLoggedUserEmail(token);
            User user = authService.loadUserByUsername(email);
            Message message = new Message();
            message.setTextMessage(messageDTO.textMessage());
            message.setSendType(messageDTO.sendType());
            message.setTelefoneDest(messageDTO.telefoneDest());
            message.setUserEmail(email);
            message.setTelefoneOrig(user.getTelefone());

            Balance balance = balanceServices.getInfoBalanceByUser(user.getId());
            if (balance.getBalanceType().equals(BalanceType.POSTPAID)) {
                if (balance.getTotalSpent() == null) {
                    if (balance.getTotalBalance().doubleValue() < 0.25D) {
                        throw new Exception("Limite Insuficiente");
                    } else {
                        messageRepository.save(message);
                        balanceServices.updateTotalSpent(balance, new BigDecimal("0.25"));
                    }
                }
                if (balance.getTotalBalance().subtract(balance.getTotalBalance()).doubleValue() < 0.25D) {
                    throw new Exception("Limite Insuficiente");
                } else {
                    messageRepository.save(message);
                    balanceServices.updateTotalSpent(balance, new BigDecimal("0.25"));
                }
            }
            if (balance.getBalanceType().equals(BalanceType.PREPAID)) {
                if (balance.getCurrentBalance().doubleValue() < 0.25D) {
                    throw new Exception("Saldo Insuficiente");
                } else {
                    messageRepository.save(message);
                    balanceServices.updateCurrentBalance(balance, new BigDecimal("0.25"));
                    balanceServices.updateTotalSpent(balance, new BigDecimal("0.25"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<MessageDTO> userMessages(String token) throws Exception {
        CostumerDTO costumerDTO = userServices.getLoggedUserInfo(token);
        List<Message> messageList = messageRepository.findMessageByUserEmail(costumerDTO.email());
        List<MessageDTO> messageDTOs = new ArrayList<>();
        for (Message message : messageList) {
            MessageDTO messageDTO = new MessageDTO(message.getTextMessage(), message.getTelefoneDest(), message.getSendType());
            messageDTOs.add(messageDTO);
        }
        return messageDTOs;
    }


}
