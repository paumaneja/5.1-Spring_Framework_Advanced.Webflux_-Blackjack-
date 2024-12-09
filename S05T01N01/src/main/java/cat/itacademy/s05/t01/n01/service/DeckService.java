package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Deck;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface DeckService {
    Mono<Deck> createDeck();
    Mono<Void> shuffleDeck(Deck deck);
    Mono<Card> dealCard(Deck deck);
    Mono<Void> resetDeck(Deck deck);
}
