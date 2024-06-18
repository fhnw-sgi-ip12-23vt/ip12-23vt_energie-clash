package ch.graueenergie.energieclash.view.util;

import ch.graueenergie.energieclash.util.Language;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Translation {
    ANY_BUTTON(createTranslationTextMap("Drücken Sie einen Knopf", "Press any button", "Appuyez un boutton")),
    WAITING_FOR_PLAYER_TEXT(createTranslationTextMap("Warten auf anderen Spieler",
        "Waiting for other player",
        "En attendant un autre joueur")),
    EXPLANATION_TITLE(createTranslationTextMap("Erklärung:", "Explanation:", "Explication:")),
    END_TITLE(createTranslationTextMap("Ende", "End", "Fin")),
    END_WINNER_TEXT(createTranslationTextMap("Der Gewinner ist: ", "The winner is: ", "Le gagnant est: ")),
    END_DRAW_TEXT(createTranslationTextMap("Unentschieden", "Draw", "Égalité")),
    SAVER_NAME(createTranslationTextMap("Sparer", "Saver", "Épargnant")),
    WASTER_NAME(createTranslationTextMap("Verschwender", "Waster", "Gaspilleur")),
    DIFFICULTY_EASY(createTranslationTextMap("Einfach", "Easy", "Facile")),
    DIFFICULTY_MEDIUM(createTranslationTextMap("Mittel", "Medium", "Moyen")),
    DIFFICULTY_HARD(createTranslationTextMap("Schwer", "Hard", "Difficile")),
    GAME_MODE_TURNBASED(createTranslationTextMap("Rundenbasiert", "Turn-based", "Tour par tour")),
    GAME_MODE_RAPIDFIRE(createTranslationTextMap("Schnellfeuer", "Rapidfire", "Tir rapide")),
    TUTORIAL_DIFFICULTY(createTranslationTextMap(
        """
            Das Spiel wird auf dem Schwierigkeitsgrad "%s" gespielt.

            Die erste Fragerunde wird in Kürze beginnen.""",
        """
            The game is set to difficulty "%s".

            The first round will begin shortly.""",
        """
            Le jeu se joue au niveau de difficulté "%s".

            Le premier tour va bientôt commencer.""")),
    TUTORIAL_ROLE_TITLE(createTranslationTextMap("Du hast die Rolle %s!", "You have the role %s!", "Tu as le role du!")),
    TUTORIAL_ROLE_SAVER_TEXT(createTranslationTextMap(
        """
            Deine Aufgabe ist es so wenig Graue Energie zu verbrauchen wie möglich.
            Dies kannst du tun in dem du die Antwort gibst welche am wenigsten Graue Energie verbraucht.""",
        """
            Your task is to use as little grey energy as possible.
            You can do this by giving answers which use the least grey energy.""",
        """
            Ta tâche est d’utiliser le moins d’énergie grise possible.
            Tu peux le faire en donnant des réponses qui utilisent le moins d’énergie grise.""")),
    TUTORIAL_ROLE_WASTER_TEXT(createTranslationTextMap(
        """
            Deine Aufgabe ist es so viel Graue Energie zu verbrauchen wie möglich.
            Dies kannst du tun in dem du die Antwort gibst welche am meisten Graue Energie verbraucht.""",
        """
            Your task is to use as much grey energy as possible.
            You can do this by giving answers which use the most grey energy.""",
        """
            Ta tâche est d’utiliser le plus d’énergie grise possible.
            Tu peux le faire en donnant des réponses qui utilisent le plus d’énergie grise.""")),
    TUTORIAL_BUTTON_GREY_ENERGY(
        createTranslationTextMap("Was ist graue Energie", "What is grey energy", "C'est quoi l'énergie grise")),
    TUTORIAL_BUTTON_TUTORIAL(
        createTranslationTextMap("Wie spielt man", "How to play", "Comment jouer")),
    TUTORIAL_BUTTON_SKIP(createTranslationTextMap("Überspringen", "Skip", "Sauter")),
    TUTORIAL_GREY_ENERGY_IMAGE(
        createTranslationTextMap("/images/greyEnergyDE.png", "/images/greyEnergyEN.png", "/images/greyEnergyFR.png")),
    TUTORIAL_GREY_ENERGY(createTranslationTextMap(
        """
            Graue Energie beschreibt sämtliche,
            für die Herstellung eines Produkts
            oder Dienstleistung gebrauchte Energie
            bis hin zur Entsorgung.""",
        """
            Grey energy is all the energy used,
            for production of a product
            or service
            until its disposal.""",
        """
            L'énergie grise décrit toute l'énergie
            pour fabriquer un produit
            ou service d'énergie utilisée
            jusqu'à l'élimination.""")),
    TUTORIAL_TUTORIAL_TEXT(createTranslationTextMap(
        """
            Es gibt zwei Rollen, den Energie-Sparer und den Energie-Verschwender.
            Beide Rollen bekommen die gleiche Frage, müssen diese aber rollenspezifisch beantworten.
            Nach dem Beantworten der Frage wird die Antwort bewertet von 1-5 und die Differenz der Antwort beider Rollen wird in die Mitte des Spielfelds übertragen.""",
        """
            There are two roles, the energy saver and the Energy wasters.
            Both roles get the same question, but have to answer it in a role-specific manner.
            After answering the question, the answer is rated from 1-5 and the difference in the answer of both roles
            is transferred to the middle of the playing field.""",
        """
            Il y a deux rôles, l'économiseur d'énergie et le gaspilleur d’énergie.
            Les deux rôles se voient poser la même question,
            mais doivent y répondre d'une manière spécifique à leur rôle.
            Après avoir répondu à la question,
            la réponse est notée de 1 à 5 et la différence dans la réponse des deux rôles
            est transférée au milieu du terrain de jeu.""")),
    BUTTON_CONTINUE(createTranslationTextMap("Weiter", "Continue", "Continuer")),
    DIFFICULTY_TEXT(createTranslationTextMap(
        "Mit welchem Schwierigkeitsgrad möchtest du Spielen?",
        "What difficulty would you like to play on?",
        "Avec quelle difficulté aimeriez-vous jouer?")),
    CHOOSE_GAMEMODE(createTranslationTextMap("Wähle einen Spielmodus",
        "Choose a gamemode",
        "Choisissez un mode de jeu"));
    private final Map<Language, String> translationTextMap;

    Translation(Map<Language, String> translationTextMap) {
        this.translationTextMap = translationTextMap;
    }

    /**
     * Creates a {@link Map} of {@link Language}s and the corresponding translation.
     *
     * @param text A series of {@link String}s. Enter in same order as {@link Language} enums!
     * @return A {@link Map} of {@link Language}s and the corresponding translation.
     */
    private static Map<Language, String> createTranslationTextMap(String... text) {
        Map<Language, String> translationTextMap = new HashMap<>();
        for (int i = 0; i < Language.values().length; i++) {
            translationTextMap.put(Language.values()[i], text[i]);
        }
        return translationTextMap;
    }

    public String getTextForLanguage(Language language) {
        return translationTextMap.get(language);
    }

    public List<String> getTranslations() {
        return translationTextMap.values().stream().toList();
    }
}
