(ns com.github.dbasner.this-or-that.state
  (:require [com.github.dbasner.this-or-that.game-logic :as game-logic]))


(def example-situation1 {:verb "eat"
                         :count-modifier "exactly"
                         :count 2
                         :noun  "burgers"})

(def initial-state
  {:current-turn-player-id nil
   :situation nil
   :players nil
   :turns-left 5
   :situationA example-situation1
   :situationB example-situation1
   :winner :not-determined})

(def state (atom initial-state))


