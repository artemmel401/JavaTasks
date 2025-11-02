package ru.artem.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.artem.NauJava.repository.HallRepository;
import ru.artem.NauJava.repository.SeatRepository;

@Service
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public HallServiceImpl (HallRepository hallRepository, SeatRepository seatRepository,
                            PlatformTransactionManager transactionManager)
    {
        this.hallRepository = hallRepository;
        this.seatRepository= seatRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public void deleteHall(Long id) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            seatRepository.deleteByHallId(id);
            hallRepository.deleteById(id);
            transactionManager.commit(status);
        }
        catch (DataAccessException ex) {
            transactionManager.rollback(status);
            throw ex;
        }
    }
}
