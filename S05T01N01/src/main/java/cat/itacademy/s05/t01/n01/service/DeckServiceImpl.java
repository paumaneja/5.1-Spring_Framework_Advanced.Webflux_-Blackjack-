package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.enums.Rank;
import cat.itacademy.s05.t01.n01.enums.Suit;
import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Deck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DeckServiceImpl implements DeckService{

    private final CardService cardService;

    @Autowired
    public DeckServiceImpl(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public Deck createDeck() {
        List<Card> cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        Deck deck = new Deck();
        deck.setCards(cards);
        return deck;
    }

    @Override
    public void shuffleDeck(Deck deck) {
        Collections.shuffle(deck.getCards());
    }

    @Override
    public Card dealCard(Deck deck) {
        if (deck.getCards().isEmpty()) {
            throw new DeckEmptyException("The deck is empty.");
        }
        Card card = deck.getCards().remove(0);
        cardService.flipCard(card);
        return card;
    }

    @Override
    public void resetDeck(Deck deck) {
        Deck newDeck = createDeck();
        deck.setCards(newDeck.getCards());
        shuffleDeck(deck);
    }
}
