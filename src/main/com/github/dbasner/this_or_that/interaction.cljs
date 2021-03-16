(ns com.github.dbasner.this-or-that.interaction
  (:require [oops.core :refer [oset! ocall oget]]
            [goog.dom :as gdom]
            [goog.functions :as gfunctions]
            [com.github.dbasner.this-or-that.game-logic :refer [generate-situation]]
            [com.github.dbasner.this-or-that.state :refer [state]]
            [com.github.dbasner.this-or-that.html :refer [ThisOrThatGame]]
            [clojure.pprint :as pp])
  (:require-macros [hiccups.core :as hiccups]))

(defn add-vote
  [state player-id situation-id]
  (update-in state [:current-round-votes situation-id] conj player-id))

;; todo this could just be a continuous vote count that we modulo player-count
(defn rotate-current-player
  [state]
  (let [max-player-index (dec (count (:player-ids state)))
        voter-index (:current-voter-index state)
        update-current-player #(assoc state :current-voter-index %)]
    (if (< voter-index max-player-index)
      (update-current-player (inc voter-index))
      (update-current-player 0))))

(defn increment-score [state player-id]
  (update-in state [:scores player-id] inc))

(defn increment-mult-scores
  [state player-ids]
  (reduce #(increment-score %1 %2) state player-ids))

(defn add-scores
  [old-state]
  (let [
        situationA-votes  (:situationA (:current-round-votes old-state))
        situationB-votes (:situationB (:current-round-votes old-state))
        situationA-count (count situationA-votes)
        situationB-count (count situationB-votes)]
    (cond
      (= situationA-count situationB-count) (increment-mult-scores old-state (:player-ids state))
      (> situationA-count situationB-count) (increment-mult-scores old-state situationA-votes)
      (< situationA-count situationB-count) (increment-mult-scores old-state situationB-votes)
      :else old-state)))

(defn generate-new-situations
  [state]
  (-> state
      (assoc :situationA (generate-situation))
      (assoc :situationB (generate-situation))))

(defn reset-votes
  [state]
  (-> state
      (assoc-in [:current-round-votes :situationA] [])
      (assoc-in [:current-round-votes :situationB] [])))


(defn dec-rounds-left
  [state]
  (update-in state [:rounds-left] dec))

(defn decide-winners
  [state]
  (let [scores (:scores state)
        max-value (apply max (vals scores))
        winners (filter #(= (second %) max-value) scores)]
    (assoc-in state [:winners] (keys winners))))

(defn prepare-next-round [state]
  (-> state
      (add-scores)
      (dec-rounds-left)
      (reset-votes)
      (generate-new-situations)))

(defn vote-and-rotate [state player-id situation-id]
  (-> state
      (add-vote player-id situation-id)
      (rotate-current-player)))

(defn handle-vote
  [state player-id situation-id]
  "handles everything related to voting"
  (let [state-voted-rotated (vote-and-rotate state player-id situation-id)
        is-new-round? (= 0 (:current-voter-index state-voted-rotated))
        new-round-state (prepare-next-round state-voted-rotated)]
    (if is-new-round?
      (if (< (:rounds-left new-round-state) 1)
        (decide-winners new-round-state)
        new-round-state)
      state-voted-rotated)))

(defn handle-vote!
  [player-id situation-id]
  (swap! state (fn [new-state] (handle-vote new-state player-id situation-id))))


(defn get-current-player
  [state]
  (nth (:player-ids state)  (:current-voter-index state)))

(defn click-app-container
  [event]
  (let [target-el (oget event "target")
        isSituationAButton? (= (oget target-el "id") "voteSituationAButton")
        isSituationBButton? (= (oget target-el "id") "voteSituationBButton")
        player-id (get-current-player @state)]
    (cond
      isSituationAButton? (handle-vote! player-id :situationA)
      isSituationBButton? (handle-vote! player-id :situationB)
      :else nil)))

(def init-dom-events!
  (gfunctions/once
    (fn []
      (ocall (gdom/getElement "appContainer") "addEventListener" "click" click-app-container))))

(defn set-app-html!
  [html-str]
  (let [el (gdom/getElement "appContainer")]
    (oset! el "innerHTML" html-str)))

(defn trigger-render!
  []
  (swap! state identity))

(defn render-ui!
  [_ _kwd _prev-state new-state]
  (set-app-html! (hiccups/html (ThisOrThatGame new-state))))


(def init-watchers!
  (gfunctions/once
    (fn []
      (add-watch state :render-ui render-ui!))))

