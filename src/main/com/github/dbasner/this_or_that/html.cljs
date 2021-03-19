(ns com.github.dbasner.this-or-that.html
  (:require-macros [hiccups.core :as hiccups :refer [html]])
  (:require [hiccups.runtime :as hiccups]))


(defn GameSetup []
  [:div.game-setup
   [:label.game-setup-text {:for "newGameCount"} "How Many People Are Playing? "]
   [:input {:id    "newGamePlayerCount"
            :type  "number"
            :name  "new-game-player-count"
            :value 3
            :min   2}]
   [:input#startGameBtn.button.is-success.is-rounded
    {:type "submit" :value "Start Game!"}]])


(defn DescribeSituation
  [situation]
  (let [verb (:verb situation)
        count-modifier (:count-modifier situation)
        count (:count situation)
        noun (:noun situation)]
    [:div
     [:span.verb (str verb " ")
      [:span.count-modifier (str count-modifier) " "]
      [:span.count (str count " ")]
      [:span.noun noun]]]))

(defn PageTitle []
  [:h1.display-3.game-title {:class "hero is-info is-medium"} "This or That!"])

(defn Situation
  [situation]
  (DescribeSituation situation))

(defn GameOverBanner
  [winners]
  (let [
        tie? (> (count winners) 1)
        winner (if (not tie?) (first winners) nil)
        result-alert (if tie?
                       (str "Players " (clojure.string/join ", " winners)" tied!")
                       (str "Player " winner " won!"))
        result-class (if tie? "tied" "winner")]
    [:div.banner-wrapper
     [:div {:class result-class}
      [:h2.alert-heading result-alert]
      [:button#newGameBtn {:class "button **is-large is-success is-rounded**"} "New Game"]]]))

(defn PlayerTurnBanner
  [current-player]
  [:div.banner-wrapper.turn-wrapper
   [:h3.label "Current Turn:"]
   [:span {:class "current-turn"} current-player]])

(defn Score
  [player-id score]
  [:div.indiv-score-player (str player-id ": ")
   [:span.indiv-score-amount score]])

(defn AllScoresBanner
  [scores]
  [:div.scorebanner
   [:h3 "Scores:"]
   (map
     (fn [indiv-score] (apply Score indiv-score)) scores)])

(defn ButtonSituationA []
  [:input {:id "voteSituationAButton" :class "voteButton button **is-large is-success is-rounded**" :type "button" :value "I'd rather do this"}])

(defn ButtonSituationB []
  [:input {:id "voteSituationBButton" :class "voteButton button **is-large is-success is-rounded**" :type "button" :value "I'd rather do that"}])

(defn SituationOptions
  [situationA situationB]
  [:div.game-wrapper
   [:div.situationA-container
    [:div.situationA (DescribeSituation situationA)]
    (ButtonSituationA)]
   [:div.versus " vs. "]
   [:div.situationB-container
    [:div.situationB (DescribeSituation situationB)]
    (ButtonSituationB)]])


;; for each situation, list the current-round's votes
(defn CurrentVotes [current-votes]
  [:div.current-votes
   [:div.situationA-votes
    [:span.label "Situation A: "]
    [:span.votes (clojure.string/join ", " (:situationA current-votes))]]
   [:div.situationB-votes
    [:span.label "Situation B: "]
    [:span.votes (clojure.string/join ", " (:situationB current-votes))]]])

(defn ThisOrThatGame
    [app-state]
    (let
      [result (:winners app-state)
       game-started? (not (zero? (count (:player-ids app-state))))
       game-over? (not (zero? (count (:winners app-state))))
       player-ids (:player-ids app-state)
       player-index (:current-voter-index app-state)]
      [:main.text-center
       (PageTitle)
       (if-not game-started?
         (GameSetup)
         [:div
          (AllScoresBanner (:scores app-state))
          (if game-over?
            (GameOverBanner result)
            [:div
             (PlayerTurnBanner (nth player-ids player-index))
             (SituationOptions (:situationA app-state) (:situationB app-state))])])]))
