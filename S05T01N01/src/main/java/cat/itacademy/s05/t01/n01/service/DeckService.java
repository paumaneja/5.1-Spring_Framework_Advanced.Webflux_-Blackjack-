package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Deck;
import org.springframework.stereotype.Service;

@Service
public interface DeckService {
    Deck createDeck();
    void shuffleDeck(Deck deck);
    Card dealCard(Deck deck);
    void resetDeck(Deck deck);
}
