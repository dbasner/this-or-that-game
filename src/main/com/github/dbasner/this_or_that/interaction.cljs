(ns com.github.dbasner.this-or-that.interaction
  (:require [oops.core :refer [oset! ocall oget]]
            [goog.dom :as gdom]
            [goog.functions :as gfunctions]
            [com.github.dbasner.this-or-that.game-logic :as game-logic]
            [com.github.dbasner.this-or-that.state :refer [state initial-state]]
            [com.github.dbasner.this-or-that.html :refer [ThisOrThatGame]])
  (:require-macros [hiccups.core :as hiccups]))

(defn get-current-player
  [state]
  (nth (:player-ids state) (:current-voter-index state)))

(defn handle-vote!
  [situation-id]
  (swap! state
         (fn [current-state]
           (let [player-id (get-current-player current-state)]
             (game-logic/process-vote current-state player-id situation-id)))))

(defn start-new-game! [count]
  (swap! state #(game-logic/reset-game-state % count)))

(defn click-app-container
  [event]
  (let [target-el (oget event "target")
        situation-A-btn? (= (oget target-el "id") "voteSituationAButton")
        situation-B-btn? (= (oget target-el "id") "voteSituationBButton")
        new-game-btn? (= (oget target-el "id") "newGameBtn")
        start-game-btn? (= (oget target-el "id") "startGameBtn")]
    (cond
      start-game-btn? (start-new-game! (oget (gdom/getElement "newGamePlayerCount") "value"))
      situation-A-btn? (handle-vote! :situationA)
      situation-B-btn? (handle-vote! :situationB)
      new-game-btn? (reset! state initial-state)
      :else nil)))

(def attach-dom-events!
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
      ; (add-watch state :logger logger))))
