(ns com.github.dbasner.this-or-that.html
  (:require-macros [hiccups.core :as hiccups :refer [html]])
  (:require [hiccups.runtime :as hiccups]))


(defn describe-situation
  [situation player-id]
  (let [verb (:verb situation)
        count-modifier (:count-modifier situation)
        count (:count situation)
        noun (:noun situation)]
    [:div
     [:span {:class (str "verb " player-id)} (str verb " ")
      [:span {:class (str "count-modifier " player-id)} (str count-modifier) " "]
      [:span {:class (str "count " player-id)} (str count " ")]
      [:span {:class (str "noun " player-id)}] noun]]))

(defn PageTitle []
  [:h1.display-3 "This or That!"])

(defn Situation
  [situation]
  (describe-situation situation "player1"))

(defn GameOverBanner
  [result]
  (let [tied-players (:tie result)
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

;;this contains a banner
(defn PlayerTurnBanner
  [current-player]
  [:div.banner-wrapper.turn-wrapper
   [:span.label "Current Turn: "]
   [:span {:class "current-turn"} current-player]])


(defn SituationOptions
  [situationA situationB]
  [:div "TODO-SITUATION-OPTIONS"])

(defn ThisOrThatGame
  [app-state]
  (let
     [current-player (:current-turn-player-id app-state)
      result (:result app-state)
      turns-left (:votes-left app-state)
      game-started? (nil? current-player)
      game-over? (and game-started? (= 0 turns-left))]
     [:main.text-center
      (PageTitle)
      (if game-over?
        (GameOverBanner result)
        (PlayerTurnBanner current-player))
      (SituationOptions (:situationA app-state) (:situationB app-state))]))




