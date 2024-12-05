package cat.itacademy.s05.t01.n01.service;

import cat.itacademy.s05.t01.n01.model.Card;
import cat.itacademy.s05.t01.n01.model.Deck;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DeckServiceImpl implements DeckService{

    @Override
    public Deck createDeck() {
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        List<Card> cards = new ArrayList<>();

        for (String suit : suits) {
            for (String rank : ranks) {
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
        return deck.getCards().remove(0);
    }

    @Override
    public void resetDeck(Deck deck) {
        Deck newDeck = createDeck();
        deck.setCards(newDeck.getCards());
        shuffleDeck(deck);
    }
}
