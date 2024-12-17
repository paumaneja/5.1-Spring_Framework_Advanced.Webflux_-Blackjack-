package cat.itacademy.s05.t01.n01.service.Impl;

import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.service.CardService;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {

    @Override
    public int calculateValue(Card card) {
        return switch (card.getRank()) {
            case ACE -> 11;
            case KING, QUEEN, JACK -> 10;
            default -> card.getRank().ordinal() + 2;
        };
    }

    @Override
    public void flipCard(Card card) {
        card.setFaceUp(!card.isFaceUp());
    }
}
