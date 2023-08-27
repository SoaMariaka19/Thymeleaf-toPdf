package com.prog4.employee_db.service;

import com.prog4.employee_db.entity.NationalCard;
import com.prog4.employee_db.repository.NationalCardRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class NationalCardService {
    private final NationalCardRepository repository;

    public List<NationalCard> findAll(){
        return repository.findAll();
    }

    public NationalCard findById(Long id){
        Optional<NationalCard> nationalCard = repository.findById(id);
        NationalCard nationalCard1 = new NationalCard();
        if(nationalCard.isPresent()){
            nationalCard1.setDate(nationalCard.get().getDate());
            nationalCard1.setNumber(nationalCard.get().getNumber());
            nationalCard1.setPlace(nationalCard.get().getPlace());
        }
        nationalCard1.setId(id);
        return nationalCard1;
    }

    public NationalCard save(NationalCard card){
        NationalCard nationalCard = repository.save(card);
        return this.findById(nationalCard.getId());
    }
    public NationalCard getByNumber(String number){
        Optional<NationalCard> nationalCard = repository.findNationalCardByNumberEqualsIgnoreCase(number);
        NationalCard nationalCard1 = new NationalCard();
        if(nationalCard.isPresent()){
            nationalCard1.setDate(nationalCard.get().getDate());
            nationalCard1.setNumber(nationalCard.get().getNumber());
            nationalCard1.setPlace(nationalCard.get().getPlace());
        }
        return nationalCard1;
    }
}
