(ns com.github.dbasner.this-or-that.html
  (:require-macros [hiccups.core :as hiccups :refer [html]])
  (:require [hiccups.runtime :as hiccups]))


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
  [:h1.display-3 "This or That!"])

(defn Situation
  [situation]
  (DescribeSituation situation))

(defn GameOverBanner
  [result]
  (let [tied-players (:tie result)
        ;;todo nil? empty? runtime error?
        tie? (nil? tied-players)
        winner (if (not tie?) (:winner result) nil)
        result-alert (if tie?
                       (str "Players " (clojure.string/join ", " tied-players)" tied!")
                       (str "Player " winner " won!"))
        result-class (if winner "winner" "tied")]
    [:div.banner-wrapper
     [:div {:class result-class}
      [:h2.alert-heading result-alert]
      [:button#newGameBtn.btn.btn-primary "New Game"]]]))

(defn PlayerTurnBanner
  [current-player]
  [:div.banner-wrapper.turn-wrapper
   [:span.label "Current Turn: "]
   [:span {:class "current-turn"} current-player]])

(defn Score
  [player-id score]
  [:div.indiv-score-player player-id
   [:div.indiv-score-amount score]])

(defn AllScoresBanner
  [scores]
  [:div.scorebanner
   [:h1 "Scores:"]
   (map
     (fn [indiv-score] (apply Score indiv-score)) scores)])

(comment (AllScoresBanner {"player-1" 0
                           "player-2" 0
                           "player-3" 1}))

(defn ButtonSituationA []
  [:input {:id "voteSituationAButton" :class "voteButton" :type "button" :value "I'd rather do this"}])

(defn ButtonSituationB []
  [:input {:id "voteSituationBButton" :class "voteButton" :type "button" :value "I'd rather do that"}])

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
      [current-player (nth (:player-ids app-state) (:current-voter-index app-state))
       result (:winner app-state)
       rounds-left (:rounds-left app-state)
       game-started? (nil? current-player)
       game-over? (not= :not-determined (:winner app-state))]
      [:main.text-center
       (PageTitle)
       (if game-over?
         (GameOverBanner result)
         [:div
          (AllScoresBanner (:scores app-state))
          (PlayerTurnBanner current-player)
          (CurrentVotes (:current-round-votes app-state))
          (SituationOptions (:situationA app-state) (:situationB app-state))])]))




