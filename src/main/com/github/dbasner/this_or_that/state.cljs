(ns com.github.dbasner.this-or-that.state
  (:require [com.github.dbasner.this-or-that.game-logic :as game-logic]))


(def example-situation1 {:verb "eat"
                         :count-modifier "exactly"
                         :count 2
                         :noun  "burgers"})
(def example-situation2 {:verb "eat"
                         :count-modifier "exactly"
                         :count 5
                         :noun  "hotdogs"})

(def initial-state
  {:player-ids ["player-1" "player-2" "player-3"]
   :current-voter-index 0
   :scores {"player-1" 0
            "player-2" 0
            "player-3" 0}
   :current-round-votes {:situationA []
                         :situationB []}
   :situationA example-situation1
   :situationB example-situation2
   :rounds-left 5
   :winner :not-determined})

(def state (atom initial-state))

