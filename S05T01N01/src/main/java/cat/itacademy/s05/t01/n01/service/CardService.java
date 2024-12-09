package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Card;
import org.springframework.stereotype.Service;

@Service
public interface CardService {
    int calculateValue(Card card);
    void flipCard(Card card);
}
